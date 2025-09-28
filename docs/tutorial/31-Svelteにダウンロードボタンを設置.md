# 8-2. Svelteにダウンロードボタンを設置

前のチャプターでCSVダウンロードAPIを実装しました。今回は、フロントエンドにダウンロードボタンを追加し、ユーザーがブラウザから直接CSVファイルをダウンロードできるようにします。

## 1. ダウンロードボタンを一覧画面に追加する

`app/assets/svelte/screens/tasks/Page.svelte` を開き、検索フォームの隣にCSVダウンロードボタンを追加します。

```svelte
<!-- ... 既存のコード ... -->
  <CardHeader>
    <div class="d-flex justify-content-between align-items-center">
      <span>タスク一覧</span>
      <div>
        <!-- ↓↓↓ ここから変更 ↓↓↓ -->
        <a href={csvDownloadUrl} download class="btn btn-success btn-sm me-2">
          CSVダウンロード
        </a>
        <!-- ↑↑↑ ここまで変更 ↑↑↑ -->
        <Button color="primary" size="sm" href="/tasks/new">新規登録</Button>
      </div>
    </div>
  </CardHeader>
<!-- ... 既存のコード ... -->
<script lang="ts">
  // ... 既存のコード ...

  let keyword = $state('');
  let status = $state('');

  // ↓↓↓ ここから追加 ↓↓↓
  const csvDownloadUrl = $derived(() => {
    const params = new URLSearchParams();
    if (keyword) {
      params.append('keyword', keyword);
    }
    if (status) {
      params.append('status', status);
    }
    const queryString = params.toString();
    return `/api/tasks/export${queryString ? `?${queryString}` : ''}`;
  });
  // ↑↑↑ ここまで追加 ↑↑↑

  async function loadTasks() {
  // ... 既存のコード ...
</script>
```

### 実装のポイント

- **`<a>`タグによるダウンロード**: CSVダウンロードは、APIを`fetch`で呼び出すのではなく、単純な`<a>`タグ（アンカータグ）で実現します。`href`属性にAPIのエンドポイントを指定し、`download`属性を付与することで、ブラウザはリンク先のURLの内容を画面遷移ではなく、ファイルとしてダウンロードしようとします。
- **`$derived`による動的なURL生成**: CSVダウンロードは、現在の一覧画面の検索条件（キーワード、ステータス）を反映する必要があります。Svelte 5の`$derived`を使うと、`keyword`や`status`といったリアクティブな変数が変更されるたびに、ダウンロード用のURL(`csvDownloadUrl`)が自動的に再計算されます。
- **`URLSearchParams`**: `URLSearchParams`は、URLのクエリ文字列を安全かつ簡単に構築するためのブラウザ標準のAPIです。これを使うことで、`?keyword=...&status=...`のような文字列を手動で組み立てる手間が省けます。
- **スタイリング**: `<a>`タグにBootstrapのCSSクラス (`btn`, `btn-success`, `btn-sm`) を適用して、ボタンのような見た目にしています。

これで実装は完了です。ブラウザでタスク一覧画面を開き、以下の点を確認してください。
1. 「CSVダウンロード」ボタンが表示されていること。
2. 検索フォームでキーワードやステータスを指定すると、ダウンロードボタンのリンク先URLが動的に変わること（ブラウザの開発者ツールで確認できます）。
3. ボタンをクリックすると、`tasks-yyyymmddhhmmss.csv`のようなファイル名でCSVファイルがダウンロードされること。
4. ダウンロードしたCSVファイルの内容が、画面に表示されているタスク一覧と一致していること。

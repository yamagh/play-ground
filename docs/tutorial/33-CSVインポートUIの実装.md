# 9-2. CSVインポートUIの実装

バックエンドにCSVインポートAPIができたので、フロントエンドにファイルをアップロードするためのUIを作成します。モーダルウィンドウ内にファイル選択ボタンとアップロードボタンを設置し、APIと連携させます。

このプロジェクトには、CSVインポート用のモーダルコンポーネント`CsvImportModal.svelte`があらかじめ用意されています。これを利用して実装を進めます。

`app/assets/svelte/components/CsvImportModal.svelte`
```svelte
// (このファイルは既にプロジェクトに存在します)
// ... ファイル選択、アップロード、エラー表示などのロジックが実装されている ...
<script lang="ts">
  // ...
  export let isOpen = false;
  export let uploadUrl: string;
  export let onUploaded: () => void;
  // ...
</script>
```
このコンポーネントは、モーダルの開閉状態(`isOpen`)、アップロード先のURL(`uploadUrl`)、アップロード完了時のコールバック関数(`onUploaded`)を`props`として受け取ります。

## 1. インポートボタンとモーダルを一覧画面に追加する

タスク一覧画面`Page.svelte`に、CSVインポートモーダルを開くためのボタンと、モーダルコンポーネント自体を配置します。

`app/assets/svelte/screens/tasks/Page.svelte`
```svelte
<!-- ... 既存のコード ... -->
      <span>タスク一覧</span>
      <div>
        <!-- ↓↓↓ ここから追加 ↓↓↓ -->
        <Button color="info" size="sm" class="me-2" on:click={() => (isImportModalOpen = true)}>
          CSVインポート
        </Button>
        <!-- ↑↑↑ ここまで追加 ↑↑↑ -->
        <a href={csvDownloadUrl} download class="btn btn-success btn-sm me-2">
          CSVダウンロード
        </a>
        <Button color="primary" size="sm" href="/tasks/new">新規登録</Button>
      </div>
<!-- ... 既存のコード ... -->
  </Card>
</div>

{#if selectedTask}
  <!-- ... 削除確認モーダル ... -->
{/if}

<!-- ↓↓↓ ここから追加 ↓↓↓ -->
<CsvImportModal
  isOpen={isImportModalOpen}
  uploadUrl="/api/tasks/import"
  onUploaded={handleUploaded}
/>
<!-- ↑↑↑ ここまで追加 ↑↑↑ -->

<script lang="ts">
  // ... 既存のコード ...
  import CsvImportModal from '../../components/CsvImportModal.svelte'; // インポート

  // ...
  let isDeleteModalOpen = false;
  // ↓↓↓ ここから追加 ↓↓↓
  let isImportModalOpen = $state(false);
  // ↑↑↑ ここまで追加 ↑↑↑

  function openDeleteConfirm(task: Task) {
    // ...
  }

  async function handleDelete() {
    // ...
  }

  // ↓↓↓ ここから追加 ↓↓↓
  function handleUploaded() {
    isImportModalOpen = false;
    loadTasks(); // 一覧を再読み込み
  }
  // ↑↑↑ ここまで追加 ↑↑↑
</script>
```

### 実装のポイント
- **ボタンの追加**: 「CSVインポート」ボタンを追加し、クリックされると`isImportModalOpen`が`true`になり、モーダルが表示されるようにします。
- **`CsvImportModal`コンポーネントの利用**:
    - `isOpen`: モーダルの表示状態を`isImportModalOpen`にバインドします。
    - `uploadUrl`: ファイルのアップロード先として、前のチャプターで作成したAPIエンドポイント`/api/tasks/import`を指定します。
    - `onUploaded`: アップロードが成功したときに呼び出されるコールバック関数として`handleUploaded`を渡します。
- **`handleUploaded`関数**: この関数では、モーダルを閉じた後、`loadTasks()`を呼び出してタスク一覧を更新します。これにより、インポートされた新しいタスクが画面に即座に反映されます。

## 2. 動作確認

これで実装は完了です。ブラウザで動作を確認しましょう。

1.  **サンプルCSVファイルの準備**: 以下のような内容のCSVファイル（例: `import.csv`）をテキストエディタで作成します。1行目はヘッダーです。文字コードは`UTF-8`で保存してください。
    ```csv
    タイトル,ステータス,期日
    新しいタスク1,未着手,2025-12-01
    新しいタスク2,進行中,
    "カンマ, を含むタスク",完了,2025-12-31
    ```

2.  **インポートの実行**:
    1.  タスク一覧画面で「CSVインポート」ボタンをクリックします。
    2.  モーダルが表示されたら、「ファイルを選択」ボタンで先ほど作成した`import.csv`を選択します。
    3.  「アップロード」ボタンをクリックします。

3.  **結果の確認**:
    - **成功した場合**: 「3件のタスクを登録しました。」のような成功メッセージが表示され、モーダルが閉じ、一覧画面が更新されて新しいタスクが表示されます。
    - **失敗した場合**: CSVのフォーマットが不正な場合（例: 日付の形式が違う、必須項目が空）、モーダル内に「2行目: 期日の日付形式が不正です」のようなエラーメッセージが表示されます。

これで、CSVを使ったデータの一括登録機能が完成しました。

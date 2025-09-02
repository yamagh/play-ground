# 2-3. 標準レイアウトコンポーネントの利用

現在のお知らせ一覧ページは、コンテンツ部分のみが表示されており、アプリケーション全体の共通ヘッダーやサイドメニューがありません。
この章では、このリポジトリに標準で用意されているレイアウト用のSvelteコンポーネントを使い、他のページとデザインを統一します。

## レイアウトコンポーネントの役割

このリポジトリでは、以下のようなSvelteコンポーネントが `app/assets/svelte/layouts` に用意されています。

- **`LayoutSideMenu.svelte`**: 左側のサイドメニューと上部のヘッダーを含む、アプリケーション全体のレイアウトを定義します。
- **`PageContainer.svelte`**: ページのタイトルや、コンテンツ部分の背景などを提供するコンポーネントです。

これらのコンポーネントを組み合わせることで、各ページで同じレイアウトコードを繰り返し書くことなく、統一感のあるUIを効率的に作成できます。

## `Page.svelte` の修正

それでは、`app/assets/svelte/screens/news/Page.svelte` を修正して、標準レイアウトを適用しましょう。

**`app/assets/svelte/screens/news/Page.svelte`**

```svelte
<script lang="ts">
  import LayoutSideMenu from "../../layouts/LayoutSideMenu.svelte";
  import PageContainer from "../../layouts/PageContainer.svelte";
  import type { MenuItem } from "../../layouts/MenuItem";

  // サイドメニューに表示する項目を定義します
  const menuItems: MenuItem[] = [
    {
      id: "tasks",
      label: "Tasks",
      link: "/tasks",
      icon: "bi-check2-square",
    },
    {
      id: "news",
      label: "News",
      link: "/news",
      icon: "bi-newspaper",
      active: true, // このページをアクティブとして表示
    },
  ];
</script>

<LayoutSideMenu title="News" {menuItems}>
  <PageContainer title="お知らせ一覧">
    <div class="card">
      <div class="card-body">
        <ul class="list-group list-group-flush">
          <li class="list-group-item">
            <p class="text-muted small">2025-09-01</p>
            <p class="fw-bold">システムメンテナンスのお知らせ</p>
          </li>
          <li class="list-group-item">
            <p class="text-muted small">2025-08-15</p>
            <p class="fw-bold">新機能のリリースについて</p>
          </li>
          <li class="list-group-item">
            <p class="text-muted small">2025-07-20</p>
            <p class="fw-bold">夏季休業のお知らせ</p>
          </li>
        </ul>
      </div>
    </div>
  </PageContainer>
</LayoutSideMenu>

<style>
  /* スタイルは空のままでOKです */
</style>
```

### コードの解説

1.  **`import`**:
    - `LayoutSideMenu` と `PageContainer` コンポーネントをインポートします。
    - `MenuItem` は、メニュー項目の型定義です。TypeScriptの機能で、どのようなプロパティ（`id`, `label`など）を持つべきかを定義しています。

2.  **`menuItems`**:
    - サイドメニューに表示する項目のリストを定義しています。
    - `active: true` を `news` の項目に設定することで、現在表示しているページがサイドメニュー上でハイライトされるようになります。

3.  **コンポーネントの利用**:
    - 元々あったコンテンツ全体を `<LayoutSideMenu>` と `<PageContainer>` で囲みます。
    - `<LayoutSideMenu>` には、ブラウザのタブに表示される `title` と、メニュー項目の `menuItems` を渡しています。
    - `<PageContainer>` には、ページのコンテンツエリアのヘッダーとして表示される `title` を渡しています。
    - `<PageContainer>` の中に書かれた要素（この場合は `div` とその中のリスト）が、`slot` というSvelteの機能を通じてレイアウトに埋め込まれ、最終的なページが完成します。

## 動作確認

ファイルを保存すると、`npm run hmr` プロセスが自動的に変更を検知し、ブラウザがリロードされます。
[http://localhost:9000/news](http://localhost:9000/news) を確認してください。

タスク一覧ページと同じように、ヘッダーとサイドメニューが表示され、コンテンツがその中にレイアウトされていれば成功です。サイドメニューの「News」がアクティブになっていることも確認しましょう。

---

これで、静的なページを作成し、共通のレイアウトを適用する手順は完了です。
次の章では、ハードコーディングされたデータではなく、サーバーサイドのAPIから動的にデータを取得して画面に表示する方法を学びます。

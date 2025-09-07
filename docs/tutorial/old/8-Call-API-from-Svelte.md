# 3-2. SvelteコンポーネントからAPIを呼び出す

前の章で作成したAPIエンドポイント (`/api/news`) を使って、お知らせ一覧ページの表示を動的にします。
SvelteコンポーネントがマウントされたタイミングでAPIを呼び出し、取得したデータで画面を更新する、という処理を実装します。

## `Page.svelte` の修正

`app/assets/svelte/screens/news/Page.svelte` を開き、以下のように修正してください。

**`app/assets/svelte/screens/news/Page.svelte`**

```svelte
<script lang="ts">
  import { $effect } from "svelte/reactivity";
  import LayoutSideMenu from "../../layouts/LayoutSideMenu.svelte";
  import PageContainer from "../../layouts/PageContainer.svelte";
  import type { MenuItem } from "../../layouts/MenuItem";

  // お知らせ一件を表す型を定義
  type NewsItem = {
    date: string;
    title: string;
  };

  // お知らせのリストを保持する変数
  // 初期値は空の配列
  let newsItems: NewsItem[] = $state([]);

  // サイドメニューの定義 (変更なし)
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
      active: true,
    },
  ];

  // コンポーネントがDOMにマウントされた後に実行される処理
  $effect(async () => {
    try {
      const response = await fetch("/api/news");
      if (response.ok) {
        newsItems = await response.json();
      } else {
        console.error("Failed to fetch news data.");
      }
    } catch (error) {
      console.error("Error fetching news data:", error);
    }
  });
</script>

<LayoutSideMenu title="News" {menuItems}>
  <PageContainer title="お知らせ一覧">
    <div class="card">
      <div class="card-body">
        <ul class="list-group list-group-flush">
          {#each newsItems as item}
            <li class="list-group-item">
              <p class="text-muted small">{item.date}</p>
              <p class="fw-bold">{item.title}</p>
            </li>
          {/each}
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

1.  **`import { $effect } from "svelte/reactivity";`**
    `$effect` はSvelte 5で導入されたRuneの一つです。`$effect` に渡されたコールバック関数は、コンポーネントがDOMにマウントされた後に実行されます。APIからのデータ取得など、初期化処理に利用できます。

2.  **`type NewsItem = { ... };`**
    TypeScriptの型エイリアスを使って、APIから返されるお知らせ一件のデータ構造を `NewsItem` 型として定義しています。これにより、`item.date` や `item.title` のようなプロパティアクセスが型安全になります。

3.  **`let newsItems: NewsItem[] = $state([]);`**
    お知らせのリストを格納するための状態変数を定義します。Svelte 5では `$state()` Runeを使ってリアクティブな「状態（state）」を宣言します。この変数の値が変更されると、Svelteは関連するDOMの部分を自動的に更新します。初期値として空の配列 `[]` を設定しています。

4.  **`$effect(async () => { ... });`**
    - `async/await` 構文を使って、非同期のAPI呼び出しを同期的なコードのように記述しています。
    - `fetch("/api/news")` はブラウザ標準のAPIで、指定されたURLにHTTPリクエストを送信します。
    - `response.ok` でHTTPステータスコードが200番台かどうかをチェックします。
    - `await response.json()` でレスポンスボディをJSONとしてパースします。
    - `newsItems = await response.json();`
      **ここがSvelteの最も強力な部分です。**
      APIから取得したデータの配列を `newsItems` 変数に**代入**するだけで、Svelteのリアクティビティが発動します。Svelteは `newsItems` の変更を検知し、テンプレートの `{#each ...}` ブロックを自動的に再評価して、新しいデータに基づいた `<li>` 要素をDOMに生成・挿入します。

5.  **`{#each newsItems as item}`**
    Svelteのテンプレート構文で、配列をループ処理するためのブロックです。`newsItems` 配列の各要素が `item` という名前で `<li>` の中で利用できます。最初は `newsItems` が空なので何も表示されませんが、APIからデータが取得されて `newsItems` が更新されると、リストが動的に描画されます。

## 動作確認

ファイルを保存し、ブラウザで [http://localhost:9000/news](http://localhost:9000/news) を確認してください。

一瞬だけリストが空の状態が見えるかもしれませんが、すぐにAPIからのデータ取得が完了し、「(APIから取得)」という文言が付いたお知らせ一覧が表示されるはずです。

---

これで、サーバーサイドAPIと連携して動的なデータを表示する基本的なサイクルが完成しました。
次の章では、データの更新処理、つまりユーザーの操作によってデータベースの情報を書き換える方法について学んでいきます。

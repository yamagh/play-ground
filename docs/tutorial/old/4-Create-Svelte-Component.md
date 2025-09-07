# 2-1. Svelteコンポーネントの作成

この章では、新しいページとして「お知らせ一覧」画面を想定し、そのUIとなるSvelteコンポーネントを作成します。

## 1. Svelteコンポーネント用のディレクトリを作成

まず、新しい画面コンポーネントを格納するためのディレクトリを作成します。
`app/assets/svelte/screens` の下に `news` というディレクトリを作成してください。

```
app/assets/svelte/screens/
└── news/
```

## 2. Svelteコンポーネントファイルの作成

次に、作成した `news` ディレクトリの中に `Page.svelte` という名前で新しいファイルを作成します。

**`app/assets/svelte/screens/news/Page.svelte`**

```svelte
<script lang="ts">
  // このセクションにはTypeScriptのコードを記述します。
  // 今回はまだロジックはないので空のままです。
</script>

<div class="container mt-4">
  <h1 class="mb-4">お知らせ一覧</h1>

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
</div>

<style>
  /* このコンポーネントにのみ適用されるスタイルを記述します */
</style>
```

### コードの解説

-   **`<script lang="ts">`**:
    このブロックには、コンポーネントのロジックをTypeScriptで記述します。今回は静的な表示のみなので、中身は空です。
-   **HTMLテンプレート**:
    `<div>` や `<h1>` などのHTMLタグを使って、コンポーネントの構造を定義します。ここではBootstrapのクラス（`container`, `card`, `list-group`など）を使ってスタイルを当てています。
-   **`<style>`**:
    このブロックに記述したCSSは、Svelteの機能によってこの`Page.svelte`コンポーネント内にのみ適用される（スコープされる）ため、他のコンポーネントのスタイルに影響を与える心配がありません。

## 3. エントリーポイントの作成

WebpackがこのSvelteコンポーネントをJavaScriptとしてビルドできるよう、エントリーポイントとなる `index.js` ファイルを作成します。

**`app/assets/svelte/screens/news/index.js`**

```javascript
import Page from "./Page.svelte";

const app = new Page({
  target: document.getElementById("app"),
});

export default app;
```

### コードの解説
- `import Page from "./Page.svelte";`
  先ほど作成したSvelteコンポーネントをインポートします。
- `new Page(...)`
  Svelteコンポーネントをインスタンス化します。
- `target: document.getElementById("app")`
  このオプションは、生成されたUIをHTML内のどこに挿入（マウント）するかを指定します。ここでは `id="app"` を持つDOM要素をターゲットにしています。このターゲットとなるHTMLは、次のステップでPlay Framework側で作成します。

---

これで、フロントエンドのUIコンポーネントの準備ができました。
次の章では、このコンポーネントを表示するためのサーバーサイドのロジック、つまりPlay FrameworkのControllerを作成します。

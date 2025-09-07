# 6. Svelteコンポーネントの作成

ここからは、実際に新しいページを作成する手順を学んでいきます。
まずは、フロントエンドのUIを担当するSvelteコンポーネントを作成しましょう。

このプロジェクトでは、新しい画面（スクリーン）は `app/assets/svelte/screens` ディレクトリ配下に作成するという規約になっています。

## 1. 画面用ディレクトリの作成

まず、新しい画面のためのディレクトリを作成します。ここでは例として、`my-page` という名前のページを作成してみましょう。

`app/assets/svelte/screens` の中に `my-page` というディレクトリを作成してください。

```
app/assets/svelte/screens/
├── login/
├── task-edit/
├── tasks/
└── my-page/  <-- これを新規作成
```

## 2. Page.svelteの作成

次に、作成した `my-page` ディレクトリの中に、`Page.svelte` という名前のファイルを作成します。
この `Page.svelte` が、新しいページのメインとなるSvelteコンポーネントになります。

ファイルを作成したら、以下の内容を記述してください。

**`app/assets/svelte/screens/my-page/Page.svelte`**:
```svelte
<script lang="ts">
  // TypeScriptを使用するための設定
</script>

<h1 class="text-2xl font-bold mb-4">マイページ</h1>

<p>これはSvelteで作成された新しいページです。</p>

<!-- ここにこのページ固有のコンテンツを記述していきます -->
```

### `Page.svelte` というファイル名について

このプロジェクトでは、各画面のルートコンポーネントは `Page.svelte` という名前に統一されています。
これは、バックエンドのPlay Framework側で、どのSvelteコンポーネントを読み込むかを動的に決定する仕組みになっているためです。Controllerから渡されたスクリーン名（例: `my-page`）を元に、`.../screens/my-page/Page.js` というパスを組み立ててJavaScriptを読み込みます。

## エントリーポイントについて

一般的なSvelteプロジェクトでは、`main.ts` のようなファイルで `new App({ target: document.body })` のように、コンポーネントをDOMにマウントする「エントリーポイント」の記述が必要です。

しかし、このリポジトリでは、その役割をバックエンドのビューテンプレートである `@app/views/screen.scala.html` が担っています。このファイルが、SvelteコンポーネントをマウントするためのDOM要素（`<div id="screen">`）を用意し、適切なJavaScriptファイルを読み込んでくれます。

そのため、**各画面のSvelteコンポーネントを作成する際に、エントリーポイントやマウント処理について意識する必要はありません**。`Page.svelte` の中に、ページのコンテンツそのものを記述することに集中できます。

---

これで、Svelteコンポーネントの作成は完了です。
しかし、この時点ではまだブラウザからこのページにアクセスすることはできません。次のステップで、このコンポーネントを表示するためのバックエンド（Play Controllerとルーティング）を作成します。

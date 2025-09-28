# 9. コラム: BootstrapとSveltestrap

このアプリケーションのUIコンポーネント（ボタン、フォーム、テーブルなど）は、[Bootstrap](https://getbootstrap.com/) というCSSフレームワークをベースにしています。そして、SvelteからBootstrapを容易に利用するために [Sveltestrap](https://sveltestrap.github.io/) というライブラリを使用しています。

## Bootstrapとは

Bootstrapは、WebサイトやWebアプリケーションを素早く構築するために、予めデザインされたUIコンポーネントの集合を提供する、世界で最も人気のあるCSSフレームワークの一つです。

Bootstrapを利用する主なメリットは以下の通りです。

- **デザインの統一感**: プロジェクト全体で一貫性のあるデザインを容易に実現できます。
- **レスポンシブデザイン**: グリッドシステムやヘルパークラスを使うことで、PC、タブレット、スマートフォンなど、さまざまな画面サイズに対応したレイアウトを簡単に構築できます。
- **開発の効率化**: `class="btn btn-primary"` のように、HTMLのクラスを指定するだけで、見栄えの良いボタンをすぐに作成できます。これにより、CSSをゼロから書く手間が省け、開発スピードが向上します。

## Sveltestrapとは

Sveltestrapは、Bootstrap 5のコンポーネントをSvelteで使いやすいようにラップしたライブラリです。

Bootstrapのコンポーネントの中には、ドロップダウンメニューやモーダルダイアログのように、JavaScriptによるインタラクティブな動作が必要なものがあります。
Sveltestrapは、これらの動作をSvelteの流儀で（`import` してコンポーネントとして）使えるようにしてくれます。

例えば、Sveltestrapを使ってボタンを作成する場合、以下のように記述します。

```svelte
<script lang="ts">
  import { Button } from "sveltestrap";
</script>

<Button color="primary">Primary Button</Button>
```

これは、HTMLで以下のように記述するのと同じ結果になります。

```html
<button type="button" class="btn btn-primary">Primary Button</button>
```

Sveltestrapを使うことで、HTMLのクラス名を直接書く代わりに、SvelteコンポーネントとしてUI部品を宣言的に記述できます。これにより、コードの可読性が向上し、propsを使って動的にコンポーネントの見た目や動作を制御しやすくなるというメリットがあります。

このプロジェクトの `app/assets/svelte/components/bs` ディレクトリには、Sveltestrapのコンポーネントをさらにラップした、このプロジェクト固有のUIコンポーネントがいくつか配置されています。

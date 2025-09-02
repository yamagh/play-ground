# コラム: BootstrapによるUIデザイン

このチュートリアルでは、UIのスタイリングに**Bootstrap**というCSSフレームワークを利用します。
Bootstrapは、Webサイトやアプリケーションを素早く開発するために、あらかじめデザインされたUIコンポーネント（ボタン、フォーム、ナビゲーションバーなど）の集合体です。

## Bootstrapの利点

- **迅速な開発:** `class="btn btn-primary"` のように、HTMLの要素に所定のクラスを指定するだけで、見栄えの良いデザインを適用できます。自分でCSSを細かく書く必要がないため、開発スピードが向上します。
- **レスポンシブデザイン:** Bootstrapは「モバイルファースト」で設計されており、グリッドシステムを使うことで、PC、タブレット、スマートフォンなど、さまざまな画面サイズに対応したレイアウトを簡単に構築できます。
- **一貫性のあるUI:** フレームワークが提供するコンポーネントを使うことで、アプリケーション全体で統一感のあるデザインを保つのが容易になります。
- **豊富なドキュメント:** 公式サイトに全てのコンポーネントの使い方やカスタマイズ方法が詳しく記載されており、学習しやすいのも特徴です。

## このリポジトリでの導入方法

このリポジトリでは、BootstrapをCDN (Content Delivery Network) を通じて読み込んでいます。
具体的には、`app/views/screen.scala.html` という全Svelteページ共通のテンプレートファイルに、以下の行が追加されています。

```html
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" ...>
```

これにより、アプリケーションの全てのページでBootstrapのCSSクラスが利用可能になります。
Svelteコンポーネントを作成する際は、特別な設定なしにBootstrapのクラスをHTML要素に適用するだけで、スタイルが反映されます。

例えば、青いボタンを作成したい場合は以下のように記述します。

```svelte
<button class="btn btn-primary">Click me</button>
```

チュートリアルの各章では、Bootstrapのクラスを使ってUIを構築していきます。どのようなクラスが利用できるか興味がある方は、ぜひ[Bootstrapの公式ドキュメント](https://getbootstrap.com/docs/5.3/getting-started/introduction/)も参照してみてください。

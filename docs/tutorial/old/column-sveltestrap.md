# コラム: SveltestrapによるUIデザイン

このリポジトリでは、UIコンポーネントの実装に **Sveltestrap** を積極的に利用します。
Sveltestrapは、人気のCSSフレームワークであるBootstrap 5のコンポーネントを、Svelteアプリケーションで簡単に利用できるようにしたライブラリです。

## Sveltestrapの利点

### 1. 宣言的なコンポーネント
BootstrapのCSSクラスをHTMLに直接書く代わりに、SvelteのコンポーネントとしてUI部品を宣言的に記述できます。

**Bootstrapの場合:**
```html
<div class="alert alert-primary" role="alert">
  A simple primary alert—check it out!
</div>
```

**Sveltestrapの場合:**
```svelte
<script>
  import { Alert } from 'sveltestrap';
</script>

<Alert color="primary">
  A simple primary alert—check it out!
</Alert>
```
このように、よりSvelteらしい書き方でコードの可読性が向上します。

### 2.インタラクティブな機能との連携
モーダルダイアログやドロップダウン、タブなど、JavaScriptによるインタラクションが必要なコンポーネントも、Sveltestrapを使えば状態管理が容易になります。`isOpen` のようなプロパティをSvelteの状態で管理するだけで、複雑なDOM操作を自分で行う必要はありません。

### 3. Bootstrapとの互換性
SveltestrapはBootstrap 5のラッパーであるため、Bootstrapが提供する豊富なデザインやユーティリティクラス（`mt-4`, `d-flex` など）とシームレスに併用できます。Sveltestrapにラッパーコンポーネントが用意されていない場合や、細かいレイアウト調整を行いたい場合は、Bootstrapのクラスを直接使うことができます。

## このリポジトリでの方針

このリポジトリでは、以下の開発方針を採用しています。

1.  **まずSveltestrapの利用を検討する:** UIコンポーネントを実装する際は、まずSveltestrapに該当するコンポーネントがあるかを確認し、積極的に利用します。
2.  **Sveltestrapに無いものはBootstrapで補う:** Sveltestrapに対応するコンポーネントが無い場合や、より細かいカスタマイズが必要な場合は、BootstrapのCSSクラスを直接HTML要素に適用して実装します。

このアプローチにより、Svelteの書きやすさとBootstrapの堅牢なデザインシステムの両方の利点を享受し、開発効率とメンテナンス性を高めています。

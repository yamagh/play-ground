# 2-2. UIデザインとBootstrap/Sveltestrap

Svelteコンポーネントの骨格ができたので、次はこの画面の見た目を整えていきましょう。このプロジェクトでは、UIデザインの効率化と統一性のために、CSSフレームワークの「Bootstrap」と、それをSvelteで使いやすくするためのライブラリ「Sveltestrap」を導入しています。

## Bootstrapとは

[Bootstrap](https://getbootstrap.com/) は、WebサイトやWebアプリケーションのUIを素早く構築するために広く使われているCSSフレームワークです。

あらかじめデザインされたボタン、フォーム、ナビゲーションバーなどのコンポーネントや、グリッドシステム（レイアウトを整えるための仕組み）が用意されています。これらを利用することで、自分でCSSをゼロから書かなくても、モダンで見栄えの良いデザインを効率的に作成できます。

Bootstrapを利用する主なメリットは以下の通りです。
- **開発効率の向上**: 用意されたCSSクラスをHTMLに適用するだけで、デザインが整います。
- **デザインの統一性**: プロジェクト全体で一貫したデザインを保ちやすくなります。
- **レスポンシブデザイン**: スマートフォンやタブレットなど、さまざまな画面サイズに自動で対応するための仕組みが備わっています。

## Sveltestrapとは

[Sveltestrap](https://sveltestrap.github.io/) は、Bootstrapの各コンポーネント（ボタン、モーダル、カードなど）をSvelteコンポーネントとしてカプセル化したライブラリです。

BootstrapのCSSクラスを直接HTMLに書くこともできますが、Sveltestrapを使うとよりSvelteらしい、宣言的で直感的な書き方ができます。

例えば、Bootstrapでボタンを作るには以下のように書きます。
```html
<button type="button" class="btn btn-primary">Primary</button>
```

Sveltestrapを使うと、以下のようにSvelteコンポーネントとして記述できます。
```svelte
<script>
  import { Button } from 'sveltestrap';
</script>

<Button color="primary">Primary</Button>
```
インタラクティブな機能（モーダルの開閉など）もSvelteの状態管理とスムーズに連携できるため、非常に便利です。

## コンポーネントへの適用

それでは、先ほど作成した `Page.svelte` にSveltestrapとBootstrapを適用してみましょう。

`app/assets/svelte/screens/tasks/Page.svelte` を以下のように編集してください。

**`app/assets/svelte/screens/tasks/Page.svelte`**
```svelte
<script lang="ts">
  import { Button, Card, CardBody, CardHeader, Col, Row } from 'sveltestrap';
</script>

<div class="container mt-4">
  <Row>
    <Col>
      <Card>
        <CardHeader class="d-flex justify-content-between align-items-center">
          <h5 class="mb-0">タスク一覧</h5>
          <Button color="primary">
            <i class="bi-plus-lg" />
            新規登録
          </Button>
        </CardHeader>
        <CardBody>
          <p>ここにタスクの一覧が表示されます。</p>
        </CardBody>
      </Card>
    </Col>
  </Row>
</div>

<style>
  /* このコンポーネント専用のCSSはここに記述します */
</style>
```

### 変更点の解説

- **`import { ... } from 'sveltestrap';`**:
  Sveltestrapから `Button`, `Card`, `Row` などのレイアウト用コンポーネントをインポートしています。
- **`<Card>` と `<CardHeader>`**:
  コンテンツを枠で囲み、ヘッダー部分とボディ部分を分けるためのコンポーネントです。見出しと新規登録ボタンをヘッダーに配置しています。
- **`class="d-flex justify-content-between ..."`**:
  これらはBootstrapが提供するユーティリティクラスです。
  - `d-flex`: Flexboxレイアウトを有効にします。
  - `justify-content-between`: Flexboxアイテム（この場合は見出しとボタン）を両端に配置します。
  - `align-items-center`: アイテムを垂直方向の中央に揃えます。
- **`<i class="bi-plus-lg" />`**:
  [Bootstrap Icons](https://icons.getbootstrap.com/) のクラスです。このプロジェクトにはBootstrap Iconsも導入済みのため、`<i>` タグにクラスを指定するだけでアイコンを表示できます。

---

これで、ただのテキストだった画面が、カード形式のレイアウトと見栄えの良いボタンを持つUIに変わりました。

次の章では、いよいよこのSvelteコンポーネントをブラウザに表示させるため、バックエンド側（Play Framework）のControllerとRouteを作成します。

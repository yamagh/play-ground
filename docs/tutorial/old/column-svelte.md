# コラム: Svelteとは

Svelteは、ユーザーインターフェースを構築するための先進的なJavaScriptフレームワークです。ReactやVue.jsのような従来のフレームワークとは異なるユニークなアプローチを採用しています。

## Svelteの主な特徴

### 1. "コンパイラ"であること
Svelteの最大の特徴は、自身を「フレームワーク」ではなく「コンパイラ」と位置づけている点です。

ReactやVueでは、アプリケーションの実行時にライブラリのコード（仮想DOMなど）がブラウザ上で動作し、UIの状態を管理します。一方、Svelteは**ビルド時**にコンポーネントのコードを最適化された純粋なJavaScriptにコンパイルします。

このアプローチにより、以下のようなメリットが生まれます。

- **No Virtual DOM:** 仮想DOMの差分検出といったオーバーヘッドがなく、非常に高速に動作します。
- **Truly Reactive:** Svelte 5では `$state()` というRuneを使って状態を宣言します。`let count = $state(0);` のように宣言した変数を更新するだけで、DOMが自動的に更新されます。
- **小さなバンドルサイズ:** フレームワークのランタイムコードが含まれないため、最終的に生成されるJavaScriptファイルが非常に小さくなります。

### 2. 書きやすさ
Svelteのコンポーネントは、HTML、CSS、JavaScriptというWebの標準技術に近い形で記述できます。

```svelte
<script>
  let name = $state('world');
</script>

<style>
  h1 {
    color: purple;
  }
</style>

<h1>Hello {name}!</h1>
```
このように、コンポーネントの構造、スタイル、ロジックが `.svelte` という単一のファイルにまとまっており、直感的で学習コストが低いのが特徴です。スタイルはデフォルトでそのコンポーネント内にスコープが限定されるため、意図しないスタイル崩れを防ぎます。

### 3. リアクティビティ
Svelte 5のリアクティビティは**Runes**という仕組みに基づいています。

```svelte
<script>
  let count = $state(0);

  function handleClick() {
    count += 1;
  }
</script>

<button on:click={handleClick}>
  Clicked {count} {count === 1 ? 'time' : 'times'}
</button>
```
`$state()` で作成された変数 `count` の値を変更するだけで、Svelteコンパイラがその変更を検知し、DOMの更新が必要な箇所をピンポイントで書き換えるコードを生成します。

このチュートリアルでは、Svelteを使ってインタラクティブなUIを効率的に構築する方法を学んでいきます。

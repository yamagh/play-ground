# 30. 付録: Svelte Runesを使ったデバッグ

Svelte 5で導入された**Runes**は、Svelteのリアクティビティ（反応性）をより細かく、そして明示的に制御するための新しい仕組みです。
このチュートリアルの一部のコードでも `$state` が登場しましたが、Runesは特に、値がいつ、どのように変化しているかを追跡するデバッグ作業において非常に役立ちます。

## Runesの基本: `$state` と `$effect`

- **`$state`**: リアクティブな状態を宣言するためのRuneです。`let count = $state(0);` のように使います。この変数の値が変更されると、Svelteはそれを使用しているUIを自動的に更新します。これは、従来の `let count = 0;` という記法と似ていますが、より明示的に「リアクティブな状態」であることを示します。

- **`$effect`**: 副作用（side effect）を定義するためのRuneです。`$effect` で囲まれたコードブロックは、その中で使用されているリアクティブな値（`$state` など）が変更されるたびに自動的に再実行されます。

## `$effect` を使ったデバッグ手法

`$effect` のこの「値の変更を検知してコードを実行する」という性質は、デバッグに非常に便利です。
`console.log` と組み合わせることで、特定の状態がいつ、どのように変化したかをコンソールに出力して追跡できます。

### 例1: 特定の変数を監視する

フォームの入力値を保持している `task` オブジェクトが、いつ更新されているかを確認したい場合。

```svelte
<script lang="ts">
  import type { Task } from "@app/models/Task";

  let task = $state<Task>({ subject: "", ... });

  // taskオブジェクトが変更されるたびに、その内容をコンソールに出力
  $effect(() => {
    console.log("Task object changed:", task);
  });
</script>
```
このように記述しておくと、ユーザーがフォームのいずれかの入力欄に一文字入力するたびに、更新された `task` オブジェクト全体がブラウザの開発者コンソールに出力されます。
これにより、「`bind:value` が正しく機能しているか」「どのタイミングで値がセットされているか」といったことを簡単に確認できます。

### 例2: propsの変更を監視する

親コンポーネントから渡された `props` が、意図したタイミングで変更されているかを確認したい場合。

```svelte
<script lang="ts">
  const { currentPage, totalItems } = $props<{ currentPage: number, totalItems: number }>();

  // currentPageまたはtotalItemsの値が変更されるたびに実行
  $effect(() => {
    console.log(`Props changed: currentPage=${currentPage}, totalItems=${totalItems}`);
  });
</script>
```
`$props` もRunesの一部で、コンポーネントのプロパティをリアクティブな値として受け取ります。
`$effect` の中でこれらの `props` を使用することで、親コンポーネントからのデータフローを監視できます。

### `onMount` との違い

`onMount` は、コンポーネントが**最初にマウントされたときに一度だけ**実行されます。
一方、`$effect` は、**依存するリアクティブな値が変更されるたびに**実行されます。

- **初期化処理**: `onMount` を使う（例: APIからの初回データ取得）
- **値の変更に応じた処理**: `$effect` を使う（例: デバッグログ、特定の変数が変わったときの再計算など）

このように `$effect` を活用することで、Svelteアプリケーションのリアクティブなデータの流れを可視化し、デバッグの効率を大幅に向上させることができます。
問題が発生した際には、怪しいと思われる状態変数を `$effect` と `console.log` で監視してみるのが良い第一歩です。

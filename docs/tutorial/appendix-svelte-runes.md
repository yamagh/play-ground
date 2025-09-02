# A-1. Svelte Runesを使ったデバッグ

Svelte 5で導入された**Runes**は、コンポーネントのリアクティビティをより明示的かつ柔軟に制御するための新しい仕組みです。
チュートリアルの本編では、Svelte 5で導入された**Runes**の一部（`$state`や`$effect`）を使用してきました。ここでは、デバッグに特に役立つRuneをいくつか追加で紹介します。

> **Note:**
> RunesはSvelte 5の主要機能です。

## `$log()` - リアクティブな `console.log`

開発中、変数の値がいつ、どのように変化するのかを確認するために `console.log` を多用します。
しかし、Svelteの `<script>` ブロックのトップレベルに書いた `console.log` は、コンポーネントの初期化時に一度しか実行されません。

```svelte
<script>
  let count = $state(0);
  console.log('count is', count); // 最初に一度 'count is 0' と表示されるだけ

  function increment() {
    count += 1;
    // 値が変わるたびにログを見たいなら、イベントハンドラ内などに書く必要がある
    console.log('count is now', count);
  }
</script>
```

これでは、どのリアクティブな変更がいつトリガーされたのかを追うのが面倒です。

そこで登場するのが `$log()` Runeです。

```svelte
<script>
  import { $log } from 'svelte/reactivity';

  let count = $state(0);

  // count の値が変化するたびに、その新しい値がコンソールに出力される
  $log(count);

  function increment() {
    count += 1;
  }
</script>

<button on:click={increment}>
  count: {count}
</button>
```

`$log(count)` と記述しておくだけで、`count` の値が変更されるたびに自動的にコンソールにログが出力されます。
複数の変数を監視することも可能です。

```javascript
$log(user, cart, products);
```

これにより、どのイベントがどの状態の変更を引き起こしたのかを時系列で簡単に追跡でき、デバッグ効率が大幅に向上します。

## `$inspect()` - より詳細なデバッグ情報

`$inspect()` は `$log()` と似ていますが、より多くの情報を提供します。
コンポーネントがいつ、なぜ再描画されたのか、どの値が変更されたのかといった内部的な動作を追跡したい場合に便利です。

```svelte
<script>
  import { $inspect } from 'svelte/reactivity';

  let count = $state(0);

  // count の読み書きを監視
  $inspect(count);

  // ラベルを付けて監視
  $inspect('My Counter', count);
</script>
```

`$inspect()` を使うと、ブラウザの開発者ツールでプログラムの実行を一時停止（ブレーク）させ、その時点での変数の状態やコールスタックを詳細に調査することができます。

- **値の読み取り (Read):** テンプレートで `{count}` のように値が表示されるときなど。
- **値の書き込み (Write):** `count += 1` のように値が変更されたとき。
- **更新の通知 (Signal):** 値の変更がDOMの更新をトリガーしたとき。

これらのタイミングでデバッガーが停止するため、リアクティブな更新が連鎖するような複雑なケースで、意図しない再描画の原因を突き止めるのに非常に役立ちます。

Runesはまだ新しい機能ですが、特に `$log` は日々の開発ですぐに役立つ便利なツールなので、覚えておくと良いでしょう。

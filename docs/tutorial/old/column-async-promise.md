# コラム: 非同期処理とPromise

SvelteコンポーネントからAPIを呼び出す際に `async/await` という構文を使いました。これは、JavaScriptにおける**非同期処理**を扱うための仕組みです。

## 同期処理 vs 非同期処理

- **同期処理 (Synchronous)**
  - 処理が上から下に順番に実行されます。
  - 一つの処理が終わるまで、次の処理はブロック（待機）されます。
  - 例: `let result = 1 + 2; console.log(result);`

- **非同期処理 (Asynchronous)**
  - 時間のかかる処理（例: APIリクエスト、ファイルの読み込み、タイマー）を待っている間に、他の処理を進めることができる仕組みです。
  - 処理の完了を待たずに次の行に進み、完了したら後で結果を処理します。

もしAPIリクエストが同期処理だと、サーバーからデータが返ってくるまでの数秒間、ブラウザの画面が固まってしまい、ユーザーは何も操作できなくなってしまいます。これを避けるために、JavaScriptのI/O処理は基本的に非同期で行われます。

## Promise

非同期処理の結果を表現するためのオブジェクトが **Promise** です。
`fetch('/api/news')` を実行すると、それは即座に `Promise` オブジェクトを返します。この`Promise`は、その時点ではまだリクエストの結果（成功か失敗か）を持っていません。

`Promise` は以下の3つのいずれかの状態を持ちます。
- **pending:** 初期状態。まだ結果は出ていない。
- **fulfilled (resolved):** 処理が成功した状態。結果の値を持つ。
- **rejected:** 処理が失敗した状態。エラー情報を持つ。

### 従来の書き方 (`.then()`)
Promiseが導入された当初は、`.then()` や `.catch()` といったメソッドを使って、処理が完了した後のコールバック関数を登録していました。

```javascript
fetch('/api/news')
  .then(response => {
    if (response.ok) {
      return response.json(); // これもPromiseを返す
    } else {
      throw new Error('Failed to fetch');
    }
  })
  .then(data => {
    // 成功時の処理
    console.log(data);
  })
  .catch(error => {
    // 失敗時の処理
    console.error(error);
  });
```
このように、処理が数珠つなぎになり、ネストが深くなると「コールバック地獄」と呼ばれる読みにくいコードになりがちでした。

## `async/await`

`async/await` は、このPromiseをベースとした非同期処理を、まるで同期処理のように直感的に書けるようにするための**シンタックスシュガー**（糖衣構文）です。

```javascript
// 関数宣言の前に async を付ける
async function fetchNews() {
  try {
    // Promiseが完了するまで待つ (await)
    const response = await fetch('/api/news');

    if (response.ok) {
      // response.json() もPromiseを返すので await する
      const data = await response.json();
      console.log(data);
    } else {
      console.error('Failed to fetch');
    }
  } catch (error) {
    // Promiseがrejectedになった場合、ここでキャッチされる
    console.error(error);
  }
}
```

- **`async`**: 関数名の前に付けると、その関数が内部で `await` を使うことを示し、また、その関数自体が常に `Promise` を返すようになります。
- **`await`**: `Promise` を返す処理（例: `fetch()`）の前に付けます。`await` は、そのPromiseが `fulfilled` または `rejected` になるまで関数の実行を一時停止し、`fulfilled` になったらその結果の値を返します。

`async/await` を使うことで、非同期処理のフローが上から下への自然な流れで読めるようになり、コードの可読性と保守性が劇的に向上します。
`onMount` のコールバック関数を `async` にしたのも、この `await` を使うためです。

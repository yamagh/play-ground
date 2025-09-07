# 4-3. Svelteで更新フォームを作成する

前の章で作成したお知らせ追加API (`POST /api/news`) を呼び出すためのUIを、Svelteで作成します。
お知らせ一覧ページに、新しいお知らせを投稿するための簡単なフォームを追加します。

## `Page.svelte` の修正

`app/assets/svelte/screens/news/Page.svelte` を開き、フォームの追加と、フォームを送信するためのロジックを追記します。

**`app/assets/svelte/screens/news/Page.svelte`**

```svelte
<script lang="ts">
  import { $effect } from "svelte/reactivity";
  import LayoutSideMenu from "../../layouts/LayoutSideMenu.svelte";
  import PageContainer from "../../layouts/PageContainer.svelte";
  import type { MenuItem } from "../../layouts/MenuItem";

  type NewsItem = {
    id: number; // IDも受け取るように変更
    newsDate: string; // APIのモデルに合わせてキャメルケースに
    title: string;
  };

  let newsItems: NewsItem[] = $state([]);

  // フォームの入力値を保持する変数
  let newTitle = $state("");
  let newDate = $state(new Date().toISOString().split("T")[0]); // 今日の日付を YYYY-MM-DD 形式で

  const menuItems: MenuItem[] = [
    {
      id: "tasks",
      label: "Tasks",
      link: "/tasks",
      icon: "bi-check2-square",
    },
    {
      id: "news",
      label: "News",
      link: "/news",
      icon: "bi-newspaper",
      active: true,
    },
  ];

  // お知らせ一覧を取得する関数
  async function fetchNews() {
    try {
      const response = await fetch("/api/news");
      if (response.ok) {
        newsItems = await response.json();
      } else {
        console.error("Failed to fetch news data.");
      }
    } catch (error) {
      console.error("Error fetching news data:", error);
    }
  }

  // フォーム送信時の処理
  async function handleSubmit() {
    if (!newTitle || !newDate) {
      alert("日付とタイトルを入力してください。");
      return;
    }

    try {
      const response = await fetch("/api/news", {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          title: newTitle,
          newsDate: newDate,
        }),
      });

      if (response.ok) {
        // フォームをリセット
        newTitle = "";
        // 再度一覧を読み込む
        await fetchNews();
      } else {
        alert("お知らせの追加に失敗しました。");
      }
    } catch (error) {
      console.error("Error submitting news:", error);
      alert("お知らせの追加中にエラーが発生しました。");
    }
  }

  // $effectで初回データを読み込む
  $effect(fetchNews);
</script>

<LayoutSideMenu title="News" {menuItems}>
  <PageContainer title="お知らせ一覧">
    <!-- 追加フォーム -->
    <div class="card mb-4">
      <div class="card-body">
        <h5 class="card-title">新規追加</h5>
        <form on:submit|preventDefault={handleSubmit}>
          <div class="row g-3 align-items-center">
            <div class="col-auto">
              <input
                type="date"
                class="form-control"
                bind:value={newDate}
              />
            </div>
            <div class="col">
              <input
                type="text"
                class="form-control"
                placeholder="タイトル"
                bind:value={newTitle}
              />
            </div>
            <div class="col-auto">
              <button type="submit" class="btn btn-primary">
                追加
              </button>
            </div>
          </div>
        </form>
      </div>
    </div>

    <!-- 一覧表示 -->
    <div class="card">
      <div class="card-body">
        <ul class="list-group list-group-flush">
          {#each newsItems as item}
            <li class="list-group-item">
              <p class="text-muted small">{item.newsDate}</p>
              <p class="fw-bold">{item.title}</p>
            </li>
          {/each}
        </ul>
      </div>
    </div>
  </PageContainer>
</LayoutSideMenu>

<style>
  /* スタイルは空のままでOKです */
</style>
```

### コードの解説

1.  **フォーム入力用の変数**:
    - `$state("")` と `$state(...)` を使い、フォームの`<input>`要素の値を保持するためのリアクティブな状態変数を定義します。

2.  **`fetchNews()` 関数**:
    - データ取得ロジックを、再利用可能な `fetchNews` 関数として独立させました。

3.  **`handleSubmit()` 関数**:
    - フォームが送信されたときに実行される非同期関数です。
    - 簡単なバリデーション（入力チェック）を行っています。
    - `fetch("/api/news", { ... })` で `POST` リクエストを送信します。
      - `method: "POST"`: HTTPメソッドを指定します。
      - `headers: { "Content-Type": "application/json" }`: リクエストボディがJSON形式であることをサーバーに伝えます。
      - `body: JSON.stringify({ ... })`: `newTitle` と `newDate` の値を持つJavaScriptオブジェクトを、JSON文字列に変換してボディに設定します。
    - お知らせの追加に成功したら、フォームの入力値をリセットし、`fetchNews()` を再度呼び出して画面のリストを最新の状態に更新します。

4.  **テンプレートの変更**:
    - **フォームの追加**:
      - `<form on:submit|preventDefault={handleSubmit}>`:
        - `on:submit` はフォームの送信イベントを捕捉します。
        - `|preventDefault` は修飾子で、フォーム送信時のデフォルトの動作（ページリロード）を抑制します。
        - `{handleSubmit}` で、イベントが発生したときに `handleSubmit` 関数を呼び出すよう紐付けています。
      - `<input bind:value={newDate} />`:
        - `bind:value` はSvelteの**双方向バインディング**の構文です。
        - これにより、`<input>` の値が変更されると `newDate` 変数の値が自動的に更新され、逆に `newDate` 変数の値がコードで変更されると `<input>` の表示も更新されます。`newTitle` も同様です。Svelte 5の`$state`とも問題なく連携します。
    - **一覧表示の修正**:
      - `item.date` を `item.newsDate` に変更し、JavaのModelクラスのフィールド名 (`newsDate`) と一致させています。

## 動作確認

ファイルを保存し、ブラウザで [http://localhost:9000/news](http://localhost:9000/news) を確認してください。

1.  お知らせ一覧の上に、日付とタイトルの入力フォーム、追加ボタンが表示されていることを確認します。
2.  日付とタイトルを入力し、「追加」ボタンをクリックします。
3.  リクエストが成功すると、フォームのタイトル入力欄が空になり、下の一覧に今追加したお知らせが（ページをリロードすることなく）表示されることを確認します。
4.  H2コンソールを開き、`SELECT * FROM news;` を実行して、実際にデータベースにレコードが追加されていることも確認してみましょう。

---

これで、データの参照（Read）と作成（Create）をWebアプリケーションのUIから一通り行えるようになりました。
このチュートリアルで扱う主な機能開発はここまでです。次の章では、全体のまとめと、今後の学習に向けたステップについて解説します。

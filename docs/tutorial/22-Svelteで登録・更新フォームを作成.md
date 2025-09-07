# 22. Svelteで登録・更新フォームを作成

バックエンドに登録・更新APIができたので、フロントエンドにそれを利用するためのフォーム画面を作成します。
ここでは、`task-edit` 画面 (`app/assets/svelte/screens/task-edit/`) を例に、新規登録と編集の両方に対応したフォームの実装方法を学びます。

## 1. 新規登録と編集のモード切り替え

`task-edit` 画面は、URLによって新規登録モードと編集モードを切り替えます。

- `/tasks/new`: 新規登録モード。空のフォームを表示します。
- `/tasks/edit/:id`: 編集モード。URLに含まれる `:id` を元に、既存のタスクデータをAPIから取得し、フォームに初期表示します。

このモード判定は、`Page.svelte` で行われます。Svelteでは、ページのURL情報を `page` ストアから取得できます。

**`app/assets/svelte/screens/task-edit/Page.svelte`**:
```svelte
<script lang="ts">
  import { page } from "$app/stores";
  import { onMount } from "svelte";
  import { fetchTask, createTask, updateTask } from "./api";
  import type { Task } from "@app/models/Task";

  // URLのパスパラメータからIDを取得
  const id = $page.params.id;
  // IDが存在するかどうかで編集モードかどうかを判定
  const isEditMode = !!id;

  let task: Task = $state({ subject: "", ... }); // フォームのデータを保持するオブジェクト

  // 編集モードの場合、コンポーネントのマウント時に既存データを取得
  onMount(async () => {
    if (isEditMode) {
      const fetchedTask = await fetchTask(id);
      if (fetchedTask) {
        task = fetchedTask;
      }
    }
  });

  // ...
</script>
```
- `import { page } from "$app/stores"`: SvelteKit（このプロジェクトではPlay Frameworkが同様の機能を提供）の機能で、現在のページに関する情報（URL、パラメータなど）を持つストアをインポートします。
- `const id = $page.params.id`: `page` ストアの `params` プロパティから、URLの `:id` 部分（例: `/tasks/edit/123` なら `"123"`）を取得します。
- `onMount`: 編集モード (`isEditMode` が `true`) の場合のみ、`fetchTask` APIを呼び出して既存のタスクデータを取得し、フォームデータ用の `task` 変数にセットします。

## 2. フォームと状態の双方向バインディング

Svelteの `bind:value` ディレクティブを使うと、フォームの入力要素と変数を非常に簡単に「双方向バインディング」できます。

```svelte
<!-- ... -->
<form on:submit|preventDefault={handleSubmit}>
  <div>
    <label for="subject">Subject</label>
    <input type="text" id="subject" bind:value={task.subject} />
  </div>
  <!-- ... 他のフォーム要素 ... -->
  <button type="submit">Save</button>
</form>
```
- `bind:value={task.subject}`:
  - `task.subject` の値が変更されると、`<input>` の表示が自動的に更新されます。
  - ユーザーが `<input>` にテキストを入力すると、`task.subject` の値が自動的に更新されます。
このように、`bind:value` を使うことで、UIの入力とコンポーネントの状態が常に同期します。

## 3. フォームの送信処理

フォームが送信されたときの処理を `handleSubmit` 関数として実装します。
編集モードかどうかによって、呼び出すAPI (`createTask` または `updateTask`) を切り替えます。

```svelte
<script lang="ts">
  // ...
  import { goto } from "$app/navigation";

  const handleSubmit = async () => {
    try {
      if (isEditMode) {
        // 更新APIを呼び出し
        await updateTask(id, task);
      } else {
        // 登録APIを呼び出し
        await createTask(task);
      }
      // 成功したら一覧画面に遷移
      goto("/tasks");
    } catch (error) {
      // エラー処理 (詳細は後の章で解説)
      console.error("Failed to save task:", error);
    }
  };
</script>
```
- `on:submit|preventDefault={handleSubmit}`:
  フォームの `submit` イベントが発生したときに `handleSubmit` 関数を呼び出します。`|preventDefault` は、フォーム送信時のデフォルトの動作（ページのリロード）を抑制するための修飾子です。
- `updateTask(id, task)` / `createTask(task)`:
  `api.ts` に実装されたAPI呼び出し関数です。これらは、内部で `@app/assets/svelte/utils/api.ts` の `put` および `post` ヘルパー関数を呼び出しています。これらのヘルパー関数は、第2引数に渡されたオブジェクトを自動的にJSON文字列に変換し、リクエストボディとして送信します。
- `goto("/tasks")`:
  保存が成功したら、SvelteKitの `goto` 関数を使って、プログラム的にタスク一覧画面 (`/tasks`) に遷移させています。

これで、URLに応じて新規登録・編集フォームを動的に表示し、APIと連携してデータの永続化を行う画面が完成しました。

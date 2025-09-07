# 26. Svelteから削除機能を呼び出す

バックエンドに削除APIができたので、フロントエンドからこのAPIを呼び出す機能を実装します。
一般的に、削除のような破壊的な操作を行う前には、ユーザーに「本当に削除しますか？」と確認を求めるのが親切な設計です。

ここでは、タスク一覧画面 (`tasks/Page.svelte`) に削除ボタンを追加し、確認ダイアログを経てAPIを呼び出す流れを実装します。

## 1. API呼び出し関数の作成

まず、`screens/tasks/api.ts` に、`DELETE /api/tasks/:id` を呼び出すための関数を追加します。

**`app/assets/svelte/screens/tasks/api.ts`**:
```typescript
import { del } from "@app/assets/svelte/utils/api"; // "delete"は予約語なので"del"を使う
// ...

/**
 * タスクを削除するAPIを呼び出します。
 * @param id 削除するタスクのID
 */
export const deleteTask = async (id: number): Promise<void> => {
  await del(`/api/tasks/${id}`);
};
```
- `import { del } from ...`: `utils/api.ts` にある `DELETE` リクエスト用のヘルパー関数 `del` をインポートします。（JavaScriptでは `delete` は予約語のため、`del` という名前になっています。）
- `del(`/api/tasks/${id}`)`: 指定されたIDを持つタスクの削除APIエンドポイントに対して、`DELETE` リクエストを送信します。

## 2. UIへの削除ボタンの追加とイベントハンドリング

次に、`tasks/Page.svelte` の一覧テーブルの各行に削除ボタンを追加し、クリックされたときの処理を実装します。

**`app/assets/svelte/screens/tasks/Page.svelte`**:
```svelte
<script lang="ts">
  import { deleteTask } from "./api";
  import { safeRun } from "@app/assets/svelte/utils/safeRun";
  import { showSuccess } from "@app/assets/svelte/stores/message";
  // ...

  const handleDelete = async (id: number, subject: string) => {
    // 確認ダイアログを表示
    if (!window.confirm(`Are you sure you want to delete "${subject}"?`)) {
      return; // キャンセルされた場合は何もしない
    }

    await safeRun(async () => {
      // 削除APIを呼び出し
      await deleteTask(id);
      showSuccess("Task deleted successfully.");
      // データを再読み込みして一覧を更新
      await loadTasks();
    });
  };
</script>

<!-- ... -->
<table>
  <tbody>
    {#each tasksResponse.list as task}
      <tr>
        <!-- ... -->
        <td>
          <button
            class="btn btn-danger btn-sm"
            on:click={() => handleDelete(task.id, task.subject)}
          >
            Delete
          </button>
        </td>
      </tr>
    {/each}
  </tbody>
</table>
```

### コードの解説
- `handleDelete(id, subject)`:
  削除ボタンがクリックされたときに実行される関数です。どのタスクを削除するかを特定するための `id` と、確認メッセージにタスクの件名を表示するための `subject` を引数で受け取ります。
- `window.confirm(...)`:
  ブラウザに組み込まれている関数で、OK/キャンセルの確認ダイアログを表示します。ユーザーがOKをクリックすると `true` を、キャンセルをクリックすると `false` を返します。
- `if (!window.confirm(...)) { return; }`:
  ユーザーがキャンセルを選択した場合は、`handleDelete` 関数の処理を中断します。
- `await deleteTask(id)`:
  ユーザーがOKを選択した場合、`deleteTask` APIを呼び出します。この処理は `safeRun` でラップされており、エラーは自動的に処理されます。
- `await loadTasks()`:
  削除が成功した後、`loadTasks()` 関数を再度呼び出すことで、サーバーから最新のタスク一覧を再取得し、画面の表示を更新しています。
- `on:click={() => handleDelete(task.id, task.subject)}`:
  `{#each}` ブロックの中でイベントハンドラに関数を渡す場合、アロー関数 `() => ...` を使ってラップするのが一般的です。これにより、クリックされた行の `task.id` と `task.subject` を `handleDelete` 関数に正しく渡すことができます。

## 補足: フォーム内のボタンと `event.preventDefault()`

もし、削除ボタンが `<form>` タグの内側に配置されている場合、ボタンをクリックするとフォーム全体の送信（submit）がトリガーされてしまうことがあります。

これを防ぐには、いくつかの方法があります。

1.  **`type="button"` を指定する**:
    `<button type="button" ...>` のように指定すると、そのボタンはフォームを送信しなくなります。

2.  **イベント修飾子 `preventDefault` を使う**:
    `<button on:click|preventDefault={handleDelete}>` のように、Svelteのイベント修飾子を使うと、クリックイベントのデフォルトの動作（この場合はフォーム送信）をキャンセルできます。

今回の例ではボタンはフォームの外側にあるため不要ですが、フォーム内にボタンを配置する際には注意が必要です。

# 7-2. 削除ボタンと確認ダイアログの実装 (フロントエンド)

前のチャプターで、タスクを削除するためのAPIを実装しました。今回はフロントエンド側で、このAPIを呼び出すためのUIを作成します。具体的には、タスク一覧の各行に削除ボタンを追加し、ユーザーが誤って削除してしまわないように、確認ダイアログを表示する機能を実装します。

## 1. 削除ボタンを一覧画面に追加する

まず、タスク一覧を表示している`Page.svelte`に、削除ボタンを追加します。

`app/assets/svelte/screens/tasks/Page.svelte`
```svelte
<!-- ... 既存のコード ... -->
          <tbody>
            {#each tasks as task}
              <tr>
                <td>{task.id}</td>
                <td>
                  <a href="/tasks/{task.id}">{task.title}</a>
                </td>
                <td>{task.status}</td>
                <td>{task.dueDate || ''}</td>
                <td>
                  <!-- ↓↓↓ ここから追加 ↓↓↓ -->
                  <Button
                    color="danger"
                    size="sm"
                    on:click={() => openDeleteConfirm(task)}
                  >
                    削除
                  </Button>
                  <!-- ↑↑↑ ここまで追加 ↑↑↑ -->
                </td>
              </tr>
            {/each}
          </tbody>
<!-- ... 既存のコード ... -->
<script lang="ts">
  // ... 既存のコード ...
  import { Button } from 'sveltestrap'; // Buttonをインポート

  // ... 既存のコード ...

  // ↓↓↓ ここから追加 ↓↓↓
  let selectedTask: Task | null = null;
  let isDeleteModalOpen = false;

  function openDeleteConfirm(task: Task) {
    selectedTask = task;
    isDeleteModalOpen = true;
  }
  // ↑↑↑ ここまで追加 ↑↑↑
</script>
```

`sveltestrap`から`Button`コンポーネントをインポートし、テーブルの各行に「削除」ボタンを配置しました。
ボタンがクリックされると`openDeleteConfirm`関数が呼び出され、どのタスクが選択されたかと、モーダルを開くためのフラグが設定されます。

## 2. 確認ダイアログ（モーダル）を実装する

次に、削除の確認を行うためのモーダルウィンドウを実装します。Sveltestrapの`Modal`コンポーネントを利用します。

`app/assets/svelte/screens/tasks/Page.svelte`
```svelte
<!-- ... 既存のコード ... -->
    </CardBody>
  </Card>
</div>

<!-- ↓↓↓ ここから追加 ↓↓↓ -->
{#if selectedTask}
  <Modal isOpen={isDeleteModalOpen} toggle={() => (isDeleteModalOpen = false)}>
    <ModalHeader toggle={() => (isDeleteModalOpen = false)}>
      タスク削除の確認
    </ModalHeader>
    <ModalBody>
      「{selectedTask.title}」を本当に削除しますか？
    </ModalBody>
    <ModalFooter>
      <Button color="danger" on:click={handleDelete}>はい、削除します</Button>
      <Button color="secondary" on:click={() => (isDeleteModalOpen = false)}>
        キャンセル
      </Button>
    </ModalFooter>
  </Modal>
{/if}
<!-- ↑↑↑ ここまで追加 ↑↑↑ -->

<script lang="ts">
  // ... 既存のコード ...
  import {
    Button,
    Modal,
    ModalBody,
    ModalFooter,
    ModalHeader,
  } from 'sveltestrap'; // Modal関連をインポート
  import { message } from '../../stores/message'; // messageストアをインポート
  import { api } from '../../utils/api'; // apiをインポート

  // ... 既存のコード ...

  function openDeleteConfirm(task: Task) {
    selectedTask = task;
    isDeleteModalOpen = true;
  }

  // ↓↓↓ ここから追加 ↓↓↓
  async function handleDelete() {
    if (!selectedTask) return;

    try {
      await api.delete(`/api/tasks/${selectedTask.id}`);
      message.set({
        type: 'success',
        text: `タスク「${selectedTask.title}」を削除しました。`,
      });
      // 画面をリロードして一覧を更新
      loadTasks();
    } catch (error) {
      console.error(error);
      message.set({
        type: 'danger',
        text: 'タスクの削除に失敗しました。',
      });
    } finally {
      isDeleteModalOpen = false;
      selectedTask = null;
    }
  }
  // ↑↑↑ ここまで追加 ↑↑↑
</script>
```

### 実装のポイント

- **Modalコンポーネント**: `sveltestrap`の`Modal`, `ModalHeader`, `ModalBody`, `ModalFooter`をインポートして使用します。`isOpen`プロパティに`isDeleteModalOpen`をバインドすることで、表示・非表示を制御します。
- **API呼び出し**: モーダル内の「はい、削除します」ボタンがクリックされると`handleDelete`関数が実行されます。この中で、`api.delete()`メソッドを使ってバックエンドの削除APIを呼び出します。URLには選択されたタスクのIDを含めます。
- **フィードバック**: API呼び出しの結果に応じて、成功または失敗のメッセージを`message`ストアにセットします。
- **一覧の更新**: 削除が成功したら、`loadTasks()`を再度呼び出して、最新のタスク一覧を再取得・表示します。
- **モーダルを閉じる**: 処理が完了したら（成功・失敗にかかわらず）、`finally`ブロックでモーダルを閉じ、選択されていたタスクの情報をクリアします。

これで、削除機能の実装は完了です。ブラウザで一覧画面を開き、削除ボタンをクリックしてモーダルが表示されること、そして実際にタスクが削除（論理削除）され、一覧から消えることを確認してください。

# 5-3. 登録・更新フォームの実装 (フロントエンド)

バックエンドAPIの準備が整ったので、次はフロントエンドにタスクを登録・更新するためのフォーム画面を作成します。

今回は、新規登録と編集の両方を一つの画面コンポーネントで扱います。URLによって、新規登録モードか編集モードかを判断し、表示や動作を切り替えるように実装します。
- `/tasks/new`: 新規登録フォーム
- `/tasks/:id/edit`: 既存タスクの編集フォーム

## 1. 画面コンポーネントの作成

まず、フォーム画面のSvelteコンポーネントを作成します。一覧画面と同様に、`screens` ディレクトリ配下に新しいディレクトリを作成します。

**`app/assets/svelte/screens/task-edit/Page.svelte`**
```svelte
<script lang="ts">
  import LayoutSideMenu from '~/layouts/LayoutSideMenu.svelte';
  import PageContainer from '~/layouts/PageContainer.svelte';
  import { onMount } from 'svelte';
  import {
    Button,
    Card,
    CardBody,
    CardFooter,
    CardHeader,
    Col,
    Form,
    FormGroup,
    Input,
    Label,
    Row
  } from 'sveltestrap';
  import * as api from '~/utils/api';
  import type { Task } from '~/components';
  import { page } from '$app/stores'; // SvelteKitのストアから現在のページ情報を取得

  // $page.paramsからIDを取得。なければ新規登録モード
  const id = $page.params.id;
  const isEditing = !!id;

  let task = $state<Partial<Task>>({
    title: '',
    isDone: false
  });
  let isLoading = $state(true);

  // 編集モードの場合、マウント時にタスクの現在情報を取得
  onMount(async () => {
    if (isEditing) {
      try {
        const res = await api.get<Task>(`tasks/${id}`);
        task = res.data;
      } catch (e) {
        console.error(e);
      }
    }
    isLoading = false;
  });

  async function handleSubmit() {
    try {
      if (isEditing) {
        await api.put(`tasks/${id}`, task);
      } else {
        await api.post('tasks', task);
      }
      // 成功したら一覧ページに遷移
      location.href = '/tasks';
    } catch (e) {
      console.error(e);
    }
  }

  const breadcrumbs = [{ name: 'Tasks', href: '/tasks' }, { name: isEditing ? 'Edit' : 'New' }];
</script>

<LayoutSideMenu>
  <PageContainer title={isEditing ? 'Edit Task' : 'New Task'} {breadcrumbs}>
    {#snippet body()}
      {#if isLoading}
        <p>読み込み中...</p>
      {:else}
        <Row>
          <Col md={{ size: 8, offset: 2 }}>
            <Card>
              <CardHeader>{isEditing ? `Edit Task (ID: ${id})` : 'Create New Task'}</CardHeader>
              <Form on:submit|preventDefault={handleSubmit}>
                <CardBody>
                  <FormGroup>
                    <Label for="title">Title</Label>
                    <Input id="title" name="title" bind:value={task.title} required />
                  </FormGroup>
                  <FormGroup check>
                    <Input id="isDone" type="checkbox" bind:checked={task.isDone} />
                    <Label for="isDone" check>Done</Label>
                  </FormGroup>
                </CardBody>
                <CardFooter class="text-end">
                  <Button color="secondary" href="/tasks">キャンセル</Button>
                  <Button color="primary" type="submit">保存</Button>
                </CardFooter>
              </Form>
            </Card>
          </Col>
        </Row>
      {/if}
    {/snippet}
  </PageContainer>
</LayoutSideMenu>
```

## 2. ControllerとRouteの追加 (Web)

このSvelteコンポーネントを表示するためのPlay FrameworkのControllerアクションとRoute定義を追加します。

**`app/controllers/web/TaskController.java`**
```java
package controllers.web;

import play.mvc.Controller;
import play.mvc.Result;

public class TaskController extends Controller {

    public Result list() {
        return ok(views.html.screen.render("tasks/Page.svelte"));
    }

    // 新規登録画面用のアクション
    public Result newScreen() {
        return ok(views.html.screen.render("task-edit/Page.svelte"));
    }

    // 編集画面用のアクション
    public Result editScreen(Long id) {
        // idはSvelte側でURLから取得するため、ここでは未使用
        return ok(views.html.screen.render("task-edit/Page.svelte"));
    }
}
```

**`conf/routes`**
```
# --- Tasks ---
GET     /tasks                      controllers.web.TaskController.list()
GET     /tasks/new                  controllers.web.TaskController.newScreen()
GET     /tasks/:id/edit             controllers.web.TaskController.editScreen(id: Long)
```

## 3. コードの解説

### Svelteコンポーネント (`Page.svelte`)

- **URLパラメータの取得**:
  - `import { page } from '$app/stores';`: このプロジェクトではSvelteKitのルーティング機能は使っていませんが、`$app/stores` を利用して現在のURL情報を取得できるように設定されています。
  - `const id = $page.params.id;`: `$page` ストアを通じて、URLのパスパラメータ（例: `/tasks/123/edit` の `123`）を取得します。
  - `const isEditing = !!id;`: `id` が存在すれば編集モード、しなければ新規登録モードと判定します。

- **データの双方向バインディング**:
  - `let task = $state<Partial<Task>>({...});`: フォームの各入力値を保持するためのリアクティブな状態を定義します。`Partial<Task>` は、`Task` 型のプロパティをすべてオプショナルにするTypeScriptの型です。
  - `<Input ... bind:value={task.title} />`: `bind:` ディレクティブを使うことで、`task.title` の値とテキスト入力フィールドの値を**双方向で**同期させます。ユーザーがテキストボックスに何か入力すると `task.title` の値が即座に更新され、逆にコード側で `task.title` の値を変更するとテキストボックスの表示も更新されます。チェックボックスの `bind:checked` も同様です。

- **フォームの送信処理**:
  - `<Form on:submit|preventDefault={handleSubmit}>`: フォームが送信されたとき（`submit` イベントが発生したとき）に `handleSubmit` 関数を呼び出します。`|preventDefault` は、フォーム送信時のデフォルトの動作（ページリロード）を抑制するためのSvelteの修飾子です。
  - `handleSubmit` 関数内では、`isEditing` フラグを見て、`api.put` (更新) と `api.post` (登録) のどちらを呼び出すかを分岐させています。
  - 処理が成功したら、`location.href = '/tasks';` でブラウザを一覧画面にリダイレクトさせています。

### 動作確認

1.  一覧画面（`/tasks`）の「新規登録」ボタンのリンク先を `/tasks/new` に修正します。
2.  ブラウザで [http://localhost:9000/tasks/new](http://localhost:9000/tasks/new) にアクセスし、新規登録フォームが表示されることを確認します。
3.  適当なタイトルを入力して「保存」ボタンを押し、タスクが作成されて一覧画面に戻ることを確認します。
4.  一覧画面に編集ボタンを追加し、そのリンク先を `/tasks/:id/edit` (例: `/tasks/1/edit`) とします。
5.  編集ボタンをクリックして編集フォームが表示され、タスクの現在のタイトルなどがフォームに表示されていることを確認します。
6.  内容を編集して「保存」ボタンを押し、変更が反映されて一覧画面に戻ることを確認します。

---

これで、データの登録・更新を行うための一連の機能が実装できました。しかし、現状では処理が成功したかどうかがユーザーに分かりにくいため、次の章では操作結果をフィードバックメッセージとして表示する仕組みを実装します。

# 4-4. APIを呼び出して画面に表示 (フロントエンド)

バックエンドでデータ取得APIの準備が整いました。いよいよ、このAPIをSvelteコンポーネントから呼び出し、取得したタスク一覧を画面に表示します。

## APIクライアントの準備

APIを呼び出す際には、毎回 `fetch` APIを使ってURLやリクエストヘッダーを指定することもできますが、プロジェクトが大きくなると煩雑になりがちです。

このプロジェクトでは、`app/assets/svelte/utils/api.ts` に、API通信を簡単に行うための共通クライアントが用意されています。このクライアントには、以下のような便利な機能が組み込まれています。

- **ベースURLの自動付与**: `get('/tasks')` のようにパスを指定するだけで、自動的に `http://localhost:9000/api` のようなベースURLを付加してくれます。
- **CSRFトークンの自動付与**: Play FrameworkのCSRF（クロスサイトリクエストフォージェリ）保護機能に対応するため、POSTやPUTといったリクエストを送信する際に、必要なCSRFトークンを自動的にリクエストヘッダーに付与します。
- **エラーハンドリング**: バックエンドがエラーレスポンス（HTTPステータスコードが400番台や500番台）を返した場合、それをPromiseの例外（rejection）としてスローします。これにより、`try...catch` 構文でAPIエラーを簡単に捕捉できます。

## SvelteコンポーネントからのAPI呼び出し

それでは、`tasks/Page.svelte` を編集して、APIを呼び出し、取得したデータを画面に表示する処理を追加しましょう。

**`app/assets/svelte/screens/tasks/Page.svelte`**
```svelte
<script lang="ts">
  import LayoutSideMenu from '~/layouts/LayoutSideMenu.svelte';
  import PageContainer from '~/layouts/PageContainer.svelte';
  import { onMount } from 'svelte';
  import { Button, Card, CardBody, CardHeader, Col, Row, Table } from 'sveltestrap';
  import * as api from '~/utils/api';
  import type { Task } from '~/components';

  const breadcrumbs = [{ name: 'Tasks' }];

  // $stateでリアクティブな状態を定義
  let tasks = $state<Task[]>([]);
  let isLoading = $state(true);

  // onMountライフサイクル関数内でAPIを呼び出す
  onMount(async () => {
    try {
      const res = await api.get<Task[]>('tasks');
      tasks = res.data;
    } catch (e) {
      console.error(e);
      // ここでエラーメッセージを表示する処理などを追加
    } finally {
      isLoading = false;
    }
  });
</script>

<LayoutSideMenu>
  <PageContainer title="Tasks" breadcrumbs={breadcrumbs}>
    {#snippet body()}
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
              {#if isLoading}
                <p>読み込み中...</p>
              {:else}
                <Table hover>
                  <thead>
                    <tr>
                      <th>ID</th>
                      <th>タイトル</th>
                      <th>完了</th>
                      <th>作成日時</th>
                      <th>更新日時</th>
                    </tr>
                  </thead>
                  <tbody>
                    {#each tasks as task (task.id)}
                      <tr>
                        <td>{task.id}</td>
                        <td>{task.title}</td>
                        <td>{task.isDone ? '✅' : ''}</td>
                        <td>{new Date(task.createdAt).toLocaleString()}</td>
                        <td>{new Date(task.updatedAt).toLocaleString()}</td>
                      </tr>
                    {/each}
                  </tbody>
                </Table>
              {/if}
            </CardBody>
          </Card>
        </Col>
      </Row>
    {/snippet}
  </PageContainer>
</LayoutSideMenu>

<style>
  /* スタイルは変更なし */
</style>
```

### 変更点の解説 (script)

- **`import { onMount } from 'svelte';`**:
  Svelteの「ライフサイクル関数」の一つである `onMount` をインポートします。`onMount` に登録したコールバック関数は、コンポーネントがDOMにマウント（描画）された直後に一度だけ実行されます。APIからのデータ取得のような初期化処理に最適です。

- **`import * as api from '~/utils/api';`**:
  先ほど説明した共通APIクライアントを `api` という名前でインポートします。

- **`let tasks = $state<Task[]>([]);`**:
  Svelte 5のRune `$state` を使って、タスクのリストを保持するためのリアクティブな状態変数 `tasks` を宣言しています。初期値は空の配列です。この変数の値が変更されると、Svelteは自動的に画面の関連する部分を更新します。`isLoading` も同様に、データの読み込み状態を管理します。

- **`onMount(async () => { ... });`**:
  `onMount` の中で非同期関数を定義し、APIを呼び出します。
  - `const res = await api.get<Task[]>('tasks');`: `api.get` を使って `/api/tasks` エンドポイントにリクエストを送信します。`await` で非同期処理の完了を待ちます。ジェネリクス `<Task[]>` でレスポンスデータの型を指定しています。
  - `tasks = res.data;`: APIから受け取ったタスクのリスト（`res.data`）を、状態変数 `tasks` に代入します。この代入が行われた瞬間に、Svelteが画面の更新をスケジューリングします。
  - `try...catch...finally`: API通信が失敗した場合のエラーを捕捉し、成功・失敗にかかわらず最後に `isLoading` を `false` に設定しています。

### 変更点の解説 (HTML)

- **`{#if isLoading}`**:
  `isLoading` の値に応じて表示を切り替えます。`true` の間は「読み込み中...」というテキストを表示し、`false` になったらテーブルを表示します。

- **`<Table>`**:
  Sveltestrapのテーブルコンポーネントを使って、取得したデータを表形式で表示します。

- **`{#each tasks as task (task.id)}`**:
  Svelteの `each` ブロックを使って、`tasks` 配列の各要素をループ処理します。
  - `(task.id)` は「キー指定」と呼ばれ、リストの各項目を一意に識別するためのものです。Svelteがリストの変更を効率的にDOMに反映させるために役立ちます。
  - ループの中で、`task.id` や `task.title` といったプロパティを表示しています。
  - `createdAt` と `updatedAt` は `new Date(...).toLocaleString()` を使って、人間が読みやすい形式の日付文字列に変換しています。

### 動作確認

H2コンソールなどを使って、`task` テーブルにいくつかのテストデータを挿入しておきましょう。

```sql
INSERT INTO task (title, is_done, created_at, updated_at) VALUES ('Svelteの学習', true, NOW(), NOW());
INSERT INTO task (title, is_done, created_at, updated_at) VALUES ('Play Frameworkの学習', false, NOW(), NOW());
```

ブラウザで [http://localhost:9000/tasks](http://localhost:9000/tasks) を開いてください。
一瞬「読み込み中...」と表示された後、データベースに登録したタスクがテーブルに表示されれば成功です！

---

これで、バックエンドから取得した動的なデータをフロントエンドに表示する、Webアプリケーションの基本的な流れが完成しました。次の章では、この処理で使われている `async/await` といった非同期処理の仕組みについて、もう少し詳しく解説します。

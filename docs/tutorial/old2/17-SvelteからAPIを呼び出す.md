# 17. SvelteからAPIを呼び出す

バックエンドでデータ取得APIの準備ができたので、次はこのAPIをフロントエンドのSvelteコンポーネントから呼び出し、取得したデータを画面に表示します。

`tasks` 画面の実装 (`app/assets/svelte/screens/tasks/`) を参考に、その仕組みを学びましょう。

## API呼び出しロジックのカプセル化

APIのエンドポイントURLや、リクエスト/レスポンスの型定義などをコンポーネントファイル (`Page.svelte`) に直接記述すると、コードが煩雑になりがちです。
このプロジェクトでは、API関連のロジックを `api.ts` のような別のファイルに分離するアプローチを取っています。

**`app/assets/svelte/screens/tasks/api.ts`**:
```typescript
import { get } from "@app/assets/svelte/utils/api";
import type { Task } from "@app/models/Task"; // バックエンドのモデルを型として再利用

// APIレスポンスの型定義
export interface TasksApiResponse {
  list: Task[];
  total: number;
}

/**
 * タスク一覧を取得するAPIを呼び出します。
 * @param term 検索キーワード
 * @returns APIのレスポンス
 */
export const fetchTasks = async (term: string): Promise<TasksApiResponse> => {
  // URLのクエリパラメータを構築
  const params = new URLSearchParams();
  if (term) {
    params.append("term", term);
  }
  // 共通ヘルパー関数を使ってAPIを呼び出し
  return await get<TasksApiResponse>(`/api/tasks?${params.toString()}`);
};
```

### コードの解説
- `import { get } from "@app/assets/svelte/utils/api"`:
  このプロジェクトには、`fetch` APIをラップした便利なヘルパー関数 (`get`, `post`, `put`, `delete`) が `utils/api.ts` に用意されています。ここでは `GET` リクエストを送信するために `get` 関数をインポートしています。
- `export interface TasksApiResponse`:
  APIが返すJSONの構造をTypeScriptの `interface` として定義しています。これにより、レスポンスのデータ構造が明確になり、型安全性が向上します。
- `export const fetchTasks = async (...)`:
  実際にAPIを呼び出す関数です。`async/await` を使った非同期関数として定義されています。
- `get<TasksApiResponse>(...)`:
  `get` ヘルパー関数を呼び出しています。ジェネリクス (`<TasksApiResponse>`) でレスポンスの型を指定することで、この関数の戻り値が `TasksApiResponse` 型であることが保証されます。

このようにAPI呼び出しを別ファイルに切り出すことで、コンポーネントは「どのAPIを叩くか」という詳細を意識する必要がなくなり、「`fetchTasks` 関数を呼び出す」という、より抽象的なレベルでロジックを記述できます。

## コンポーネントからのAPI呼び出し

次に、`Page.svelte` から `fetchTasks` 関数を呼び出します。
APIの呼び出しは、コンポーネントが画面に表示された（マウントされた）タイミングで行うのが一般的です。Svelteでは、そのために `onMount` というライフサイクル関数を使用します。

**`app/assets/svelte/screens/tasks/Page.svelte`**:
```svelte
<script lang="ts">
  import { onMount } from "svelte";
  import { fetchTasks, type TasksApiResponse } from "./api";

  let tasksResponse: TasksApiResponse | null = null;

  // コンポーネントがマウントされたときに一度だけ実行される
  onMount(async () => {
    tasksResponse = await fetchTasks(""); // 初期表示時は検索キーワードなし
  });
</script>

<!-- ... -->

{#if tasksResponse}
  <table>
    <thead>
      <!-- ... -->
    </thead>
    <tbody>
      {#each tasksResponse.list as task}
        <tr>
          <td>{task.id}</td>
          <td>{task.subject}</td>
          <!-- ... -->
        </tr>
      {/each}
    </tbody>
  </table>
{/if}
```

### コードの解説
- `import { onMount } from "svelte"`:
  `onMount` 関数をSvelteからインポートします。
- `let tasksResponse: TasksApiResponse | null = null`:
  APIのレスポンスを格納するための変数を定義します。初期状態ではデータが存在しないため `null` を入れておきます。
- `onMount(async () => { ... })`:
  `onMount` のコールバック関数として非同期関数を渡し、その中で `fetchTasks` を呼び出しています。
- `tasksResponse = await fetchTasks("")`:
  API呼び出しが完了し、レスポンスが返ってくると、その結果が `tasksResponse` 変数に代入されます。Svelteのリアクティビティにより、この変数の値が変わると、UIが自動的に更新されます。
- `{#if tasksResponse} ... {/if}`:
  `tasksResponse` が `null` でない（データが取得できた）場合にのみ、テーブルを描画します。これにより、データ取得前にテーブルが表示されてしまうのを防ぎます。
- `{#each tasksResponse.list as task} ... {/each}`:
  取得したタスクのリストをループ処理し、テーブルの各行 (`<tr>`) を生成します。

これで、画面が表示されると自動的にバックエンドAPIが呼び出され、取得したタスクの一覧がテーブルに表示されるようになりました。

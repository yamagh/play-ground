# 20. データ登録・更新APIの作成

一覧画面に続いて、フォームからデータを新規登録したり、既存のデータを更新したりするためのAPIを作成します。
REST APIの設計原則に基づき、登録には `POST`、更新には `PUT` メソッドを使用します。

## 1. Controller: リクエストボディの受け取りと処理

フロントエンドのフォームから送信されたデータは、HTTPリクエストの「ボディ」にJSON形式で含まれています。
`TaskController` で、このリクエストボディを受け取り、`Task` モデルオブジェクトに変換して、`TaskRepository` に処理を委譲します。

**`app/controllers/api/TaskController.java`**:
```java
// ...
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
// ...

public class TaskController extends Controller {
    // ...

    /**
     * 新しいタスクを作成します。
     * @param request HTTPリクエスト
     * @return 作成されたタスクのJSON
     */
    public Result create(Http.Request request) {
        // リクエストボディからJSONを取得
        final JsonNode json = request.body().asJson();
        if (json == null) {
            return badRequest("JSON body is expected.");
        }

        // JSONをTaskオブジェクトに変換
        final Task task = Json.fromJson(json, Task.class);

        // リポジトリを呼び出して永続化
        this.taskRepository.save(task);

        return ok(Json.toJson(task));
    }

    /**
     * 既存のタスクを更新します。
     * @param id タスクID
     * @param request HTTPリクエスト
     * @return 更新されたタスクのJSON
     */
    public Result update(final Long id, Http.Request request) {
        final JsonNode json = request.body().asJson();
        if (json == null) {
            return badRequest("JSON body is expected.");
        }

        final Task task = Json.fromJson(json, Task.class);
        // URLのIDをモデルに設定
        task.id = id;

        // リポジトリを呼び出して更新
        this.taskRepository.update(task);

        return ok(Json.toJson(task));
    }
}
```

### コードの解説
- `request.body().asJson()`:
  リクエストボディを `JsonNode` オブジェクト（JSON構造を表現するオブジェクト）として解析します。もしリクエストボディがJSON形式でない、または空の場合は `null` を返します。
- `Json.fromJson(json, Task.class)`:
  `JsonNode` オブジェクトを、指定した `Task.class` のインスタンスに変換（デシリアライズ）します。JSONのキーとクラスのフィールド名が一致するプロパティに、値が自動的にマッピングされます。
- `task.id = id`:
  更新 (`update`) の場合、どのタスクを更新するのかを特定するために、URLから受け取った `id` を `Task` オブジェクトに設定しています。

## 2. Repository: データの永続化

`TaskRepository` に、`Task` オブジェクトをデータベースに保存するための `save()` と `update()` メソッドを実装します。
Ebeanの `Model` クラスを継承しているため、これらの実装は非常にシンプルです。

**`app/repository/TaskRepository.java`**:
```java
// ...
public class TaskRepository {
    // ...

    /**
     * タスクを保存（新規登録）します。
     * @param task 保存するタスク
     * @return 保存されたタスク
     */
    public Task save(final Task task) {
        task.save(); // Ebeanのsave()メソッドを呼び出す
        return task;
    }

    /**
     * タスクを更新します。
     * @param task 更新するタスク
     * @return 更新されたタスク
     */
    public Task update(final Task task) {
        task.update(); // Ebeanのupdate()メソッドを呼び出す
        return task;
    }
}
```
- `task.save()`:
  Ebeanは、このオブジェクトの `id` が `null` であることから新規データであると判断し、`INSERT` 文を生成して実行します。実行後、自動採番されたIDが `task` オブジェクトに設定されます。
- `task.update()`:
  `id` が `null` でないため、Ebeanは既存データの更新であると判断し、`UPDATE` 文を生成して実行します。

## 3. routesの設定

最後に、作成した `create` と `update` アクションをURLにマッピングします。

**`conf/routes`**:
```
# Tasks API
# ... (GETリクエストの定義) ...
POST    /api/tasks            controllers.api.TaskController.create(request: Request)
PUT     /api/tasks/:id        controllers.api.TaskController.update(id: Long, request: Request)
```
- `POST /api/tasks`: 新規タスクの作成。
- `PUT /api/tasks/:id`: `:id` で指定されたIDを持つ既存タスクの更新。`:id` の部分は、`update` メソッドの `id` 引数に `Long` 型として渡されます。

これで、フロントエンドから送信されたデータをデータベースに登録・更新するためのAPIが完成しました。

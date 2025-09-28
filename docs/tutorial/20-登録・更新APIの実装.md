# 5-1. 登録・更新APIの実装 (バックエンド)

一覧画面ができたので、次はタスクを新しく登録したり、既存のタスクを編集したりする機能を追加します。まずは、そのバックエンド処理となる登録・更新APIを実装しましょう。

RESTの原則に従い、以下の2つのエンドポイントを作成します。
- `POST /api/tasks`: 新しいタスクを登録する。
- `PUT /api/tasks/:id`: 既存のタスクを更新する。（`:id` は更新対象タスクのID）

多くの場合、登録と更新のロジックは似通っているため、1つのメソッドで両方の処理を扱うことができます。

## 1. Repositoryへのメソッド追加

まず、`app/repository/TaskRepository.java` に、タスクを保存（登録または更新）するためのメソッドを追加します。

**`app/repository/TaskRepository.java`**
```java
// ... (既存のコードは省略)

public class TaskRepository {
    // ... (既存のコードは省略)

    /**
     * タスクを保存（登録 or 更新）します。
     * @param task 保存するTaskオブジェクト
     * @return 保存後のTaskオブジェクト
     */
    public CompletionStage<Task> save(Task task) {
        return supplyAsync(() -> {
            ebeanServer.save(task);
            return task;
        }, executionContext);
    }

    /**
     * IDでタスクを検索します。
     * @param id タスクID
     * @return 検索結果のOptional<Task>
     */
    public CompletionStage<Optional<Task>> findById(Long id) {
        return supplyAsync(() ->
            Optional.ofNullable(ebeanServer.find(Task.class).setId(id).findOne()),
            executionContext
        );
    }
}
```
- **`save(Task task)`**:
  Ebeanの `save()` メソッドは非常に賢く、引数で渡された `Task` オブジェクトの `id` フィールドが `null` の場合は新しいレコードとして `INSERT` し、`id` が存在する場合は既存のレコードを `UPDATE` します。このおかげで、登録と更新の処理を1つのメソッドで共通化できます。
- **`findById(Long id)`**:
  更新処理のために、まずデータベースから対象のタスクを取得する必要があります。そのためのメソッドも追加しておきます。結果が見つからない可能性もあるため、`Optional<Task>` でラップして返します。

## 2. Controllerへのアクション追加

次に、`app/controllers/api/TaskController.java` に、リクエストを受け取ってタスクを登録・更新するアクションを追加します。

**`app/controllers/api/TaskController.java`**
```java
package controllers.api;

// ... (importを追加)
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import play.mvc.BodyParser;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
// ...

public class TaskController extends Controller {
    // ... (既存のコードは省略)

    /**
     * タスクを新規登録します。
     */
    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> create(Http.Request request) {
        JsonNode json = request.body().asJson();
        Task task = Json.fromJson(json, Task.class);
        // IDはサーバー側で採番するので、リクエストに含まれていても無視する
        task.id = null;
        return taskRepository.save(task).thenApplyAsync(savedTask ->
            created(Json.toJson(savedTask))
        );
    }

    /**
     * 既存のタスクを更新します。
     */
    @BodyParser.Of(BodyParser.Json.class)
    public CompletionStage<Result> update(Long id, Http.Request request) {
        JsonNode json = request.body().asJson();
        Task taskFromBody = Json.fromJson(json, Task.class);

        return taskRepository.findById(id).thenComposeAsync(optionalTask -> {
            if (optionalTask.isPresent()) {
                Task existingTask = optionalTask.get();
                // 既存のレコードに対して、リクエストボディの内容を反映
                existingTask.title = taskFromBody.title;
                existingTask.isDone = taskFromBody.isDone;
                return taskRepository.save(existingTask).thenApplyAsync(savedTask ->
                    ok(Json.toJson(savedTask))
                );
            } else {
                // 対象のタスクが見つからない場合は 404 Not Found を返す
                return CompletableFuture.completedFuture(notFound());
            }
        });
    }
}
```
- **`@BodyParser.Of(BodyParser.Json.class)`**:
  このアクションがJSON形式のリクエストボディを受け取ることをPlayに伝えます。これにより、`request.body().asJson()` でリクエストボディを `JsonNode` として安全に取得できます。
- **`Json.fromJson(json, Task.class)`**:
  PlayのJSONライブラリの機能で、`JsonNode` を指定したクラス（この場合は `Task`）のオブジェクトに自動的に変換（デシリアライズ）してくれます。
- **`create()` メソッド**:
  - 新規登録なので、クライアントから送られてきた `id` は無視して `null` を設定し、Ebeanに新しいレコードとして扱わせます。
  - `taskRepository.save()` を呼び出し、保存が完了したら、HTTPステータスコード `201 Created` と、データベースによって採番されたIDを含む最新のTaskオブジェクトをJSONで返します。
- **`update(Long id, ...)` メソッド**:
  - まず `taskRepository.findById(id)` で更新対象のタスクがデータベースに存在するかを確認します。
  - **`thenComposeAsync`**: 非同期処理の結果を使って、さらに別の非同期処理を呼び出す場合に使います。ここでは `findById` の結果（`Optional<Task>`）に応じて、`save` を呼び出すか `notFound` を返すかを分岐しています。
  - 存在すれば、リクエストボディの内容で既存のタスクオブジェクトのフィールドを上書きし、`save()` を呼び出して更新します。
  - 存在しなければ、HTTPステータスコード `404 Not Found` を返します。

## 3. routesの設定

最後に、作成したアクションをURLに紐付けます。

`conf/routes` ファイルに以下の2行を追記してください。

**`conf/routes`**
```
POST    /api/tasks                  controllers.api.TaskController.create(request: Request)
PUT     /api/tasks/:id              controllers.api.TaskController.update(id: Long, request: Request)
```
- **`POST /api/tasks`**: `create` アクションに紐付けます。
- **`PUT /api/tasks/:id`**: `update` アクションに紐付けます。`:id` の部分はURLのパスパラメータとなり、Playが自動的にLong型の `id` としてアクションメソッドに渡してくれます。

### 動作確認

curlやPostmanのようなAPIテストツールを使って、動作を確認してみましょう。

**新規登録 (POST)**
- URL: `http://localhost:9000/api/tasks`
- Method: `POST`
- Headers: `Content-Type: application/json`
- Body:
  ```json
  {
    "title": "APIのテスト",
    "isDone": false
  }
  ```
成功すれば、`id` が採番されたTaskオブジェクトが返ってきます。

**更新 (PUT)**
- URL: `http://localhost:9000/api/tasks/1` (先ほど作成したタスクのIDを指定)
- Method: `PUT`
- Headers: `Content-Type: application/json`
- Body:
  ```json
  {
    "title": "APIのテスト（更新）",
    "isDone": true
  }
  ```
成功すれば、更新後のTaskオブジェクトが返ってきます。

---

これで、タスクの登録と更新を行うためのバックエンドAPIが完成しました。次の章では、Controllerの実装で利用したDI（Dependency Injection）という重要な設計パターンについて解説します。

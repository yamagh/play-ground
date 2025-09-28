# 7-1. データ削除APIの実装 (バックエンド)

これまでのチュートリアルで、タスクの登録・更新機能まで実装できました。今回は、不要になったタスクを削除する機能を追加します。

物理的にデータベースからレコードを削除する「物理削除」と、削除フラグを立てて見かけ上削除されたように見せる「論理削除」がありますが、今回は`isActive`というフラグを利用して論理削除を実装します。これにより、万が一の場合のデータ復旧が容易になります。

## 1. Repositoryに削除メソッドを追加する

まず、`TaskRepository.java`にタスクを論理削除するためのメソッドを追加します。

`app/repository/TaskRepository.java`
```java
// ... 既存のコード ...

import java.util.Optional; // Optionalをインポート

public class TaskRepository {
    // ... 既存のコード ...

    public Optional<Task> findById(Long id) {
        return Optional.ofNullable(finder.byId(id));
    }

    public Task save(Task task) {
        if (task.id == null) {
            task.save();
        } else {
            task.update();
        }
        return task;
    }

    // ↓↓↓ ここから追加 ↓↓↓
    /**
     * 指定されたIDのタスクを論理削除します。
     *
     * @param id 削除するタスクのID
     * @return 削除処理が成功した場合はtrue、タスクが見つからない場合はfalse
     */
    public boolean delete(Long id) {
        Optional<Task> taskOptional = findById(id);
        if (taskOptional.isPresent()) {
            Task task = taskOptional.get();
            task.isActive = false; // 論理削除フラグを立てる
            task.update();
            return true;
        }
        return false;
    }
    // ↑↑↑ ここまで追加 ↑↑↑
}
```

`findById`メソッドも追加しておくと便利なので、ここで追加しておきます。
`delete`メソッドでは、まず`findById`でタスクを検索します。タスクが存在すれば、`isActive`プロパティを`false`に設定して`update()`を呼び出し、更新をデータベースに反映させます。

## 2. Controllerに削除アクションを追加する

次に、APIリクエストを処理する`TaskController.java`に、削除を実行するアクションを追加します。

`app/controllers/api/TaskController.java`
```java
// ... 既存のコード ...

public class TaskController extends Controller {
    // ... 既存のコード ...

    @Inject
    public TaskController(
        Http.Request request,
        TaskRepository taskRepository,
        FormFactory formFactory,
        MessagesApi messagesApi) {
        // ... 既存のコード ...
    }

    // ... list(), save() メソッド ...

    // ↓↓↓ ここから追加 ↓↓↓
    /**
     * 指定されたIDのタスクを削除します。
     *
     * @param id 削除するタスクのID
     * @return 処理結果を示すJSONレスポンス
     */
    public Result delete(Long id) {
        boolean deleted = taskRepository.delete(id);
        if (deleted) {
            return ok(Json.toJson(Map.of("message", "Task deleted successfully.")));
        } else {
            return notFound(Json.toJson(Map.of("error", "Task not found.")));
        }
    }
    // ↑↑↑ ここまで追加 ↑↑↑
}
```

この`delete`アクションは、URLのパスから受け取った`id`を`TaskRepository`の`delete`メソッドに渡します。
- 削除が成功した場合（`true`が返された場合）: HTTPステータス`200 OK`と成功メッセージを返します。
- 削除対象が見つからなかった場合（`false`が返された場合）: HTTPステータス`404 Not Found`とエラーメッセージを返します。

## 3. routesに設定を追加する

最後に、`conf/routes`ファイルを編集して、新しいAPIエンドポイントを定義します。

`conf/routes`
```
# ... 既存のルート設定 ...

# Task API
GET     /api/tasks                  controllers.api.TaskController.list(request: Request)
POST    /api/tasks                  controllers.api.TaskController.save(request: Request)
PUT     /api/tasks/:id              controllers.api.TaskController.save(request: Request, id: Long)
# ↓↓↓ ここから追加 ↓↓↓
DELETE  /api/tasks/:id              controllers.api.TaskController.delete(id: Long)
# ↑↑↑ ここまで追加 ↑↑↑

# ... 既存のルート設定 ...
```

`DELETE`メソッドで`/api/tasks/:id`という形式のURLへのリクエストがあった場合に、`TaskController`の`delete`アクションが呼び出されるように設定しました。`:id`の部分は動的なパラメータとして扱われ、アクションメソッドの引数に渡されます。

これでバックエンド側の実装は完了です。
次のチャプターでは、フロントエンドに削除ボタンを設置し、このAPIを呼び出す処理を実装します。

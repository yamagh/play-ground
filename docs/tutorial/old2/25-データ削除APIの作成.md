# 25. データ削除APIの作成

アプリケーションの基本的なCRUD (Create, Read, Update, Delete) 機能の最後として、データを削除するためのAPIを実装します。
RESTの原則に従い、リソースの削除には `DELETE` HTTPメソッドを使用します。

実装の基本的な流れは、データ取得や更新APIの作成と似ています。

1.  **Repository**: Ebeanを使ってデータベースからレコードを削除するロジックを実装します。
2.  **Controller**: HTTPリクエストを受け取り、Repositoryを呼び出すアクションを実装します。
3.  **routes**: `DELETE` メソッドのリクエストを、作成したControllerのアクションにマッピングします。

## 1. Repository: データの削除処理

`TaskRepository.java` に、指定されたIDのタスクを削除するメソッドを追加します。

**`app/repository/TaskRepository.java`**:
```java
// ...
public class TaskRepository {
    // ...

    /**
     * IDで指定されたタスクを削除します。
     * @param id 削除するタスクのID
     * @return 削除に成功した場合はtrue、該当タスクが見つからない場合はfalse
     */
    public boolean delete(final Long id) {
        final Task task = DB.find(Task.class, id);
        if (task != null) {
            task.delete(); // Ebeanのdelete()メソッドを呼び出す
            return true;
        }
        return false;
    }
}
```
- `DB.find(Task.class, id)`: まず、削除対象のタスクがデータベースに存在するかどうかを確認するために、IDで検索します。
- `task.delete()`: タスクが見つかった場合、そのオブジェクトの `delete()` メソッドを呼び出します。Ebeanは、このオブジェクトのIDを元に `DELETE FROM tasks WHERE id = ?` というSQLを生成して実行します。
- 存在しないIDが指定された場合に `false` を返すことで、Controller側で「対象が見つからなかった」というエラーハンドリングが可能になります。

## 2. Controller: APIエンドポイントの実装

`TaskController.java` に、`DELETE` リクエストを処理する `delete` アクションを追加します。

**`app/controllers/api/TaskController.java`**:
```java
// ...
public class TaskController extends Controller {
    // ...

    /**
     * タスクを削除します。
     * @param id 削除するタスクのID
     * @return 成功した場合は204 No Content、失敗した場合は404 Not Found
     */
    public Result delete(final Long id) {
        final boolean deleted = this.taskRepository.delete(id);
        if (deleted) {
            // 成功した場合、ボディなしの 204 No Content を返すのが一般的
            return noContent();
        } else {
            // 対象が見つからなかった場合
            return notFound("Task not found.");
        }
    }
}
```
- `this.taskRepository.delete(id)`: Repositoryの `delete` メソッドを呼び出します。
- `return noContent()`: 削除が成功した場合、クライアントに返すコンテンツは特にありません。このような場合、REST APIでは `204 No Content` というHTTPステータスコードを返すのが一般的です。Playの `noContent()` ヘルパーがこのレスポンスを生成します。
- `return notFound(...)`: `delete` メソッドが `false` を返した場合（つまり、削除しようとしたタスクが存在しなかった場合）、`404 Not Found` ステータスを返します。

## 3. routesの設定

最後に、`conf/routes` ファイルに `DELETE` メソッドのルーティングを追加します。

**`conf/routes`**:
```
# Tasks API
# ... (GET, POST, PUTの定義) ...
DELETE  /api/tasks/:id        controllers.api.TaskController.delete(id: Long)
```
- `DELETE /api/tasks/:id`: `DELETE` メソッドで `/api/tasks/123` のようなURLにリクエストがあった場合に、`TaskController.delete` アクションを呼び出します。URLの `:id` 部分は、`delete` メソッドの `id` 引数に渡されます。

これで、タスクを削除するためのバックエンドAPIが完成しました。
次のステップでは、SvelteのUIからこのAPIを呼び出す方法を学びます。

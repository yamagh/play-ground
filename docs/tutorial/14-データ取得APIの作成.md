# 14. データ取得APIの作成

ここからは、フロントエンドのSvelteコンポーネントとバックエンドを連携させ、動的な一覧画面を作成していきます。
最初のステップとして、データベースからタスクの一覧を取得し、JSON形式で返すAPIを作成します。

このプロジェクトでは、バックエンドの役割を以下の4つの層に分割しています。これは「関心の分離」というソフトウェア設計の原則に基づいています。

- **`models`**: データベースのテーブル構造を表現するクラス。
- **`repository`**: データベースへの具体的なアクセス処理（SQLクエリの発行など）を担当するクラス。
- **`services`**: ビジネスロジックを担当するクラス。複数のリポジトリを組み合わせて複雑な処理を行うこともあります。
- **`controllers/api`**: HTTPリクエストを受け取り、レスポンス（主にJSON）を返すことに特化したクラス。

## 1. Repository: データベースからのデータ取得

まず、データベースからタスクを取得するロジックを `TaskRepository` に実装します。
このプロジェクトでは、EbeanというORMライブラリを使用しており、SQLを直接書かなくてもJavaのコードで安全にクエリを組み立てることができます。

`app/repository/TaskRepository.java` を開き、タスク一覧を取得するためのメソッドを追加します。（このチュートリアルでは、既存の `find` メソッドを参考に解説します）

**`app/repository/TaskRepository.java`**:
```java
// ...

public class TaskRepository {
    // ...

    /**
     * タスクを検索します。
     * @param term 検索キーワード (件名)
     * @return タスクのリスト
     */
    public PagedList<Task> find(final String term) {
        final ExpressionList<Task> expressionList = DB.find(Task.class).where();
        if (term != null && !term.isEmpty()) {
            expressionList.icontains("subject", term); // あいまい検索
        }
        return expressionList.orderBy("id asc").findPagedList();
    }
}
```

### Ebeanクエリの解説

- `DB.find(Task.class)`:
  `Task` モデル（`tasks` テーブルに対応）に対するクエリを開始します。
- `.where()`:
  `WHERE` 句の組み立てを開始します。
- `if (term != null && !term.isEmpty()) { ... }`:
  `term` パラメータ（検索キーワード）が指定されている場合のみ、条件を追加します。
- `.icontains("subject", term)`:
  `subject` カラムに `term` の文字列が（大文字小文字を区別せずに）含まれている、という条件 (`LIKE '%term%'`) を追加します。
- `.orderBy("id asc")`:
  `id` カラムの昇順で結果をソートします。
- `.findPagedList()`:
  クエリを実行し、結果を `PagedList` オブジェクトとして取得します。`PagedList` は、総件数やページ情報を含む、ページネーションに適したリストです。

## 2. Controller: APIエンドポイントの作成

次に、`TaskRepository` を使ってタスク一覧を取得し、JSONとして返すAPIコントローラーを作成します。
API用のControllerは `app/controllers/api` に配置します。

`app/controllers/api/TaskController.java` を見てみましょう。

**`app/controllers/api/TaskController.java`**:
```java
// ...
import play.libs.Json;
// ...

public class TaskController extends Controller {
    private final TaskRepository taskRepository;
    // ...

    @Inject
    public TaskController(TaskRepository taskRepository, ...) {
        this.taskRepository = taskRepository;
        // ...
    }

    /**
     * タスクの一覧を取得します。
     * @param request HTTPリクエスト
     * @return タスク一覧のJSON
     */
    public Result list(Http.Request request) {
        // クエリパラメータから検索キーワードを取得
        final String term = request.getQueryString("term");
        // リポジトリを呼び出してデータを取得
        final PagedList<Task> tasks = this.taskRepository.find(term);
        // 結果をJSONに変換して返す
        return ok(Json.toJson(tasks));
    }
}
```

### コードの解説

- `@Inject`:
  `@Inject` は、DI (Dependency Injection) のためのアノテーションです。Play Frameworkが、`TaskController` のインスタンスを生成する際に、自動的に `TaskRepository` のインスタンスを注入してくれます。これにより、Controllerはリポジトリの具体的な生成方法を知らなくても、その機能を利用できます。
- `request.getQueryString("term")`:
  HTTPリクエストのクエリパラメータ（例: `/api/tasks?term=Svelte`）から `term` の値を取得します。
- `this.taskRepository.find(term)`:
  先ほど作成したリポジトリのメソッドを呼び出し、データベースからタスクのリストを取得します。
- `return ok(Json.toJson(tasks))`:
  `Json.toJson()` ヘルパーを使って、`Task` オブジェクトのリストをJSON形式の文字列に変換します。そして、HTTPステータス `200 OK` と共に、そのJSONをレスポンスボディとして返します。

## 3. routesの設定

最後に、このAPIアクションをURLにマッピングします。

**`conf/routes`**:
```
# Tasks API
GET     /api/tasks            controllers.api.TaskController.list(request: Request)
```

この設定により、`GET /api/tasks` というURLへのアクセスが `TaskController.list` アクションにルーティングされるようになります。

これで、データ取得APIの作成は完了です。
`sbt run` を実行した状態で、ブラウザや `curl` コマンドなどで `http://localhost:9000/api/tasks` にアクセスすると、タスクの一覧がJSON形式で返ってくることが確認できます。

# 4-1. データ取得APIの実装 (バックエンド)

データベースの準備が整いました。次はいよいよ、そのデータベースからデータを取得し、フロントエンドに渡すための「API (Application Programming Interface)」を実装します。

APIとは、アプリケーションの機能を外部（この場合はフロントエンドのSvelte）から利用するための窓口や接点のようなものです。今回は、保存されているタスクの一覧をJSON形式で返すAPIを作成します。

APIの実装は、以下の3ステップで進めます。
1.  **Modelの作成**: データベースのテーブルとJavaコードを対応付けます。
2.  **Repository層の作成**: データベースへのアクセス処理をカプセル化します。
3.  **Controller (API) の作成**: リクエストを受け付け、Repositoryを呼び出し、結果をJSONで返します。

## 1. Modelの作成

まず、`11-DBマイグレーション.md`で作成した `task` テーブルに対応するJavaクラスを作成します。

`app/models/Task.java` というファイルが既に存在するかと思いますが、なければ作成し、以下の内容にしてください。

**`app/models/Task.java`**
```java
package models;

import io.ebean.Finder;
import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;

@Entity
public class Task extends BaseModel {

    @Id
    public Long id;

    @Constraints.Required
    public String title;

    public boolean isDone = false;

    @WhenCreated
    public Instant createdAt;

    @WhenModified
    public Instant updatedAt;

    public static final Finder<Long, Task> find = new Finder<>(Task.class);
}
```
- **`@Entity`**: このクラスがEbeanの管理対象であり、データベースのテーブルに対応することを示します。
- **`@Id`**: このフィールドが主キーであることを示します。
- **`@Constraints.Required`**: このフィールドが必須項目であることを示すアノテーションです。後のバリデーションで使用します。
- **`@WhenCreated`, `@WhenModified`**: レコードの作成時・更新時に、Ebeanが自動的にタイムスタンプを設定してくれます。
- **`Finder`**: Ebeanでデータを検索するためのヘルパーオブジェクトです。`Task.find.all()` のように、静的メソッドのようにしてクエリを組み立てることができます。

## 2. Repository層の作成

次に、データベースへの具体的なアクセス処理を担当するRepositoryクラスを作成します。ビジネスロジック（Controller）からDB操作の詳細を分離することで、コードの見通しが良くなり、テストもしやすくなります。

`app/repository/TaskRepository.java` というファイルが既に存在するかと思いますが、なければ作成し、以下の内容にしてください。

**`app/repository/TaskRepository.java`**
```java
package repository;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import models.Task;
import play.db.ebean.EbeanConfig;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class TaskRepository {

    private final EbeanServer ebeanServer;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public TaskRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
    }

    /**
     * 全てのタスクを取得します。
     */
    public CompletionStage<List<Task>> all() {
        return supplyAsync(() ->
            ebeanServer.find(Task.class).orderBy("id").findList(),
            executionContext
        );
    }
}
```
- **`@Inject`**: Play FrameworkのDI（Dependency Injection）機能により、必要なクラス（`EbeanConfig`など）のインスタンスが自動的にコンストラクタに渡されます。
- **`ebeanServer`**: データベース操作の起点となるEbeanのメインオブジェクトです。
- **`supplyAsync(...)`**: データベースへのアクセスは時間がかかる可能性があるため、非同期処理で実行します。これにより、Play FrameworkはDBアクセス中に他のリクエストを処理でき、アプリケーション全体のスループットが向上します。
- **`ebeanServer.find(Task.class)...findList()`**: Ebeanを使って `task` テーブルから全件のデータを取得し、`Task` オブジェクトのリストとして返すクエリです。

## 3. Controller (API) の作成

Repositoryができたので、これを使って実際にAPIのエンドポイントを作成します。API用のコントローラーは `app/controllers/api` ディレクトリに配置します。

`app/controllers/api/TaskController.java` というファイルが既に存在するかと思いますが、なければ作成し、以下の内容にしてください。

**`app/controllers/api/TaskController.java`**
```java
package controllers.api;

import models.Task;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import repository.TaskRepository;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

public class TaskController extends Controller {

    private final TaskRepository taskRepository;

    @Inject
    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public CompletionStage<Result> list() {
        return taskRepository.all().thenApplyAsync(tasks -> {
            return ok(Json.toJson(tasks));
        });
    }
}
```
- **`@Inject`**: 先ほど作成した `TaskRepository` のインスタンスをDIコンテナから注入してもらいます。
- **`taskRepository.all()`**: Repositoryのメソッドを呼び出して、タスクのリストを非同期で取得します。
- **`.thenApplyAsync(...)`**: `all()` メソッドの処理（DBアクセス）が完了した後に実行される処理を記述します。
- **`ok(Json.toJson(tasks))`**:
  - `Json.toJson(tasks)`: 取得した `Task` オブジェクトのリストをJSON形式のデータに変換します。
  - `ok(...)`: 変換したJSONデータをレスポンスボディとし、HTTPステータス 200 OK を持つ `Result` オブジェクトを生成して返します。

## 4. routesの設定

最後に、作成したAPIコントローラーのアクションをURLに紐付けます。

`conf/routes` ファイルを開き、以下の1行を追記してください。

**`conf/routes`**
```
# --- API ---
GET     /api/tasks                  controllers.api.TaskController.list
```
これで、「`/api/tasks` というURLへのGETリクエストが来たら、`controllers.api.TaskController` の `list` メソッドを呼び出す」というルーティングが設定されました。

### 動作確認

`sbt run` を実行し、ブラウザまたはcurlなどのツールで以下のURLにアクセスしてください。

[http://localhost:9000/api/tasks](http://localhost:9000/api/tasks)

現時点ではデータベースにデータがないため、空の配列 `[]` が表示されれば成功です。

もしH2コンソールで手動でデータをいくつか挿入してから再度アクセスすると、挿入したデータがJSON形式で表示されるはずです。

---

これで、フロントエンドが利用できるデータ取得APIが完成しました。次の章では、このようなAPIを設計する際の基本的な考え方である「REST API」について解説します。

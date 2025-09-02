# 4-2. データを更新するAPIの作成

データベースにテーブルを作成したので、次はそのテーブルのデータを操作するAPIを実装します。
この章では、まず既存のお知らせ一覧取得API (`/api/news`) が、ダミーデータではなくデータベースからデータを取得するように修正します。
その後、新しいお知らせを追加するためのAPI (`POST /api/news`) を作成します。

## 1. Modelクラスの作成

まず、`news` テーブルのレコードを表現するためのJavaクラスを作成します。このようなクラスを**Model**と呼びます。
`app/models` ディレクトリに `News.java` というファイルを作成してください。

**`app/models/News.java`**

```java
package models;

import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "news")
public class News extends BaseModel {

    @Id
    public Long id;

    public LocalDate newsDate;

    public String title;

    @WhenCreated
    public Instant whenCreated;

    @WhenModified
    public Instant whenModified;
}
```

### コードの解説
- `@Entity`: このクラスがEbeanによって管理されるエンティティ（データベースのテーブルに対応するオブジェクト）であることを示します。
- `@Table(name = "news")`: このクラスが `news` テーブルに対応することを示します。
- `@Id`: このフィールドが主キーであることを示します。
- `BaseModel`: このリポジトリで定義されている基底クラスで、共通のFinder（検索機能）などを提供します。
- `newsDate`, `title`: テーブルのカラムに対応するフィールドです。Ebeanが自動的にスネークケース (`news_date`) とキャメルケース (`newsDate`) をマッピングしてくれます。
- `@WhenCreated`, `@WhenModified`: レコードの作成時・更新時に自動的にタイムスタンプが設定されるためのアノテーションです。これらを使うには、対応するカラムをEvolutionsで追加する必要がありますが、このチュートリアルでは省略します。

## 2. Repositoryクラスの作成

次に、データベースへの具体的なアクセス処理を記述する**Repository**クラスを作成します。
`app/repository` ディレクトリに `NewsRepository.java` を作成してください。

**`app/repository/NewsRepository.java`**

```java
package repository;

import io.ebean.Ebean;
import io.ebean.EbeanServer;
import models.News;
import play.db.ebean.EbeanConfig;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class NewsRepository {

    private final EbeanServer ebeanServer;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public NewsRepository(EbeanConfig ebeanConfig, DatabaseExecutionContext executionContext) {
        this.ebeanServer = Ebean.getServer(ebeanConfig.defaultServer());
        this.executionContext = executionContext;
    }

    /**
     * お知らせを全件取得します。
     */
    public CompletionStage<List<News>> list() {
        return supplyAsync(() ->
                ebeanServer.find(News.class).orderBy("newsDate desc").findList(), executionContext);
    }

    /**
     * 新しいお知らせを保存します。
     */
    public CompletionStage<Long> insert(News news) {
        return supplyAsync(() -> {
            ebeanServer.insert(news);
            return news.id;
        }, executionContext);
    }
}
```

### コードの解説
- `EbeanServer`: Ebeanの操作を行うための中心的なオブジェクトです。
- `DatabaseExecutionContext`: データベースアクセスのようなブロッキング処理を、Play Frameworkのメインスレッドから切り離して実行するためのスレッドプールです。
- `list()`:
  - `ebeanServer.find(News.class)`: `news` テーブルに対するクエリを開始します。
  - `.orderBy("newsDate desc")`: 日付の降順でソートします。
  - `.findList()`: 結果をリストとして取得します。
  - `supplyAsync(...)`: これらのDBアクセス処理を、`executionContext` 上で非同期に実行します。結果は `CompletionStage<List<News>>` として返されます。
- `insert(News news)`:
  - `ebeanServer.insert(news)`: 引数で受け取った `News` オブジェクトをデータベースに挿入します。
  - 挿入後、自動採番されたIDが `news.id` にセットされるので、それを返します。

## 3. API Controllerの修正と追加

最後に、`app/controllers/api/NewsController.java` を修正して、作成した `NewsRepository` を使うように変更します。

**`app/controllers/api/NewsController.java`**

```java
package controllers.api;

import com.fasterxml.jackson.databind.JsonNode;
import models.News;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repository.NewsRepository;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class NewsController extends Controller {

    private final NewsRepository newsRepository;

    @Inject
    public NewsController(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    /**
     * お知らせ一覧をJSONで返却します。
     */
    public CompletionStage<Result> list() {
        return newsRepository.list().thenApply(newsItems -> {
            final JsonNode json = Json.toJson(newsItems);
            return ok(json);
        });
    }

    /**
     * 新しいお知らせを追加します。
     */
    public CompletionStage<Result> add(Http.Request request) {
        // リクエストボディをNewsオブジェクトに変換
        final News news = Json.fromJson(request.body().asJson(), News.class);

        return newsRepository.insert(news).thenApply(newsId ->
                created(Json.toJson(newsId))
        );
    }
}
```

### コードの解説
- **コンストラクタインジェクション**:
  `NewsRepository` をコンストラクタで受け取るように変更します。PlayのDI機能により、インスタンスが自動的に注入されます。
- **`list()` メソッド**:
  - `newsRepository.list()` を呼び出して、非同期にデータを取得します。
  - `.thenApply(...)`: `CompletionStage` が完了した後（データ取得が終わった後）に実行される処理を記述します。取得した `newsItems` をJSONに変換して返します。
- **`add()` メソッド**:
  - `request.body().asJson()`: リクエストボディをJSONとして取得します。
  - `Json.fromJson(..., News.class)`: JSONデータを `News` クラスのオブジェクトに変換します。
  - `newsRepository.insert(news)` を呼び出して、DBに保存します。
  - `created(...)`: HTTPステータスコード `201 CREATED` を返します。レスポンスボディには、作成されたお知らせのIDをJSONとして含めます。

## 4. ルーティングの追加

`add` アクションを `POST /api/news` にマッピングするため、`conf/routes` を修正します。

**`conf/routes`**

```
# ↓↓↓ この行を修正 ↓↓↓
GET     /api/news                   controllers.api.NewsController.list()
POST    /api/news                   controllers.api.NewsController.add(request: Request)
# ↑↑↑ この行を修正・追加 ↑↑↑
```

## 5. 動作確認

### 一覧取得API
ブラウザで [http://localhost:9000/api/news](http://localhost:9000/api/news) にアクセスしてください。
Evolutionsで投入した初期データがJSONで表示されれば成功です。タイトルから「(APIから取得)」の文言が消えていることを確認してください。

### 追加API
APIをテストするツール（例: Postman, curl, VSCodeのREST Client拡張機能など）を使って、`POST` リクエストを送信します。

- **URL:** `http://localhost:9000/api/news`
- **Method:** `POST`
- **Headers:** `Content-Type: application/json`
- **Body:**
  ```json
  {
    "newsDate": "2025-10-01",
    "title": "新しいお知らせ"
  }
  ```

リクエストを送信し、レスポンスとして新しいID（例: `4`）が返ってくれば成功です。
その後、再度一覧取得APIにアクセスし、追加したデータが含まれていることを確認してください。

---

これで、データベースと連携したデータの読み書きを行うAPIが完成しました。
次の章では、この追加APIを呼び出すためのフォームをSvelteで作成します。

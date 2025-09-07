# 2-2. Play FrameworkのControllerを作成する

前の章で作成したSvelteコンポーネントをブラウザに表示するため、サーバーサイドでHTTPリクエストを受け取り、HTMLを返す仕組みが必要です。Play Frameworkでは、その役割を**Controller**が担います。

## 1. Controllerファイルの作成

`app/controllers/web` ディレクトリに `NewsController.java` という名前で新しいファイルを作成します。

**`app/controllers/web/NewsController.java`**

```java
package controllers.web;

import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;

public class NewsController extends Controller {

    @Inject
    public NewsController() {
    }

    public Result index() {
        // screen.scala.html というテンプレートをレンダリングします。
        // 第1引数にはSvelteコンポーネントのエントリーポイントのパスを指定します。
        return ok(views.html.screen.render("news"));
    }
}
```

### コードの解説
- `public class NewsController extends Controller`
  Play FrameworkのControllerは `play.mvc.Controller` クラスを継承して作成します。
- `@Inject`
  `@Inject` アノテーションは、依存性注入（Dependency Injection）のために必要です。これにより、Play Frameworkがこのクラスのインスタンスを適切に管理してくれます。
- `public Result index()`
  このメソッドが、実際のリクエストを処理する**アクション**です。`Result` 型のオブジェクトを返す必要があり、`ok()` はHTTPステータスコード `200 OK` を表す`Result`を生成します。
- `views.html.screen.render("news")`
  これは、`app/views/screen.scala.html` という**テンプレート**を呼び出してHTMLを生成する処理です。
  - `screen.scala.html` は、このリポジトリでSvelteコンポーネントを読み込むために用意された共通のテンプレートです。
  - 引数の `"news"` は、読み込むべきSvelteのエントリーポイント（`app/assets/svelte/screens/news/index.js`）を指定するためのキーです。このキーを元に、テンプレート側で適切なJavaScriptファイルのパスが解決されます。

## 2. ルーティングの設定

次に、作成した`NewsController`の`index`アクションを、特定のURLにマッピングする必要があります。この設定は `conf/routes` ファイルで行います。

`conf/routes` ファイルを開き、以下の1行を追記してください。

**`conf/routes`**

```
# Tasks
GET     /tasks                      controllers.web.TaskController.index
GET     /tasks/new                  controllers.web.TaskController.add
GET     /tasks/:id                  controllers.web.TaskController.edit(id: Long)

# ↓↓↓ この行を追記 ↓↓↓
GET     /news                       controllers.web.NewsController.index()
# ↑↑↑ この行を追記 ↑↑↑

# Login
GET     /login                      controllers.web.LoginController.index
POST    /login                      controllers.web.LoginController.login
POST    /logout                     controllers.web.LoginController.logout
```

### 設定の解説
- `GET`
  HTTPメソッドを指定します。
- `/news`
  リクエストを受け付けるURLのパスを指定します。
- `controllers.web.NewsController.index()`
  このURLへのリクエストを処理するControllerのクラスとアクションメソッドを指定します。

## 3. 動作確認

ここまでの変更で、`http://localhost:9000/news` にアクセスすると、お知らせ一覧ページが表示されるようになりました。

1.  sbtとnpmのプロセスが両方とも実行中であることを確認してください。
2.  ブラウザで [http://localhost:9000/news](http://localhost:9000/news) にアクセスします。

前の章で作成したSvelteコンポーネントが表示されれば成功です。

> **Note:**
> もし `No static page found` のようなエラーが出る場合は、`npm run hmr` のプロセスが正しく実行されているか、`app/assets/svelte/screens/news/index.js` ファイルが正しく作成されているかを確認してください。

---

これで、Play FrameworkのControllerを使ってSvelteコンポーネントをレンダリングする基本的な流れが完成しました。
しかし、現状ではヘッダーやサイドメニューなどの共通レイアウトが表示されていません。次の章では、このリポジトリに用意されている共通レイアウトコンポーネントを使って、他のページとデザインを統一する方法を学びます。

# 7. Play Controllerとroutesの作成

前のステップでSvelteコンポーネントを作成しましたが、そのコンポーネントをブラウザに表示するためには、バックエンド側で以下の2つの設定が必要です。

1.  **Controllerのアクション**: リクエストを受け取り、SvelteコンポーネントをホストするHTMLページを返す処理。
2.  **routes**: 特定のURLと、作成したControllerのアクションを結びつける設定。

## 1. Controllerの作成

まず、HTTPリクエストを処理するControllerクラスを作成します。
画面を表示するためのControllerは、`app/controllers/web` ディレクトリに作成するのがこのプロジェクトの規約です。

`app/controllers/web` の中に `MyPageController.java` という名前で新しいファイルを作成し、以下の内容を記述してください。

**`app/controllers/web/MyPageController.java`**:
```java
package controllers.web;

import play.mvc.Controller;
import play.mvc.Result;

public class MyPageController extends Controller {

    public Result index() {
        // "my-page" という名前のSvelteスクリーンを描画するようビューに指示
        return ok(views.html.screen.render("my-page"));
    }
}
```

### コードの解説

- `public class MyPageController extends Controller`:
  Play FrameworkのControllerは、`play.mvc.Controller` クラスを継承して作成します。
- `public Result index()`:
  `index` メソッドが、このControllerの「アクション」です。HTTPリクエストを受け取った際に実行される処理単位です。戻り値の `Result` オブジェクトが、ブラウザに返されるレスポンス（HTTPステータスコードやコンテンツ）になります。
- `return ok(...)`:
  `ok()` は、HTTPステータスコード `200 OK` を持つ `Result` を生成するヘルパーメソッドです。
- `views.html.screen.render("my-page")`:
  これがSvelteコンポーネントを呼び出すための重要な部分です。
  - `views.html.screen` は、コンパイルされたビューテンプレート `app/views/screen.scala.html` を指します。
  - `.render("my-page")` は、そのテンプレートをレンダリング（描画）し、引数として `"my-page"` という文字列を渡しています。
  - `screen.scala.html` は、この文字列を受け取り、読み込むべきJavaScriptのパスを `.../screens/my-page/Page.js` のように解決し、HTMLに埋め込んでくれます。

## 2. routesの設定

次に、作成した `MyPageController` の `index` アクションを、特定のURLにマッピングします。この設定は `conf/routes` ファイルで行います。

`conf/routes` ファイルを開き、末尾に以下の1行を追加してください。

**`conf/routes`**:
```
# ... 既存のroutes設定 ...

# My Page
GET     /mypage               controllers.web.MyPageController.index()
```

### routesファイルの書式

`routes` ファイルは、左から順に以下の要素で構成されています。

1.  **HTTPメソッド**: `GET`, `POST`, `PUT`, `DELETE` など。
2.  **URLパス**: `/mypage` のように、アプリケーションのルートからのパスを記述します。
3.  **Controllerのアクション**: `パッケージ名.クラス名.メソッド名()` の形式で、呼び出すアクションを指定します。

この設定により、「`GET` メソッドで `/mypage` というURLにアクセスがあった場合、`controllers.web.MyPageController` クラスの `index` メソッドを実行する」というルールが定義されました。

## 動作確認

ここまで完了したら、`sbt run` と `npm run hmr` が実行されていることを確認し、ブラウザで `http://localhost:9000/mypage` にアクセスしてみてください。

前のステップで作成した「マイページ」の画面が表示されれば成功です。

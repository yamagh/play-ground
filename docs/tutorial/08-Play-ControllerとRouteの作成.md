# 2-3. Play FrameworkのControllerとRouteを作成

SvelteでUIコンポーネントを作成しただけでは、まだブラウザからアクセスすることはできません。特定のURLにアクセスしたときに、そのコンポーネントを描画するようバックエンドに指示する必要があります。その役割を担うのが、Play Frameworkの「Controller」と「Route」です。

## Controllerの作成

Controllerは、ブラウザからのリクエストを受け取り、それに応じた処理（この場合はSvelteコンポーネントを含むHTMLを返す）を行うクラスです。

まず、`app/controllers/web` ディレクトリに `TaskController.java` という名前で新しいファイルを作成してください。

**`app/controllers/web/TaskController.java`**
```java
package controllers.web;

import play.mvc.Controller;
import play.mvc.Result;

public class TaskController extends Controller {

    public Result list() {
        // "tasks/Page.svelte" を描画するようビューに指示
        return ok(views.html.screen.render("tasks/Page.svelte"));
    }
}
```

### コードの解説

- **`package controllers.web;`**:
  このクラスが `controllers.web` パッケージに属することを示します。Play Frameworkでは、ディレクトリ構造とパッケージ名が一致している必要があります。
- **`public class TaskController extends Controller`**:
  Play Frameworkのコントローラー機能を利用するために、`play.mvc.Controller` クラスを継承します。
- **`public Result list()`**:
  このメソッドが、リクエストを処理する「アクション」です。`Result` 型のオブジェクトを返す必要があり、`ok()` はHTTPステータスコード 200 (成功) を持つレスポンスを生成します。
- **`views.html.screen.render("tasks/Page.svelte")`**:
  これがSvelteコンポーネントを描画するための重要な部分です。
  - `views.html.screen` は、`app/views/screen.scala.html` というビューテンプレートファイルを指します。
  - `.render("tasks/Page.svelte")` は、そのテンプレートに対して「`tasks/Page.svelte` というコンポーネントを描画してください」という指示を文字列として渡しています。
  - `ok(...)` は、`render` メソッドが生成したHTMLをレスポンスボディとして、ブラウザに返します。

## routesの設定

Controllerにアクションを作成したら、次に「どのURLにアクセスされたら、どのアクションを呼び出すか」という対応付けを設定する必要があります。この設定を行うのが `conf/routes` ファイルです。

`conf/routes` ファイルを開き、ファイルの末尾に以下の1行を追加してください。

**`conf/routes`**
```
# --- Tasks ---
GET     /tasks                      controllers.web.TaskController.list()
```

### 設定の解説

この1行は、3つの部分で構成されています。

1.  **`GET`**:
    HTTPメソッドを指定します。ブラウザでURLに直接アクセスするのは `GET` リクエストになります。
2.  **`/tasks`**:
    URLのパスを指定します。`http://localhost:9000/tasks` というURLにアクセスされた場合に、この設定がマッチします。
3.  **`controllers.web.TaskController.list()`**:
    呼び出すControllerのクラスとアクションメソッドを完全修飾名で指定します。

これで、「`/tasks` というURLへのGETリクエストが来たら、`TaskController` の `list` メソッドを呼び出す」というルーティングが設定されました。

## 動作確認

`sbt run` と `npm run hmr` の両方が実行されていることを確認してください。
もし `TaskController.java` を追加した後に `sbt run` を再起動していない場合は、一度 `Ctrl+C` で停止し、再度 `sbt run` を実行して変更を反映させてください。（Playのホットリロード機能により、多くの場合自動で反映されます）

ブラウザを開き、以下のURLにアクセスしてください。

[http://localhost:9000/tasks](http://localhost:9000/tasks)

前の章で作成した、カードレイアウトと「新規登録」ボタンが表示されれば成功です！

---

これで、URL、Controller、Svelteコンポーネントが繋がり、静的なページを表示する一連の流れが完成しました。次の章では、アプリケーション全体で共通のヘッダーやサイドメニューといったレイアウトを適用する方法を学びます。

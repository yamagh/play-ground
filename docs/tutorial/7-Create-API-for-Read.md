# 3-1. APIを作成してデータを取得する

現在のお知らせ一覧ページに表示されているデータは、Svelteコンポーネント内に直接書き込まれた静的なものです。
この章では、サーバーサイドにJSON形式でデータを返すAPIを作成し、データベース（の代わりのインメモリデータ）からお知らせ情報を取得できるようにします。

## 1. API用のControllerを作成

まず、APIリクエストを処理するための新しいControllerを `app/controllers/api` ディレクトリに作成します。
`TaskController.java` を参考に、`NewsController.java` というファイル名で作成しましょう。

**`app/controllers/api/NewsController.java`**

```java
package controllers.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

public class NewsController extends Controller {

    @Inject
    public NewsController() {
    }

    /**
     * お知らせ一覧をJSONで返却します。
     */
    public Result list() {
        // 本来はデータベースから取得しますが、今回はダミーデータを作成します。
        final List<ObjectNode> newsItems = Arrays.asList(
                Json.newObject()
                        .put("date", "2025-09-01")
                        .put("title", "システムメンテナンスのお知らせ (APIから取得)"),
                Json.newObject()
                        .put("date", "2025-08-15")
                        .put("title", "新機能のリリースについて (APIから取得)"),
                Json.newObject()
                        .put("date", "2025-07-20")
                        .put("title", "夏季休業のお知らせ (APIから取得)")
        );

        // List<ObjectNode> を JsonNode に変換して返却します。
        final JsonNode json = Json.toJson(newsItems);
        return ok(json);
    }
}
```

### コードの解説
- `package controllers.api;`
  API関連のControllerは `controllers.api` パッケージに配置する、というプロジェクトのルールに従っています。
- `import com.fasterxml.jackson.databind.JsonNode;`
  Play FrameworkはJSONの操作に `Jackson` というライブラリを使用します。`JsonNode` はJSONデータを表現するためのクラスです。
- `Json.newObject().put("key", "value")`
  `ObjectNode` を作成し、キーと値のペアを追加しています。これにより、`{"key": "value"}` のようなJSONオブジェクトをプログラムで組み立てることができます。
- `Json.toJson(newsItems)`
  Javaのリストオブジェクトを `JsonNode` にシリアライズ（変換）します。
- `return ok(json);`
  `ok()` メソッドに `JsonNode` を渡すと、Playは自動的に `Content-Type: application/json` ヘッダーを付けて、JSONレスポンスを返却します。

## 2. APIのルーティングを設定

次に、作成した `list` アクションを `/api/news` というURLにマッピングします。
`conf/routes` ファイルを開き、APIセクションに以下の1行を追記してください。

**`conf/routes`**

```
# APIs
GET     /api/tasks                  controllers.api.TaskController.list
POST    /api/tasks                  controllers.api.TaskController.add
GET     /api/tasks/:id              controllers.api.TaskController.get(id: Long)
PUT     /api/tasks/:id              controllers.api.TaskController.update(id: Long)
DELETE  /api/tasks/:id              controllers.api.TaskController.delete(id: Long)
GET     /api/user/info              controllers.api.UserController.info

# ↓↓↓ この行を追記 ↓↓↓
GET     /api/news                   controllers.api.NewsController.list()
# ↑↑↑ この行を追記 ↑↑↑
```

## 3. 動作確認

sbtが自動的にソースコードの変更を検知し、再コンパイルを行います。
コンパイルが完了したら、ブラウザまたはcurlコマンドでAPIにアクセスしてみましょう。

ブラウザで [http://localhost:9000/api/news](http://localhost:9000/api/news) にアクセスしてください。

以下のようなJSONデータが表示されれば、APIの作成は成功です。

```json
[
  {
    "date": "2025-09-01",
    "title": "システムメンテナンスのお知らせ (APIから取得)"
  },
  {
    "date": "2025-08-15",
    "title": "新機能のリリースについて (APIから取得)"
  },
  {
    "date": "2025-07-20",
    "title": "夏季休業のお知らせ (APIから取得)"
  }
]
```

---

これで、フロントエンドから呼び出すためのデータ取得APIが準備できました。
次の章では、SvelteコンポーネントからこのAPIを呼び出し、取得したデータを使って画面を動的に描画する方法を学びます。

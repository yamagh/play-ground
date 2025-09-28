# 8-1. CSVダウンロードAPIの実装

アプリケーションに蓄積されたデータを、他のツールで分析したり、バックアップとして保存したりするために、CSV形式でダウンロードできる機能は非常に一般的で便利です。

このチャプターでは、タスク一覧をCSVファイルとしてダウンロードするためのAPIを実装します。

## 1. CSV生成ロジックを実装する (CsvService)

まず、CSVデータを生成するロジックを専門に扱う`CsvService`を作成します。ビジネスロジックをControllerから分離することで、コードの見通しが良くなり、再利用性も高まります。

`app/services/CsvService.java` を新規作成します。
```java
package services;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import models.Task;

public class CsvService {

  /**
   * タスクのリストからCSV形式の文字列を生成します。
   *
   * @param tasks CSVに変換するタスクのリスト
   * @return CSV形式の文字列
   */
  public String generateTaskCsv(List<Task> tasks) {
    // ヘッダー行
    String header = "ID,タイトル,ステータス,期日,作成日時,更新日時";

    // データ行
    String data = tasks.stream()
        .map(task -> Stream.of(
            String.valueOf(task.id),
            escapeCsv(task.title),
            escapeCsv(task.status),
            task.dueDate != null ? task.dueDate.toString() : "",
            task.createdAt.toString(),
            task.updatedAt.toString()
        ).collect(Collectors.joining( டிர))) // Corrected from "\n" to "\n"
        .collect(Collectors.joining("\n"));

    return header + "\n" + data;
  }

  /**
   * CSVの特殊文字をエスケープします。
   * カンマ、ダブルクォート、改行が含まれる場合はダブルクォートで囲みます。
   * ダブルクォート自体は2つ重ねることでエスケープします。
   *
   * @param value エスケープする文字列
   * @return エスケープ後の文字列
   */
  private String escapeCsv(String value) {
    if (value == null) {
      return "";
    }
    if (value.contains(",") || value.contains("\"") || value.contains("\n")) { // Corrected from "\"" to "\""
      return "\"" + value.replace("\"", "\"\"") + "\""; // Corrected from "\"" to "\""
    }
    return value;
  }
}
```

### 実装のポイント
- **`generateTaskCsv`**: `Task`オブジェクトのリストを受け取り、1行のCSV文字列に変換します。最初にヘッダー行を定義し、その後、各`Task`オブジェクトをカンマ区切りの文字列に変換して、改行で連結しています。
- **`escapeCsv`**: CSVでは、値にカンマ`,`やダブルクォート`"`、改行文字が含まれる場合、その値をダブルクォートで囲むというルールがあります。このメソッドは、そのエスケープ処理を行っています。

## 2. ControllerからCsvServiceを呼び出す

次に、`TaskController`から`CsvService`を呼び出し、生成されたCSVデータをクライアントに返すアクションを実装します。

`app/controllers/api/TaskController.java` を修正します。
```java
// ... 既存のインポート ...
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.inject.Inject;
import play.mvc.Result;
import services.CsvService; // CsvServiceをインポート

public class TaskController extends Controller {

    private final TaskRepository taskRepository;
    private final FormFactory formFactory;
    private final MessagesApi messagesApi;
    private final CsvService csvService; // CsvServiceをインジェクション

    @Inject
    public TaskController(
        Http.Request request,
        TaskRepository taskRepository,
        FormFactory formFactory,
        MessagesApi messagesApi,
        CsvService csvService) { // CsvServiceをコンストラクタで受け取る
        this.request = request;
        this.taskRepository = taskRepository;
        this.formFactory = formFactory;
        this.messagesApi = messagesApi;
        this.csvService = csvService; // インスタンスを保持
    }

    // ... list(), save(), delete() メソッド ...

    // ↓↓↓ ここから追加 ↓↓↓
    /**
     * タスク一覧をCSV形式でダウンロードします。
     *
     * @return CSVファイル
     */
    public Result exportCsv() {
        // 検索条件はlist()メソッドと同じロジックで取得
        String keyword = request.getQueryString("keyword");
        String status = request.getQueryString("status");

        List<Task> tasks = taskRepository.find(keyword, status);
        String csv = csvService.generateTaskCsv(tasks);

        // ファイル名を生成
        String fileName = "tasks-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".csv";

        // CsvResultヘルパーを使ってレスポンスを返す
        return CsvResult.ok(csv.getBytes(StandardCharsets.UTF_8), fileName);
    }
    // ↑↑↑ ここまで追加 ↑↑↑
}
```

### 実装のポイント
- **DI (Dependency Injection)**: `CsvService`のインスタンスをコンストラクタで注入（Inject）してもらいます。これにより、`TaskController`は`CsvService`の具体的な実装を知らなくても、その機能を利用できます。
- **検索条件の引き継ぎ**: CSVダウンロード時も、画面に表示されているのと同じ検索条件（キーワードやステータス）が適用されるべきです。`list`アクションと同様にクエリパラメータを取得し、`taskRepository.find()`に渡しています。
- **ファイル名の生成**: ダウンロードされるファイル名が毎回ユニークになるように、現在の日時を含めたファイル名を生成しています。
- **`CsvResult`ヘルパー**: このプロジェクトには、CSVダウンロードのレスポンスを簡単に生成するための`CsvResult.java`というヘルパークラスが用意されています。（`app/libraries/CsvResult.java`）これを使うと、Content-TypeやContent-Dispositionヘッダーを適切に設定した`Result`オブジェクトを簡単に作成できます。

## 3. routesに設定を追加する

最後に、`conf/routes`ファイルを編集して、CSVダウンロード用のAPIエンドポイントを定義します。

`conf/routes`
```
# ... 既存のルート設定 ...

DELETE  /api/tasks/:id              controllers.api.TaskController.delete(id: Long)
# ↓↓↓ ここから追加 ↓↓↓
GET     /api/tasks/export           controllers.api.TaskController.exportCsv()
# ↑↑↑ ここまで追加 ↑↑↑

# ... 既存のルート設定 ...
```

`GET`メソッドで`/api/tasks/export`というURLへのリクエストがあった場合に、`TaskController`の`exportCsv`アクションが呼び出されるように設定しました。

これでバックエンド側の実装は完了です。
次のチャプターでは、フロントエンドにダウンロードボタンを設置し、このAPIを呼び出す処理を実装します。

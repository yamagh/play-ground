# 27. CSVダウンロードAPIの作成

Webアプリケーションの機能として、一覧画面のデータをCSV (Comma-Separated Values) 形式のファイルとしてダウンロードしたい、という要求はよくあります。
ここでは、タスク一覧をCSVファイルとしてダウンロードするためのAPIをPlay Frameworkで作成する方法を学びます。

この機能の実装は、主にバックエンド側で行います。ポイントは以下の2点です。

1.  データベースから取得したデータリストを、CSV形式の文字列に変換する。
2.  Controllerから、ブラウザがファイルとしてダウンロードするように特別なHTTPヘッダーを付けてレスポンスを返す。

## 1. Service: CSV文字列の生成

CSVの生成ロジックは、Controllerに直接書くよりも、再利用可能な「サービス」として切り出すのが良い設計です。
このプロジェクトには、`CsvService.java` という、CSV生成を担当するサービスクラスが既に用意されています。

**`app/services/CsvService.java`**:
```java
// ...
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
// ...

public class CsvService {
    private final CsvMapper csvMapper = new CsvMapper();

    /**
     * タスクのリストをCSV文字列に変換します。
     * @param tasks タスクのリスト
     * @return CSV形式の文字列
     */
    public String tasksToCsv(final List<Task> tasks) {
        // CSVのヘッダー行を定義
        final CsvSchema schema = CsvSchema.builder()
            .addColumn("id")
            .addColumn("subject")
            .addColumn("dueDate")
            .addColumn("done")
            .build()
            .withHeader();

        try {
            // オブジェクトのリストをCSV文字列に書き出す
            return this.csvMapper.writer(schema).writeValueAsString(tasks);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
```
- このサービスは、`jackson-dataformat-csv` というライブラリを使って、JavaのオブジェクトリストからCSV文字列への変換を行っています。
- `CsvSchema` でCSVのヘッダーとカラムの順序を定義し、`csvMapper` が `Task` オブジェクトのフィールドを対応するカラムにマッピングしてくれます。

## 2. Controller: CSVレスポンスの返却

次に、`TaskController` にCSVダウンロード用の `downloadCsv` アクションを追加します。
このアクションは、`TaskRepository` から全件のタスクを取得し、`CsvService` を使ってCSV文字列に変換し、ファイルとしてクライアントに返します。

**`app/controllers/api/TaskController.java`**:
```java
// ...
public class TaskController extends Controller {
    private final TaskRepository taskRepository;
    private final CsvService csvService;

    @Inject
    public TaskController(TaskRepository taskRepository, CsvService csvService, ...) {
        this.taskRepository = taskRepository;
        this.csvService = csvService;
        // ...
    }

    /**
     * タスク一覧をCSV形式でダウンロードします。
     * @return CSVファイル
     */
    public Result downloadCsv() {
        // Repositoryから全件取得 (ページネーションなし)
        final List<Task> tasks = this.taskRepository.findAll();
        // Serviceを使ってCSV文字列を生成
        final String csv = this.csvService.tasksToCsv(tasks);

        // Content-Dispositionヘッダーを設定してファイルをダウンロードさせる
        return ok(csv)
            .as("text/csv")
            .withHeader("Content-Disposition", "attachment; filename=\"tasks.csv\"");
    }
}
```

### コードの解説
- `this.taskRepository.findAll()`:
  CSVダウンロードでは通常、全件のデータを出力するため、ページネーションを適用しない全件取得のメソッド（`findAll`）をRepositoryに別途用意して呼び出しています。
- `this.csvService.tasksToCsv(tasks)`:
  `CsvService` を呼び出して、タスクのリストをCSV文字列に変換します。
- `.as("text/csv")`:
  レスポンスの `Content-Type` ヘッダーを `text/csv` に設定します。これにより、ブラウザはこのレスポンスがCSVデータであることを認識します。
- `.withHeader("Content-Disposition", "attachment; filename=\"tasks.csv\"")`:
  これが最も重要な部分です。`Content-Disposition` ヘッダーに `attachment` を指定することで、ブラウザに対して、このレスポンスを画面に表示するのではなく、**添付ファイルとしてダウンロードする**ように指示します。
  `filename="..."` で、ダウンロード時のデフォルトのファイル名を指定できます。

## 3. routesの設定

最後に、このアクションを `conf/routes` に追加します。

**`conf/routes`**:
```
# Tasks API
# ...
GET     /api/tasks/csv        controllers.api.TaskController.downloadCsv()
```

これで、`GET /api/tasks/csv` にアクセスすると、`tasks.csv` という名前でCSVファイルがダウンロードされるAPIが完成しました。

```
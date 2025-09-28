# 9-1. CSVインポートAPIの実装

CSVダウンロードと対になる機能として、CSVファイルを使ってデータを一括登録するインポート機能を実装します。ユーザーはCSVファイルを用意してアップロードするだけで、複数のタスクを一度に作成できるようになります。

## 1. ファイルアップロード処理のハンドラを利用する

Play Frameworkでファイルアップロードを処理するには、`multipart/form-data`形式のリクエストをパースする必要があります。このプロジェクトでは、CSVアップロードの定型的な処理をまとめた`CsvImportHandler`というヘルパークラスが用意されています。

`app/libraries/CsvImportHandler.java`
```java
// (このファイルは既にプロジェクトに存在します)
public class CsvImportHandler {
    // ... ファイルのチェックやパース処理 ...
    public void handle(Http.Request request, Consumer<List<String[]>> onSuccess, Consumer<String> onError) {
        // ...
    }
}
```
このハンドラは、リクエストを受け取り、ファイルがCSVであるかのチェック、中身のパースまでを行い、成功すればパース結果（`List<String[]>`）を、失敗すればエラーメッセージをコールバック関数経由で返してくれます。

## 2. CSVデータのバリデーションと登録 (CsvService, TaskService)

パースされたCSVデータ（文字列の配列）を、`Task`モデルに変換し、データベースに登録する処理が必要です。このロジックは`CsvService`と`TaskService`に実装します。

まず、`CsvService`にCSVの各行を`Task`オブジェクトに変換し、バリデーションを行うメソッドを追加します。

`app/services/CsvService.java`
```java
// ... 既存のコード ...
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import models.Task;
import play.data.validation.ValidationError;

public class CsvService {
    // ... generateTaskCsv, escapeCsv メソッド ...

    // ↓↓↓ ここから追加 ↓↓↓
    /**
     * CSVデータ（文字列の配列リスト）をTaskオブジェクトのリストに変換し、バリデーションを行います。
     *
     * @param csvData パースされたCSVデータ
     * @param errors バリデーションエラーを格納するリスト
     * @return バリデーション済みのTaskオブジェクトのリスト
     */
    public List<Task> validateAndBuildTasks(List<String[]> csvData, List<String> errors) {
        List<Task> tasks = new ArrayList<>();
        // 1行目はヘッダーなので2行目から処理
        AtomicInteger rowNum = new AtomicInteger(2);

        csvData.stream().skip(1).forEach(row -> {
            if (row.length < 2) { // 最低でもタイトルとステータスは必要
                errors.add(rowNum.get() + "行目: カラム数が不足しています。");
            } else {
                Task task = new Task();
                task.title = row[0];
                task.status = row[1];
                try {
                    if (row.length > 2 && row[2] != null && !row[2].isEmpty()) {
                        task.dueDate = LocalDate.parse(row[2]);
                    }
                } catch (DateTimeParseException e) {
                    errors.add(rowNum.get() + "行目: 期日の日付形式が不正です (例: 2025-12-31)。");
                }

                // Play Frameworkのバリデーターを使ってモデルの制約をチェック
                List<ValidationError> validationErrors = task.validate();
                if (!validationErrors.isEmpty()) {
                    validationErrors.forEach(error -> errors.add(rowNum.get() + "行目: " + error.message()));
                } else {
                    tasks.add(task);
                }
            }
            rowNum.incrementAndGet();
        });
        return tasks;
    }
    // ↑↑↑ ここまで追加 ↑↑↑
}
```
このメソッドは、CSVの各行をループし、`Task`オブジェクトを作成します。日付のフォーマットチェックや、`Task`モデルに定義された`@Constraints`アノテーションによるバリデーションを行い、エラーがあれば`errors`リストに追加します。

次に、`TaskService`に、`Task`のリストをまとめてデータベースに登録するメソッドを追加します。

`app/services/TaskService.java`
```java
// (このファイルは既にプロジェクトに存在します)
// ...
import io.ebean.Ebean;
// ...
public class TaskService {
    // ...
    // ↓↓↓ ここから追加 ↓↓↓
    /**
     * 複数のタスクをまとめてデータベースに保存します（バルクインサート）。
     *
     * @param tasks 保存するタスクのリスト
     */
    public void saveAll(List<Task> tasks) {
        Ebean.saveAll(tasks);
    }
    // ↑↑↑ ここまで追加 ↑↑↑
}
```
`Ebean.saveAll()`を使うと、複数のレコードを効率的に一括でINSERTできます（バルクインサート）。

## 3. Controllerにインポートアクションを追加する

準備が整ったので、`TaskController`にCSVインポートを実行するアクションを追加します。

`app/controllers/api/TaskController.java`
```java
// ... 既存のインポート ...
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import libraries.CsvImportHandler;
import play.libs.Json;
import services.TaskService; // TaskServiceをインポート

public class TaskController extends Controller {
    // ...
    private final CsvService csvService;
    private final TaskService taskService; // TaskServiceをインジェクション
    private final CsvImportHandler csvImportHandler; // CsvImportHandlerをインジェクション

    @Inject
    public TaskController(
        // ...
        CsvService csvService,
        TaskService taskService, // コンストラクタで受け取る
        CsvImportHandler csvImportHandler) {
        // ...
        this.csvService = csvService;
        this.taskService = taskService; // インスタンスを保持
        this.csvImportHandler = csvImportHandler; // インスタンスを保持
    }

    // ... list(), save(), delete(), exportCsv() メソッド ...

    // ↓↓↓ ここから追加 ↓↓↓
    /**
     * アップロードされたCSVファイルをインポートしてタスクを登録します。
     *
     * @return 処理結果を示すJSONレスポンス
     */
    public Result importCsv() {
        ObjectNode result = Json.newObject();
        List<String> errors = new ArrayList<>();

        csvImportHandler.handle(request,
            // onSuccess: ファイルのパースが成功した場合
            csvData -> {
                List<Task> tasks = csvService.validateAndBuildTasks(csvData, errors);
                if (errors.isEmpty()) {
                    taskService.saveAll(tasks);
                    result.put("success", true);
                    result.put("message", tasks.size() + "件のタスクを登録しました。");
                } else {
                    result.put("success", false);
                    ArrayNode errorNodes = result.putArray("errors");
                    errors.forEach(errorNodes::add);
                }
            },
            // onError: ファイルのパースに失敗した場合
            errorMessage -> {
                result.put("success", false);
                ArrayNode errorNodes = result.putArray("errors");
                errorNodes.add(errorMessage);
            }
        );

        if (result.get("success").asBoolean(false)) {
            return ok(result);
        } else {
            return badRequest(result);
        }
    }
    // ↑↑↑ ここまで追加 ↑↑↑
}
```
### 実装のポイント
- **`CsvImportHandler.handle()`**: このメソッドに、成功時の処理と失敗時の処理をラムダ式で渡します。
- **成功時の処理**: `CsvService`でバリデーションと`Task`オブジェクトへの変換を行います。エラーがなければ`TaskService`でDBに一括登録し、成功メッセージをJSONに詰めます。エラーがあれば、エラーメッセージのリストをJSONに詰めます。
- **失敗時の処理**: ファイル形式が不正な場合など、パース自体に失敗した場合は、そのエラーメッセージをJSONに詰めます。
- **レスポンス**: 最終的に処理が成功したかどうかで、HTTPステータス`200 OK`または`400 Bad Request`を返します。

## 4. routesに設定を追加する

最後に、`conf/routes`にエンドポイントを追加します。

`conf/routes`
```
# ... 既存のルート設定 ...

GET     /api/tasks/export           controllers.api.TaskController.exportCsv()
# ↓↓↓ ここから追加 ↓↓↓
POST    /api/tasks/import           controllers.api.TaskController.importCsv()
# ↑↑↑ ここまで追加 ↑↑↑

# ... 既存のルート設定 ...
```

これでバックエンドの実装は完了です。次のチャプターで、このAPIを呼び出すためのUIをフロントエンドに作成します。

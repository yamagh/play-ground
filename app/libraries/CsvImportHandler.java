package libraries;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Files;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static play.mvc.Results.badRequest;

public class CsvImportHandler {

    /**
     * Multipart/form-dataリクエストからCSVファイルを抽出し、指定された処理を実行する。
     *
     * @param request PlayのHTTPリクエスト
     * @param block   抽出されたファイルを使って実行する処理
     * @return 処理結果を示すCompletionStage<Result>
     */
    public static CompletionStage<Result> handle(
        Http.Request request,
        Function<File, CompletionStage<Result>> block
    ) {
        Http.MultipartFormData<Files.TemporaryFile> body = request.body().asMultipartFormData();
        if (body == null) {
            ObjectNode errorJson = Json.newObject();
            errorJson.put("errors", Json.newArray().add("Invalid request body. Expecting multipart/form-data."));
            return CompletableFuture.completedFuture(badRequest(errorJson));
        }

        Http.MultipartFormData.FilePart<Files.TemporaryFile> filePart = body.getFile("csv");
        if (filePart == null) {
            ObjectNode errorJson = Json.newObject();
            errorJson.put("errors", Json.newArray().add("CSV file not found."));
            return CompletableFuture.completedFuture(badRequest(errorJson));
        }

        java.io.File file = filePart.getRef().path().toFile();
        return block.apply(file);
    }
}

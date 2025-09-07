package controllers.api;

import com.fasterxml.jackson.databind.JsonNode;
import models.Task;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.TaskService;
import libraries.CsvResult;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public class TaskController extends Controller {

    private final TaskService taskService;

    @Inject
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    public CompletionStage<Result> find(Http.Request request) {
        int page = request.queryString("page").map(Integer::parseInt).orElse(1);
        int perPage = request.queryString("perPage").map(Integer::parseInt).orElse(10);
        String title = request.queryString("title").orElse(null);
        List<String> statuses = Optional.ofNullable(request.queryString().get("statuses"))
            .map(Arrays::asList)
            .orElse(Collections.emptyList());

        return taskService.find(page, perPage, title, statuses)
            .thenApply(result -> ok(result));
    }

    public CompletionStage<Result> findById(Http.Request request, Long id) {
        return taskService.findById(id).thenApply(taskOpt ->
            taskOpt.map(task -> ok(Json.toJson(task)))
                .orElse(notFound())
        );
    }

    public CompletionStage<Result> create(Http.Request request) {
        JsonNode json = request.body().asJson();
        Task task = Json.fromJson(json, Task.class);
        return taskService.create(task).thenApply(insertedTask ->
            created(Json.toJson(insertedTask))
        );
    }

    public CompletionStage<Result> update(Http.Request request, Long id) {
        JsonNode json = request.body().asJson();
        Task task = Json.fromJson(json, Task.class);
        return taskService.update(id, task).thenApply(taskOpt ->
            taskOpt.map(updatedTask -> ok(Json.toJson(updatedTask)))
                .orElse(notFound())
        );
    }

    public CompletionStage<Result> exportCsv(Http.Request request) {
        String title = request.queryString("title").orElse(null);
        List<String> statuses = Optional.ofNullable(request.queryString().get("statuses"))
            .map(Arrays::asList)
            .orElse(Collections.emptyList());

        return taskService.exportCsv(title, statuses).thenApply(csv ->
            CsvResult.ok(csv, "tasks.csv")
        );
    }
}

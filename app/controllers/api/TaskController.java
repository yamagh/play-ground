package controllers.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import libraries.CsvResult;
import models.Task;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repository.TaskRepository;
import services.CsvService;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TaskController extends Controller {

    private final TaskRepository taskRepository;
    private final CsvService csvService;

    @Inject
    public TaskController(TaskRepository taskRepository, CsvService csvService) {
        this.taskRepository = taskRepository;
        this.csvService = csvService;
    }

    public CompletionStage<Result> find(Http.Request request) {
        int page = request.queryString("page").map(Integer::parseInt).orElse(1);
        int perPage = request.queryString("perPage").map(Integer::parseInt).orElse(10);
        String title = request.queryString("title").orElse(null);
        List<String> statuses = Optional.ofNullable(request.queryString().get("statuses"))
            .map(Arrays::asList)
            .orElse(Collections.emptyList());

        return taskRepository.find(page, perPage, title, statuses).thenApply(pagedList -> {
            ObjectNode result = Json.newObject();
            result.put("total", pagedList.getTotalCount());
            result.set("items", Json.toJson(pagedList.getList().stream().map(task -> {
                ObjectNode node = Json.newObject();
                node.put("id", task.id);
                node.put("title", task.title);
                node.put("status", task.status);
                if (task.owner != null) {
                    node.put("ownerName", task.owner.name);
                } else {
                    node.putNull("ownerName");
                }
                node.put("dueDate", task.dueDate);
                node.put("priority", task.priority);
                if (task.createdOn != null) {
                    node.put("createdOn", task.createdOn.toString());
                } else {
                    node.putNull("createdOn");
                }
                return node;
            }).collect(Collectors.toList())));
            return ok(result);
        });
    }

    public CompletionStage<Result> findById(Http.Request request, Long id) {
        return taskRepository.findById(id).thenApply(taskOpt ->
            taskOpt.map(task -> ok(Json.toJson(task)))
                .orElse(notFound())
        );
    }

    public CompletionStage<Result> create(Http.Request request) {
        JsonNode json = request.body().asJson();
        Task task = Json.fromJson(json, Task.class);
        return taskRepository.insert(task).thenApply(insertedTask ->
            created(Json.toJson(insertedTask))
        );
    }

    public CompletionStage<Result> update(Http.Request request, Long id) {
        JsonNode json = request.body().asJson();
        Task task = Json.fromJson(json, Task.class);
        task.id = id;
        return taskRepository.update(task).thenApply(updatedTask ->
            ok(Json.toJson(updatedTask))
        );
    }

    public CompletionStage<Result> exportCsv(Http.Request request) {
        String title = request.queryString("title").orElse(null);
        List<String> statuses = Optional.ofNullable(request.queryString().get("statuses"))
            .map(Arrays::asList)
            .orElse(Collections.emptyList());

        return taskRepository.findAll(title, statuses).thenApply(tasks -> {
            List<String> headers = Arrays.asList("ID", "Title", "Status", "Owner", "Due Date", "Priority", "Created On");

            Function<Task, List<String>> rowMapper = task -> Arrays.asList(
                String.valueOf(task.id),
                task.title,
                task.status,
                task.owner != null ? task.owner.name : "",
                task.dueDate,
                String.valueOf(task.priority),
                task.createdOn != null ? task.createdOn.toString() : ""
            );

            String csv = csvService.generate(headers, tasks, rowMapper);

            return CsvResult.ok(csv, "tasks.csv");
        });
    }
}

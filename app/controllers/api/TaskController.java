package controllers.api;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Task;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repository.TaskRepository;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

public class TaskController extends Controller {

    private final TaskRepository taskRepository;

    @Inject
    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public CompletionStage<Result> find(Http.Request request) {
        int page = request.queryString("page").map(Integer::parseInt).orElse(1);
        int perPage = request.queryString("perPage").map(Integer::parseInt).orElse(10);
        String title = request.queryString("title").orElse(null);
        String description = request.queryString("description").orElse(null);
        List<String> statuses = Optional.ofNullable(request.queryString().get("statuses"))
            .map(Arrays::asList)
            .orElse(Collections.emptyList());

        return taskRepository.find(page, perPage, title, description, statuses).thenApply(pagedList -> {
            ObjectNode result = Json.newObject();
            result.put("total", pagedList.getTotalCount());
            result.set("items", Json.toJson(pagedList.getList().stream().map(task -> {
                ObjectNode node = Json.newObject();
                node.put("id", task.id);
                node.put("title", task.title);
                node.put("description", task.description);
                node.put("status", task.status);
                return node;
            }).collect(Collectors.toList())));
            return ok(result);
        });
    }
}

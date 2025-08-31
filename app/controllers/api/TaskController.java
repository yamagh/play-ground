package controllers.api;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Task;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import repository.TaskRepository;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

public class TaskController extends Controller {

    private final TaskRepository taskRepository;

    @Inject
    public TaskController(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public CompletionStage<Result> find(int page, int perPage) {
        return taskRepository.findPage(page, perPage).thenApply(pagedList -> {
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

package services;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Task;
import play.libs.Json;
import repository.TaskRepository;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TaskService {

    private final TaskRepository taskRepository;
    private final CsvService csvService;

    @Inject
    public TaskService(TaskRepository taskRepository, CsvService csvService) {
        this.taskRepository = taskRepository;
        this.csvService = csvService;
    }

    public CompletionStage<ObjectNode> find(int page, int perPage, String title, List<String> statuses) {
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
            return result;
        });
    }

    public CompletionStage<Optional<Task>> findById(Long id) {
        return taskRepository.findById(id);
    }

    public CompletionStage<Task> create(Task task) {
        return taskRepository.insert(task);
    }

    public CompletionStage<Task> update(Long id, Task task) {
        task.id = id;
        return taskRepository.update(task);
    }

    public CompletionStage<String> exportCsv(String title, List<String> statuses) {
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

            return csvService.generate(headers, tasks, rowMapper);
        });
    }
}

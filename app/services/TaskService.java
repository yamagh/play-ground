package services;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.Task;
import play.libs.Json;
import play.libs.F.Either;
import repository.TaskRepository;

import javax.inject.Inject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
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

    public CompletionStage<ObjectNode> find(int page, int perPage, String title, List<String> statuses, List<Long> ownerIds) {
        return taskRepository.find(page, perPage, title, statuses, ownerIds).thenApply(pagedList -> {
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
        return taskRepository.update(id, task);
    }

    public CompletionStage<Task> delete(Long id) {
        return taskRepository.delete(id);
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

    public CompletionStage<List<String>> findExistingTitles(List<String> titles) {
        return taskRepository.findExistingTitles(titles);
    }

    public CompletionStage<Integer> batchInsert(List<Task> tasks) {
        return taskRepository.batchInsert(tasks);
    }

    public CompletionStage<Either<List<String>, Integer>> importCsv(java.io.File file) {
        // 1. Synchronous file parsing in supplyAsync
        CompletionStage<Either<List<String>, List<Task>>> parsingStage = CompletableFuture.supplyAsync(() -> {
            List<String> errors = new ArrayList<>();
            List<Task> tasksToImport = new ArrayList<>();

            try (BufferedReader br = new BufferedReader(new InputStreamReader(new java.io.FileInputStream(file), "UTF-8"))) {
                String header = br.readLine();
                // Simple validation for BOM
                if (header != null && header.startsWith("\uFEFF")) {
                    header = header.substring(1);
                }

                if (header == null || !"title,status,dueDate,priority".equals(header)) {
                    errors.add("Invalid or empty CSV file. Expected header: title,status,dueDate,priority");
                    return Either.Left(errors);
                }

                Set<String> titlesInFile = new HashSet<>();
                String line;
                int row = 2;
                while ((line = br.readLine()) != null) {
                    String[] values = line.split(",", -1);
                    if (values.length != 4) {
                        errors.add("Row " + row + ": Invalid column count. Expected 4, but got " + values.length + ".");
                        continue; // Skip to next row but collect all errors
                    }
                    String title = values[0];
                    if (title.isEmpty() || title.length() > 255) {
                        errors.add("Row " + row + ": Title is required and must be less than 255 characters.");
                    }
                    if (!titlesInFile.add(title)) {
                        errors.add("Row " + row + ": Duplicate title found in the CSV file: " + title);
                    }

                    Task task = new Task();
                    task.title = title;
                    task.status = values[1];
                    task.dueDate = values[2];
                    try {
                        task.priority = Integer.parseInt(values[3]);
                    } catch (NumberFormatException e) {
                        errors.add("Row " + row + ": Priority must be a number.");
                    }
                    tasksToImport.add(task);
                    row++;
                }
            } catch (java.io.IOException e) {
                errors.add("Error reading CSV file: " + e.getMessage());
                return Either.Left(errors);
            }

            if (!errors.isEmpty()) {
                return Either.Left(errors);
            }

            return Either.Right(tasksToImport);
        });

        // 2. & 3. Asynchronous database operations chained with thenCompose
        return parsingStage.thenCompose(parsingResult -> {
            if (parsingResult.left.isPresent()) {
                return CompletableFuture.completedFuture(Either.Left(parsingResult.left.get()));
            }

            List<Task> tasksToImport = parsingResult.right.get();
            if (tasksToImport.isEmpty()) {
                return CompletableFuture.completedFuture(Either.Right(0));
            }

            List<String> titles = tasksToImport.stream().map(t -> t.title).collect(Collectors.toList());

            return findExistingTitles(titles).thenCompose(existingTitles -> {
                List<String> errors = new ArrayList<>();
                for (String title : existingTitles) {
                    errors.add("Title already exists in the database: " + title);
                }
                if (!errors.isEmpty()) {
                    return CompletableFuture.completedFuture(Either.Left(errors));
                }

                // 4. Batch insert and wrap the result
                return batchInsert(tasksToImport).thenApply(count -> Either.Right(count));
            });
        });
    }
}

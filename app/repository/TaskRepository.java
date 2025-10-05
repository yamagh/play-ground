package repository;

import io.ebean.DB;
import io.ebean.ExpressionList;
import io.ebean.PagedList;
import io.ebean.Query;
import models.Task;

import javax.inject.Inject;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class TaskRepository {
    private final DatabaseExecutionContext executionContext;

    @Inject
    public TaskRepository(DatabaseExecutionContext executionContext) {
        this.executionContext = executionContext;
    }

    public CompletionStage<PagedList<Task>> find(int page, int perPage, String title, List<String> statuses, List<Long> ownerIds) {
        return supplyAsync(() -> {
            Query<Task> query = DB.find(Task.class);
            ExpressionList<Task> where = query.where();
            where.eq("isActive", true);
            if (title != null && !title.isEmpty()) {
                where.ilike("title", "%" + title + "%");
            }
            if (statuses != null && !statuses.isEmpty()) {
                where.in("status", statuses);
            }
            if (ownerIds != null && !ownerIds.isEmpty()) {
                where.in("owner.id", ownerIds);
            }
            return query
                .fetch("owner")
                .setFirstRow((page - 1) * perPage)
                .setMaxRows(perPage)
                .findPagedList();
        }, executionContext);
    }

    public CompletionStage<Optional<Task>> findById(Long id) {
        return supplyAsync(() ->
            DB.find(Task.class)
                .where()
                .eq("id", id)
                .findOneOrEmpty()
        , executionContext);
    }

    public CompletionStage<Task> insert(Task task) {
        return supplyAsync(() -> {
            DB.insert(task);
            return task;
        }, executionContext);
    }

    public CompletionStage<Task> update(Long id, Task newData) {
        return supplyAsync(() -> {
            Task task = DB.find(Task.class).setId(id).findOne();
            if (task == null) {
                throw new EntityNotFoundException("Task not found with id: " + id);
            }
            task.setTitle(newData.getTitle());
            task.setStatus(newData.getStatus());
            task.setDueDate(newData.getDueDate());
            task.setPriority(newData.getPriority());
            task.setOwnerId(newData.getOwnerId());
            task.update();
            return task;
        }, executionContext);
    }

    public CompletionStage<Task> delete(Long id) {
        return supplyAsync(() -> {
            Task task = DB.find(Task.class).setId(id).findOne();
            if (task == null) {
                throw new EntityNotFoundException("Task not found with id: " + id);
            }
            task.setIsActive(false);
            task.update();
            return task;
        }, executionContext);
    }

    public CompletionStage<List<Task>> findAll(String title, List<String> statuses) {
        return supplyAsync(() -> {
            Query<Task> query = DB.find(Task.class);
            ExpressionList<Task> where = query.where();
            where.eq("isActive", true);
            if (title != null && !title.isEmpty()) {
                where.ilike("title", "%" + title + "%");
            }
            if (statuses != null && !statuses.isEmpty()) {
                where.in("status", statuses);
            }
            return query
                .fetch("owner")
                .findList();
        }, executionContext);
    }

    public CompletionStage<List<String>> findExistingTitles(List<String> titles) {
        return supplyAsync(() ->
            DB.find(Task.class)
                .select("title")
                .where()
                .eq("isActive", true)
                .in("title", titles)
                .findList()
                .stream()
                .map(Task::getTitle)
                .collect(Collectors.toList())
        , executionContext);
    }

    public CompletionStage<Integer> batchInsert(List<Task> tasks) {
        return supplyAsync(() -> {
            DB.saveAll(tasks);
            return tasks.size();
        }, executionContext);
    }
}
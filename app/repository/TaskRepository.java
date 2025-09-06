package repository;

import io.ebean.DB;
import io.ebean.ExpressionList;
import io.ebean.PagedList;
import io.ebean.Query;
import models.Task;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class TaskRepository {
    private final DatabaseExecutionContext executionContext;

    @Inject
    public TaskRepository(DatabaseExecutionContext executionContext) {
        this.executionContext = executionContext;
    }

    public CompletionStage<PagedList<Task>> find(int page, int perPage, String title, List<String> statuses) {
        return supplyAsync(() -> {
            Query<Task> query = DB.find(Task.class);
            ExpressionList<Task> where = query.where();
            if (title != null && !title.isEmpty()) {
                where.ilike("title", "%" + title + "%");
            }
            if (statuses != null && !statuses.isEmpty()) {
                where.in("status", statuses);
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

    public CompletionStage<Task> update(Task task) {
        return supplyAsync(() -> {
            DB.update(task);
            return task;
        }, executionContext);
    }

    public CompletionStage<List<Task>> findAll(String title, List<String> statuses) {
        return supplyAsync(() -> {
            Query<Task> query = DB.find(Task.class);
            ExpressionList<Task> where = query.where();
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
}

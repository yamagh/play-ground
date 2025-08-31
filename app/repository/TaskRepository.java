package repository;

import io.ebean.DB;
import io.ebean.ExpressionList;
import io.ebean.PagedList;
import models.Task;

import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class TaskRepository {
    private final DatabaseExecutionContext executionContext;

    @Inject
    public TaskRepository(DatabaseExecutionContext executionContext) {
        this.executionContext = executionContext;
    }

    public CompletionStage<PagedList<Task>> find(int page, int perPage, String title, String description, List<String> statuses) {
        return supplyAsync(() -> {
            ExpressionList<Task> where = DB.find(Task.class).where();
            if (title != null && !title.isEmpty()) {
                where.ilike("title", "%" + title + "%");
            }
            if (description != null && !description.isEmpty()) {
                where.ilike("description", "%" + description + "%");
            }
            if (statuses != null && !statuses.isEmpty()) {
                where.in("status", statuses);
            }
            return where
                .setFirstRow((page - 1) * perPage)
                .setMaxRows(perPage)
                .findPagedList();
        }, executionContext);
    }
}

package repository;

import io.ebean.DB;
import io.ebean.PagedList;
import java.util.concurrent.CompletionStage;
import javax.inject.Inject;
import models.Task;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class TaskRepository {
    private final DatabaseExecutionContext executionContext;

    @Inject
    public TaskRepository(DatabaseExecutionContext executionContext) {
        this.executionContext = executionContext;
    }

    public CompletionStage<PagedList<Task>> findPage(int page, int perPage) {
        return supplyAsync(() -> {
            return DB.find(Task.class)
                .setFirstRow((page - 1) * perPage)
                .setMaxRows(perPage)
                .findPagedList();
        }, executionContext);
    }
}

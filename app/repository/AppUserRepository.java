package repository;

import io.ebean.DB;
import models.AppUser;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

/**
 *
 */
public class AppUserRepository {

    private final DatabaseExecutionContext executionContext;

    @Inject
    public AppUserRepository(DatabaseExecutionContext executionContext) {
        this.executionContext = executionContext;
    }

    public CompletionStage<Map<String, String>> options() {
        return supplyAsync(() -> DB.find(AppUser.class).orderBy("name").findList(), executionContext)
                .thenApply(list -> {
                    HashMap<String, String> options = new LinkedHashMap<String, String>();
                    for (AppUser u : list) {
                        options.put(u.id.toString(), u.name);
                    }
                    return options;
                });
    }

    public CompletionStage<Optional<AppUser>> findByEmail(String email) {
        return supplyAsync(() -> DB.find(AppUser.class).where().eq("email", email).findOneOrEmpty(), executionContext);
    }

    public CompletionStage<Optional<AppUser>> findById(Long id) {
        return supplyAsync(() -> DB.find(AppUser.class).where().eq("id", id).findOneOrEmpty(), executionContext);
    }

}

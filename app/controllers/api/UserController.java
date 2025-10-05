package controllers.api;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.AppUserService;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class UserController extends Controller {

  private final AppUserService appUserService;

  @Inject
  public UserController(AppUserService appUserService) {
    this.appUserService = appUserService;
  }

  public CompletionStage<Result> me(Http.Request request) {
    Optional<String> userId = request.session().get("userId");
    Optional<String> email = request.session().get("email");
    Optional<String> name = request.session().get("name");

    if (userId.isEmpty() || email.isEmpty() || name.isEmpty()) {
      return CompletableFuture.completedFuture(unauthorized());
    }

    ObjectNode result = Json.newObject();
    result.put("userId", userId.get());
    result.put("email", email.get());
    result.put("name", name.get());

    return CompletableFuture.completedFuture(ok(result));
  }

  public CompletionStage<Result> find(Http.Request request) {
    String q = request.queryString("q").orElse("");
    int page = request.queryString("page").map(Integer::parseInt).orElse(1);
    int perPage = request.queryString("perPage").map(Integer::parseInt).orElse(10);
    return appUserService.find(q, page, perPage).thenApply(result ->
        ok(result)
    );
  }
}

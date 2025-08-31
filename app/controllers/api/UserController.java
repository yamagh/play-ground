package controllers.api;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class UserController extends Controller {

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
}

package controllers.api;

import models.AppUser;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import repository.AppUserRepository;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class UserController extends Controller {

  private final AppUserRepository appUserRepository;

  @Inject
  public UserController(AppUserRepository appUserRepository) {
    this.appUserRepository = appUserRepository;
  }

  public CompletionStage<Result> me(Http.Request request) {
    Optional<String> userIdOptional = request.session().get("userId");
    if (userIdOptional.isEmpty()) {
      return CompletableFuture.completedFuture(unauthorized());
    }
    long userId = Long.parseLong(userIdOptional.get());
    return appUserRepository.findById(userId).thenApply(userOptional ->
      userOptional.map(user -> {
        ObjectNode result = Json.newObject();
        result.put("email", user.email);
        return ok(result);
      }).orElse(notFound())
    );
  }
}

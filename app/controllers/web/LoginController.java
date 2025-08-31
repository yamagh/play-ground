package controllers.web;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.AppUser;
import play.data.DynamicForm;
import play.data.FormFactory;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import services.AppUserService;

import javax.inject.Inject;
import java.util.concurrent.CompletionStage;

public class LoginController extends Controller {

  private final AppUserService appUserService;
  private final FormFactory formFactory;

  @Inject
  public LoginController(AppUserService appUserService, FormFactory formFactory) {
    this.appUserService = appUserService;
    this.formFactory = formFactory;
  }

  public Result index(Http.Request request) {
    return ok(views.html.screen.render("login", request));
  }

  public CompletionStage<Result> login(Http.Request request) {
    DynamicForm form = formFactory.form().bindFromRequest(request);
    String email = form.get("email");
    String password = form.get("password");
    return appUserService.authenticate(email, password).thenApply(userOptional -> {
      if (userOptional.isPresent()) {
        AppUser user = userOptional.get();
        ObjectNode result = Json.newObject();
        result.put("success", true);
        result.put("redirectUrl", "/");
        return ok(result)
            .addingToSession(request, "userId", user.id.toString())
            .addingToSession(request, "email", user.email)
            .addingToSession(request, "name", user.name);
      } else {
        ObjectNode result = Json.newObject();
        result.put("success", false);
        return badRequest(result);
      }
    });
  }
}

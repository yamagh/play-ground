package controllers.web;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http;
import views.html.screen;

import javax.inject.Inject;

public class NotFoundController extends Controller {

  @Inject
  public NotFoundController() {}

  public Result index(Http.Request request) {
    return ok(views.html.screen.render("not-found", request));
  }
}

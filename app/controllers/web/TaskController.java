package controllers.web;
 
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Http;
import play.filters.csrf.RequireCSRFCheck;

public class TaskController extends Controller {

  public Result index(Http.Request request) {
    return ok(views.html.screen.render("task", request));
  }
}
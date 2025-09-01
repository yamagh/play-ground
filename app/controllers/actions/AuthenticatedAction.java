package controllers.actions;

import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Results;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class AuthenticatedAction extends Action<Authenticated> {

    @Override
    public CompletionStage<Result> call(Http.Request req) {
        if (req.session().get("userId").isEmpty()) {
            return CompletableFuture.completedFuture(Results.redirect("/unauthorized"));
        }
        return delegate.call(req);
    }
}

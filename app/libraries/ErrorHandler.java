package libraries;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.typesafe.config.Config;
import play.Environment;
import play.api.OptionalSourceMapper;
import play.api.routing.Router;
import play.http.DefaultHttpErrorHandler;
import play.libs.Json;
import play.mvc.Http.RequestHeader;
import play.mvc.Result;
import play.mvc.Results;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Singleton
public class ErrorHandler extends DefaultHttpErrorHandler {

    private final Logger logger = LoggerFactory.getLogger("application.ErrorHandler");

    @Inject
    public ErrorHandler(Config config, Environment env, OptionalSourceMapper sourceMapper, Provider<Router> routes) {
        super(config, env, sourceMapper, routes);
    }

    @Override
    public CompletionStage<Result> onServerError(RequestHeader request, Throwable exception) {
        logger.error("A server error occurred for request: " + request.uri(), exception);

        if (request.path().startsWith("/api/")) {
            ObjectNode result = Json.newObject();
            result.put("message", "予期しないエラーが発生しました。");
            return CompletableFuture.completedFuture(
                Results.internalServerError(result)
            );
        } else {
            // For non-API requests, delegate to the default behavior which renders a nice error page.
            return super.onServerError(request, exception);
        }
    }
}

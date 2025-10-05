package services;

import com.fasterxml.jackson.databind.node.ObjectNode;
import models.AppUser;
import play.libs.Json;
import repository.AppUserRepository;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;

public class AppUserService {

    private final AppUserRepository appUserRepository;

    @Inject
    public AppUserService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    public CompletionStage<Optional<AppUser>> authenticate(String email, String password) {
        return appUserRepository.findByEmail(email).thenApply(userOptional -> {
            if (userOptional.isPresent()) {
                AppUser user = userOptional.get();
                // NOTE: パスワードはハッシュ化して保存・比較するのが望ましい
                if (password.equals(user.password)) {
                    return Optional.of(user);
                }
            }
            return Optional.empty();
        });
    }
    public CompletionStage<ObjectNode> find(String q, int page, int perPage) {
        return appUserRepository.find(q, page, perPage).thenApply(pagedList -> {
            ObjectNode result = Json.newObject();
            result.put("total", pagedList.getTotalCount());
            result.set("items", Json.toJson(pagedList.getList()));
            return result;
        });
    }
}

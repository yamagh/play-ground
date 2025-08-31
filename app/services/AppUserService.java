package services;

import models.AppUser;
import repository.AppUserRepository;

import javax.inject.Inject;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

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
}

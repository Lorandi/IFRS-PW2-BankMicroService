package br.edu.ifrs.user.bootstrap;

import br.edu.ifrs.user.dto.SignupRequestDTO;
import br.edu.ifrs.user.repository.UserRepository;
import br.edu.ifrs.user.service.LoginService;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

@ApplicationScoped
public class StartupDataLoader {

    @Inject
    UserRepository repository;

    @Inject
    LoginService loginService;

    void init(@Observes StartupEvent ev) {

        // Para não duplicar se reiniciar o servidor
        if (repository.count() > 0) {
            return;
        }

        // Cria usuários iniciais usando o fluxo real do sistema
        loginService.signup(new SignupRequestDTO("admin", "a", "11122233344"));
        loginService.signup(new SignupRequestDTO("user1", "a", "22233344455"));
        loginService.signup(new SignupRequestDTO("auditor1", "a", "33344455566"));

        System.out.println("=== Usuários iniciais criados via signup() ===");
    }
}

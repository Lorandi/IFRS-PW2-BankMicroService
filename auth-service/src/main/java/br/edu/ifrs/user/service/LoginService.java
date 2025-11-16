package br.edu.ifrs.user.service;

import br.edu.ifrs.user.dto.*;
import br.edu.ifrs.user.entity.User;
import br.edu.ifrs.user.enums.RoleTypeEnum;
import br.edu.ifrs.user.repository.UserRepository;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Set;

@ApplicationScoped
public class LoginService {

    private static final Logger LOG = Logger.getLogger(LoginService.class);
    private static final String ISSUER = "users-issuer";

    @Inject
    UserRepository repository;

    @Inject
    UserAuditService auditService;

    @Inject
    JsonWebToken jwt;

    // --- CREATE (Signup) ---
    @Transactional
    public User signup(SignupRequestDTO req) {
        String normalizedCpf = normalizeCpf(req.cpf());
        String action = "SIGNUP";

        try {
            if (repository.findByCpf(normalizedCpf) != null)
                throw new IllegalArgumentException("CPF já cadastrado");

            String hashed = BCrypt.hashpw(req.password(), BCrypt.gensalt(12));
            User user = User.builder()
                    .username(req.username())
                    .password(hashed)
                    .cpf(normalizedCpf)
                    .role(RoleTypeEnum.CUSTOMER)
                    .build();

            repository.persist(user);
            auditService.record(action, user.getId(), "SUCCESS", null);

            LOG.infof("AUDIT | %s | userId=%d | result=SUCCESS", action, user.getId());
            return user;

        } catch (Exception e) {
            auditService.record(action, null, "FAILURE", e.getMessage());
            LOG.warnf("AUDIT | %s | userId=null | result=FAILURE | msg=%s", action, e.getMessage());
            throw e;
        }
    }


    @Transactional
    public TokenResponseDTO login(Long userId, String password) {
        String action = "LOGIN";

        try {
            User user = repository.findByIdOptional(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

            if (!BCrypt.checkpw(password, user.getPassword())) {
                auditService.record(action, userId, "FAILURE", "Senha incorreta");
                throw new SecurityException("Senha incorreta");
            }

            String token = Jwt.issuer(ISSUER)
                    .upn(user.getUsername())
                    .groups(Set.of(user.getRole().name()))
                    .claim("userId", user.getId())
                    .sign();

            auditService.record(action, user.getId(), "SUCCESS", null);
            LOG.infof("AUDIT | LOGIN | userId=%d | result=SUCCESS", user.getId());

            return new TokenResponseDTO(token);

        } catch (Exception e) {
            auditService.record(action, userId, "FAILURE", e.getMessage());
            LOG.warnf("AUDIT | LOGIN | userId=%d | result=FAILURE | msg=%s", userId, e.getMessage());
            throw e;
        }
    }

    private String normalizeCpf(String cpf) {
        if (cpf == null) return null;
        return cpf.replaceAll("\\D", "");
    }
}

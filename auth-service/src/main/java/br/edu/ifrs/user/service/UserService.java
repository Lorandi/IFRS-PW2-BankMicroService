package br.edu.ifrs.user.service;

import br.edu.ifrs.user.dto.*;
import br.edu.ifrs.user.entity.User;
import br.edu.ifrs.user.enums.RoleTypeEnum;
import br.edu.ifrs.user.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;
import org.mindrot.jbcrypt.BCrypt;

import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserService {

    private static final Logger LOG = Logger.getLogger(UserService.class);
    private static final String ISSUER = "ifrs-bank";

    @Inject
    UserRepository repository;

    @Inject
    UserAuditService auditService;

    @Inject
    JsonWebToken jwt;


    @Transactional
    public List<UserMinDTO> listAll() {
        List<User> users = repository.listAll();

        auditService.record("LIST_USERS", null, "SUCCESS", null);
        LOG.infof("AUDIT | LIST_USERS | total=%d | result=SUCCESS", users.size());

        return users.stream()
                .map(u -> new UserMinDTO(u.getId(), u.getUsername(), u.getRole()))
                .collect(Collectors.toList());
    }

    // --- READ BY ID ---
    @Transactional
    public UserMinDTO getById(Long id) {
        String action = "GET_BY_ID";
        try {
            User user = repository.findByIdOptional(id)
                    .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

            auditService.record(action, user.getId(), "SUCCESS", null);
            LOG.infof("AUDIT | %s | userId=%d | result=SUCCESS", action, user.getId());

            return new UserMinDTO(user.getId(), user.getUsername(), user.getRole());

        } catch (Exception e) {
            auditService.record(action, id, "FAILURE", e.getMessage());
            LOG.warnf("AUDIT | %s | userId=%d | result=FAILURE | msg=%s", action, id, e.getMessage());
            throw e;
        }
    }

    // --- READ BY USERNAME ---
    @Transactional
    public UserMinDTO getByUsername(String username) {
        String action = "GET_BY_USERNAME";
        try {
            User user = repository.findByUsername(username);
            if (user == null)
                throw new NotFoundException("Usuário não encontrado");

            auditService.record(action, user.getId(), "SUCCESS", null);
            LOG.infof("AUDIT | %s | userId=%d | result=SUCCESS", action, user.getId());

            return new UserMinDTO(user.getId(), user.getUsername(), user.getRole());

        } catch (Exception e) {
            auditService.record(action, null, "FAILURE", e.getMessage());
            LOG.warnf("AUDIT | %s | userId=null | result=FAILURE | msg=%s", action, e.getMessage());
            throw e;
        }
    }

    // --- READ BY CPF ---
    @Transactional
    public UserMinDTO getByCpf(String cpf) {
        String action = "GET_BY_CPF";
        String normalizedCpf = normalizeCpf(cpf);

        try {
            User user = repository.findByCpf(normalizedCpf);
            if (user == null)
                throw new NotFoundException("CPF não encontrado");

            auditService.record(action, user.getId(), "SUCCESS", null);
            LOG.infof("AUDIT | %s | userId=%d | result=SUCCESS", action, user.getId());

            return new UserMinDTO(user.getId(), user.getUsername(), user.getRole());

        } catch (Exception e) {
            auditService.record(action, null, "FAILURE", e.getMessage());
            LOG.warnf("AUDIT | %s | userId=null | result=FAILURE | msg=%s", action, e.getMessage());
            throw e;
        }
    }

    // --- UPDATE ---
    @Transactional
    public User update(Long id, UserUpdateDTO req) {
        String action = "UPDATE_USER";

        try {
            User user = repository.findByIdOptional(id)
                    .orElseThrow(() -> new NotFoundException("Usuário não encontrado para atualização"));

            if (req.username() != null && !req.username().isBlank())
                user.setUsername(req.username());
            if (req.cpf() != null && !req.cpf().isBlank())
                user.setCpf(normalizeCpf(req.cpf()));

            repository.persist(user);
            auditService.record(action, id, "SUCCESS", null);
            LOG.infof("AUDIT | %s | userId=%d | result=SUCCESS", action, id);
            return user;

        } catch (Exception e) {
            auditService.record(action, id, "FAILURE", e.getMessage());
            LOG.warnf("AUDIT | %s | userId=%d | result=FAILURE | msg=%s", action, id, e.getMessage());
            throw e;
        }
    }

    // --- DELETE ---
    @Transactional
    public void delete(Long id) {
        String action = "DELETE_USER";

        try {
            boolean deleted = repository.deleteById(id);
            if (!deleted)
                throw new NotFoundException("Usuário não encontrado para exclusão");

            auditService.record(action, id, "SUCCESS", null);
            LOG.infof("AUDIT | %s | userId=%d | result=SUCCESS", action, id);

        } catch (Exception e) {
            auditService.record(action, id, "FAILURE", e.getMessage());
            LOG.warnf("AUDIT | %s | userId=%d | result=FAILURE | msg=%s", action, id, e.getMessage());
            throw e;
        }
    }

    // --- CHANGE PASSWORD ---
    @Transactional
    public void changePassword(ChangePasswordDTO dto) {
        String action = "CHANGE_PASSWORD";

        try {
            User user = repository.findByIdOptional(dto.userId())
                    .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

            if (!BCrypt.checkpw(dto.oldPassword(), user.getPassword())) {
                auditService.record(action, user.getId(), "FAILURE", "Senha antiga incorreta");
                throw new SecurityException("Senha antiga incorreta");
            }

            if (BCrypt.checkpw(dto.newPassword(), user.getPassword())) {
                auditService.record(action, user.getId(), "FAILURE", "Nova senha igual à anterior");
                throw new IllegalArgumentException("Nova senha não pode ser igual à anterior");
            }

            String hashed = BCrypt.hashpw(dto.newPassword(), BCrypt.gensalt(12));
            user.setPassword(hashed);
            repository.persist(user);

            auditService.record(action, user.getId(), "SUCCESS", null);
            LOG.infof("AUDIT | %s | userId=%d | result=SUCCESS", action, user.getId());

        } catch (Exception e) {
            auditService.record(action, dto.userId(), "FAILURE", e.getMessage());
            LOG.warnf("AUDIT | %s | userId=%d | result=FAILURE | msg=%s", action, dto.userId(), e.getMessage());
            throw e;
        }
    }

    @Transactional
    public void changeRole(Long userId, RoleTypeEnum newRole) {
        String action = "CHANGE_ROLE";

        try {
            User user = repository.findByIdOptional(userId)
                    .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

            if (newRole == null) {
                auditService.record(action, userId, "FAILURE", "Role inválida ou ausente");
                throw new IllegalArgumentException("Role inválida ou ausente");
            }

            if (user.getRole() == newRole) {
                auditService.record(action, userId, "FAILURE", "Usuário já possui essa role");
                throw new IllegalArgumentException("Usuário já possui essa role");
            }

            user.setRole(newRole);
            repository.persist(user);

            auditService.record(action, userId, "SUCCESS", null);
            LOG.infof("AUDIT | %s | userId=%d | newRole=%s | result=SUCCESS",
                    action, userId, newRole);

        } catch (Exception e) {
            auditService.record(action, userId, "FAILURE", e.getMessage());
            LOG.warnf("AUDIT | %s | userId=%d | result=FAILURE | msg=%s",
                    action, userId, e.getMessage());
            throw e;
        }
    }


    @Transactional
    public void changePasswordWithAuth(ChangePasswordDTO dto) {
        String action = "CHANGE_PASSWORD";

        try {
            Long tokenUserId = Long.parseLong(jwt.getClaim("userId").toString());

            if (!tokenUserId.equals(dto.userId())) {
                auditService.record(action, dto.userId(), "FAILURE", "Acesso negado");
                throw new SecurityException("Acesso negado — o token de autenticação não pertence ao usuário informado.");
            }

            User user = repository.findByIdOptional(dto.userId())
                    .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado"));

            if (!BCrypt.checkpw(dto.oldPassword(), user.getPassword())) {
                auditService.record(action, user.getId(), "FAILURE", "Senha antiga incorreta");
                throw new SecurityException("Senha antiga incorreta");
            }

            if (BCrypt.checkpw(dto.newPassword(), user.getPassword())) {
                auditService.record(action, user.getId(), "FAILURE", "Nova senha igual à anterior");
                throw new IllegalArgumentException("Nova senha não pode ser igual à anterior");
            }

            user.setPassword(BCrypt.hashpw(dto.newPassword(), BCrypt.gensalt(12)));
            repository.persistAndFlush(user);

            auditService.record(action, user.getId(), "SUCCESS", null);
            LOG.infof("AUDIT | %s | userId=%d | result=SUCCESS", action, user.getId());

        } catch (Exception e) {
            auditService.record(action, dto.userId(), "FAILURE", e.getMessage());
            LOG.warnf("AUDIT | %s | userId=%d | result=FAILURE | msg=%s", action, dto.userId(), e.getMessage());
            throw e;
        }
    }


    // --- UTIL ---
    private String normalizeCpf(String cpf) {
        if (cpf == null) return null;
        return cpf.replaceAll("\\D", "");
    }
}

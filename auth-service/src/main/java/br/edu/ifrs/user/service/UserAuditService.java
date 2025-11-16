package br.edu.ifrs.user.service;
import br.edu.ifrs.user.dto.AdminAuditUserDTO;
import br.edu.ifrs.user.entity.UserAudit;
import br.edu.ifrs.user.repository.AdminAuditUserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class UserAuditService {

    private static final Logger LOG = Logger.getLogger(UserAuditService.class);

    @Inject
    AdminAuditUserRepository repository;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void record(String action, Long userId, String result, String errorMessage) {

        String operationId = UUID.randomUUID().toString();

        try {
            UserAudit audit = UserAudit.builder()
                    .operationId(operationId)
                    .userId(userId)
                    .action(action)
                    .result(result)
                    .timestamp(LocalDateTime.now())
                    .errorMessage(errorMessage)
                    .build();

            repository.persist(audit);

            LOG.infof("AUDIT | opId=%s | %s | result=%s", operationId, action, result);
        } catch (Exception e) {
            LOG.warnf("AUDIT-FAIL | opId=%s | %s | error=%s", operationId, action, e.getMessage());
        }
    }

    public List<AdminAuditUserDTO> listAll() {
        List<UserAudit> entityList = repository.listAll();
        return getAdminAuditUserDTOS(entityList);
    }

    public List<AdminAuditUserDTO> recent(int limit) {
        List<UserAudit> entityList = repository.find("order by timestamp desc").page(0, limit).list();
        return getAdminAuditUserDTOS(entityList);

    }

    public List<AdminAuditUserDTO> userId(Long userId) {
        List<UserAudit> entityList = repository.list("userId", userId);
        return getAdminAuditUserDTOS(entityList);
    }

    private static List<AdminAuditUserDTO> getAdminAuditUserDTOS(List<UserAudit> entityList) {
        return entityList.stream()
                .map(e -> new AdminAuditUserDTO(
                        e.getId(),
                        e.getOperationId(),
                        e.getAction(),
                        e.getUserId(),
                        e.getResult(),
                        e.getTimestamp(),
                        e.getErrorMessage()
                ))
                .toList();
    }

}

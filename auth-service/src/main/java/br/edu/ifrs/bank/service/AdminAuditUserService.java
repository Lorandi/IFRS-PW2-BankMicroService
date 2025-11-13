package br.edu.ifrs.bank.service;
import br.edu.ifrs.bank.entity.AdminAuditUser;
import br.edu.ifrs.bank.repository.AdminAuditUserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class AdminAuditUserService {

    private static final Logger LOG = Logger.getLogger(AdminAuditUserService.class);

    @Inject
    AdminAuditUserRepository repository;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void record(String action, Long userId, String result, String errorMessage) {

        String operationId = UUID.randomUUID().toString();

        try {
            AdminAuditUser audit = AdminAuditUser.builder()
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

    public List<AdminAuditUser> listAll() {
        return repository.listAll();
    }

    public List<AdminAuditUser> recent(int limit) {
        return repository.find("order by timestamp desc").page(0, limit).list();
    }

    public List<AdminAuditUser> byOwner(Long ownerId) {
        return repository.list("ownerId", ownerId);
    }

}

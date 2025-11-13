package br.edu.ifrs.bank.service;
import br.edu.ifrs.bank.entity.AdminAudit;
import br.edu.ifrs.bank.repository.AdminAuditRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class AdminAuditService {

    private static final Logger LOG = Logger.getLogger(AdminAuditService.class);

    @Inject
    AdminAuditRepository repository;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void record(String action, Long ownerId, String sourceAccountId, String targetAccountId,
                       BigDecimal amount, String result, String errorMessage) {

        String operationId = UUID.randomUUID().toString();

        try {
            AdminAudit audit = AdminAudit.builder()
                    .operationId(operationId)
                    .action(action)
                    .ownerId(ownerId)
                    .sourceAccountId(sourceAccountId)
                    .targetAccountId(targetAccountId)
                    .amount(amount)
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

    public List<AdminAudit> listAll() {
        return repository.listAll();
    }

    public List<AdminAudit> recent(int limit) {
        return repository.find("order by timestamp desc").page(0, limit).list();
    }

    public List<AdminAudit> byOwner(Long ownerId) {
        return repository.list("ownerId", ownerId);
    }

    public List<AdminAudit> byAccountId(String accountId) {
        return repository.list("sourceAccountId = ?1 or targetAccountId = ?1", accountId);
    }
}

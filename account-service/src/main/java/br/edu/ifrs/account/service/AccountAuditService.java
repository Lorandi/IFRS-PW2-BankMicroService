package br.edu.ifrs.account.service;
import br.edu.ifrs.account.dto.AccountAuditDTO;
import br.edu.ifrs.account.entity.AccountAudit;
import br.edu.ifrs.account.repository.AccountAuditRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@ApplicationScoped
public class AccountAuditService {

    private static final Logger LOG = Logger.getLogger(AccountAuditService.class);

    @Inject
    AccountAuditRepository repository;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public void record(String action, Long ownerId, String sourceAccountId, String targetAccountId,
                       BigDecimal amount, String result, String errorMessage) {

        String operationId = UUID.randomUUID().toString();

        try {
            AccountAudit audit = AccountAudit.builder()
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

    public List<AccountAuditDTO> listAll() {
        return getAccountAuditDTOS(repository.listAll());
    }

    public List<AccountAuditDTO> recent(int limit) {
        return getAccountAuditDTOS(repository.find("order by timestamp desc").page(0, limit).list());
    }

    public List<AccountAuditDTO> byOwner(Long ownerId) {

        return getAccountAuditDTOS( repository.list("ownerId", ownerId));
    }

    public List<AccountAuditDTO> byAccountId(String accountId) {
        return getAccountAuditDTOS(repository.list("sourceAccountId = ?1 or targetAccountId = ?1", accountId));

    }

    private static List<AccountAuditDTO> getAccountAuditDTOS(List<AccountAudit> entityList) {
        return entityList.stream()
                .map(e -> new AccountAuditDTO(
                        e.getId(),
                        e.getOperationId(),
                        e.getAction(),
                        e.getOwnerId(),
                        e.getSourceAccountId(),
                        e.getTargetAccountId(),
                        e.getAmount(),
                        e.getResult(),
                        e.getTimestamp(),
                        e.getErrorMessage()
                ))
                .toList();
    }
}

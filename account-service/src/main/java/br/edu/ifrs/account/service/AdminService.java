package br.edu.ifrs.account.service;

import br.edu.ifrs.account.entity.Account;
import br.edu.ifrs.account.repository.AccountRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@ApplicationScoped
public class AdminService {

    @Inject
    AccountRepository repository;

    @Inject
    AuditService audit;

    public List<Account> listAll() {
        return repository.listAll();
    }

    public Account getByAccountId(String accountId) {
        return findByAccountIdOrThrow(accountId);
    }

    public Account getByOwnerId(Long ownerId) {
        return repository.find("ownerId", ownerId)
                .firstResultOptional()
                .orElseThrow(() -> new IllegalArgumentException("Nenhuma conta encontrada para o usuário " + ownerId));
    }


    @Transactional
    public Account toggleStatus(String accountId) {
        try {
            Account account = findByAccountIdOrThrow(accountId);
            account.setStatus(account.getStatus().toggle());
            repository.persist(account);

            audit.record("TOGGLE_STATUS", account.getOwnerId(), null, accountId, null, "SUCCESS", null);
            return account;

        } catch (Exception e) {
            audit.record("TOGGLE_STATUS", null, null, accountId, null, "FAILURE", e.getMessage());
            throw e;
        }
    }

    private Account findByAccountIdOrThrow(String accountId) {
        return repository.find("accountId", accountId)
                .firstResultOptional()
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada: " + accountId));
    }
}

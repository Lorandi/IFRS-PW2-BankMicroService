package br.edu.ifrs.bank.service;

import br.edu.ifrs.bank.dto.AccountRequestDTO;
import br.edu.ifrs.bank.entity.Account;
import br.edu.ifrs.bank.enums.AccountStatusEnum;
import br.edu.ifrs.bank.repository.AccountRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;

@ApplicationScoped
public class AccountService {

    @Inject
    AccountRepository repository;

    @Inject
    AdminAuditService audit;

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
    public Account create(AccountRequestDTO request) {
        try {
            verificaUsuarioSemConta(request.ownerId());

            Account account = Account.builder()
                    .accountId("CC_" + request.ownerId())
                    .balance(BigDecimal.ZERO)
                    .status(AccountStatusEnum.BLOCKED)
                    .ownerId(request.ownerId())
                    .build();

            repository.persist(account);

            audit.record("CREATE_ACCOUNT", request.ownerId(), null, account.getAccountId(), null, "SUCCESS", null);
            return account;

        } catch (Exception e) {
            audit.record("CREATE_ACCOUNT", request.ownerId(), null, null, null, "FAILURE", e.getMessage());
            throw e;
        }
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

    @Transactional
    public void deposit(String accountId, BigDecimal amount) {
        try {
            Account account = findByAccountIdOrThrow(accountId);
            verificaValorPositivo(amount);
            verificaContaAtiva(account);

            account.setBalance(account.getBalance().add(amount));
            repository.persist(account);

            audit.record("DEPOSIT", account.getOwnerId(), null, accountId, amount, "SUCCESS", null);

        } catch (Exception e) {
            audit.record("DEPOSIT", null, null, accountId, amount, "FAILURE", e.getMessage());
            throw e;
        }
    }

    @Transactional
    public Account withdraw(String accountId, Long ownerId, BigDecimal amount) {
        try {
            verificaValorPositivo(amount);
            Account account = findByAccountIdOrThrow(accountId);
            verificaProprietario(ownerId, account);
            verificaContaAtiva(account);
            verificaSaldoSuficiente(amount, account);

            account.setBalance(account.getBalance().subtract(amount));
            repository.persist(account);

            audit.record("WITHDRAW", ownerId, null, accountId, amount, "SUCCESS", null);
            return account;

        } catch (Exception e) {
            audit.record("WITHDRAW", ownerId, null, accountId, amount, "FAILURE", e.getMessage());
            throw e;
        }
    }

    @Transactional
    public void transfer(String sourceAccountId, Long ownerId, String targetAccountId, BigDecimal amount) {
        try {
            verificaValorPositivo(amount);
            verificaContasDiferentes(sourceAccountId, targetAccountId);

            Account source = findByAccountIdOrThrow(sourceAccountId);
            verificaProprietario(ownerId, source);
            verificaContaAtiva(source);
            verificaSaldoSuficiente(amount, source);

            Account target = findByAccountIdOrThrow(targetAccountId);
            verificaContaAtiva(target);

            source.setBalance(source.getBalance().subtract(amount));
            target.setBalance(target.getBalance().add(amount));

            repository.persist(source);
            repository.persist(target);

            audit.record("TRANSFER", ownerId, sourceAccountId, targetAccountId, amount, "SUCCESS", null);

        } catch (Exception e) {
            audit.record("TRANSFER", ownerId, sourceAccountId, targetAccountId, amount, "FAILURE", e.getMessage());
            throw e;
        }
    }

    private Account findByAccountIdOrThrow(String accountId) {
        return repository.find("accountId", accountId)
                .firstResultOptional()
                .orElseThrow(() -> new IllegalArgumentException("Conta não encontrada: " + accountId));
    }

    private void verificaUsuarioSemConta(Long ownerId) {
        boolean existe = repository.find("ownerId", ownerId).firstResultOptional().isPresent();
        if (existe)
            throw new IllegalStateException("Usuário " + ownerId + " já possui uma conta.");
    }

    private static void verificaValorPositivo(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("O valor deve ser maior que zero.");
    }

    private static void verificaProprietario(Long ownerId, Account account) {
        if (!account.getOwnerId().equals(ownerId))
            throw new IllegalArgumentException("Usuário não é dono da conta " + account.getAccountId());
    }

    private static void verificaContaAtiva(Account account) {
        if (account.getStatus().isBlocked())
            throw new IllegalStateException("Conta bloqueada: " + account.getAccountId());
    }

    private static void verificaSaldoSuficiente(BigDecimal amount, Account account) {
        if (account.getBalance().compareTo(amount) < 0)
            throw new IllegalStateException("Saldo insuficiente.");
    }

    private static void verificaContasDiferentes(String source, String target) {
        if (source.equals(target))
            throw new IllegalArgumentException("Conta de origem e destino não podem ser iguais.");
    }
}

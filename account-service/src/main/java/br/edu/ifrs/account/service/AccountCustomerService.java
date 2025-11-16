package br.edu.ifrs.account.service;

import br.edu.ifrs.account.dto.AccountDTO;
import br.edu.ifrs.account.entity.Account;
import br.edu.ifrs.account.enums.AccountStatusEnum;
import br.edu.ifrs.account.repository.AccountRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.math.BigDecimal;

@ApplicationScoped
public class AccountCustomerService {

    @Inject
    AccountRepository repository;

    @Inject
    AccountAuditService audit;

    @Inject
    JsonWebToken jwt;

    public AccountDTO getByOwnerId() {
        Long userId = getUserIdFromToken(jwt);
        Account account =  repository.find("ownerId", userId)
                .firstResultOptional()
                .orElseThrow(() -> new IllegalArgumentException("Nenhuma conta encontrada para o usuário " + userId));
        return new AccountDTO(
                account.getId(),
                account.getAccountId(),
                account.getBalance(),
                account.getStatus().name(),
                account.getOwnerId()
        );

    }

    @Transactional
    public void create() {
        try {
            Long userId = getUserIdFromToken(jwt);
            checkCustomerPermission(jwt);
            checkTokenComUserId(userId);
            verificaUsuarioSemConta(userId);
            Account account = Account.builder()
                    .accountId("CC_" + userId)
                    .balance(BigDecimal.ZERO)
                    .status(AccountStatusEnum.BLOCKED)
                    .ownerId(userId)
                    .build();

            repository.persist(account);
            audit.record("CREATE_ACCOUNT", userId, null,
                    account.getAccountId(), null, "SUCCESS", null);

        } catch (Exception e) {
            audit.record("CREATE_ACCOUNT", null, null,
                    null, null, "FAILURE", e.getMessage());

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
    public void withdraw(String accountId, BigDecimal amount) {
        Long userId = getUserIdFromToken(jwt);
        try {
            verificaValorPositivo(amount);
            Account account = findByAccountIdOrThrow(accountId);
            verificaProprietario(userId, account);
            verificaContaAtiva(account);
            verificaSaldoSuficiente(amount, account);

            account.setBalance(account.getBalance().subtract(amount));
            repository.persist(account);

            audit.record("WITHDRAW", userId, null, accountId, amount, "SUCCESS", null);

        } catch (Exception e) {
            audit.record("WITHDRAW", userId, null, accountId, amount, "FAILURE", e.getMessage());
            throw e;
        }
    }

    @Transactional
    public void transfer(String sourceAccountId, String targetAccountId, BigDecimal amount) {
        Long userId = getUserIdFromToken(jwt);
        try {
            verificaValorPositivo(amount);
            verificaContasDiferentes(sourceAccountId, targetAccountId);

            Account source = findByAccountIdOrThrow(sourceAccountId);
            verificaProprietario(userId, source);
            verificaContaAtiva(source);
            verificaSaldoSuficiente(amount, source);

            Account target = findByAccountIdOrThrow(targetAccountId);
            verificaContaAtiva(target);

            source.setBalance(source.getBalance().subtract(amount));
            target.setBalance(target.getBalance().add(amount));

            repository.persist(source);
            repository.persist(target);

            audit.record("TRANSFER", userId, sourceAccountId, targetAccountId, amount, "SUCCESS", null);

        } catch (Exception e) {
            audit.record("TRANSFER", userId, sourceAccountId, targetAccountId, amount, "FAILURE", e.getMessage());
            throw e;
        }
    }

    private static Long getUserIdFromToken(JsonWebToken jwt) {
        Long userId;
        Object claim = jwt.getClaim("userId");
        if (claim == null) {
            throw new IllegalStateException("Token não contém o claim 'userId'.");
        }
        userId = claim.toString().isEmpty() ? null : Long.parseLong(claim.toString());
        return userId;
    }

    private static void checkTokenComUserId(Long userId) {
        if (userId == null) {
            throw new IllegalStateException("Token não contém 'userId'.");
        }
    }

    private static void checkCustomerPermission(JsonWebToken jwt) {
        if (!jwt.getGroups().contains("CUSTOMER")) {
            throw new IllegalStateException("Usuário não possui permissão CUSTOMER.");
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
            throw new IllegalStateException("Usuário id " + ownerId + " já possui uma conta.");
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

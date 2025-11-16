package br.edu.ifrs.orch.service;

import br.edu.ifrs.orch.client.AccountClient;
import br.edu.ifrs.orch.dto.AccountDTO;
import br.edu.ifrs.orch.dto.AccountTransferDTO;
import br.edu.ifrs.orch.exception.ErrorHandler;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.math.BigDecimal;

@ApplicationScoped
public class AccountCustomerService {
    @Inject
    @RestClient
    AccountClient client;

    public void create() {
        try {
            client.create();
        } catch (Exception ex) {
            throw ErrorHandler.rethrow(ex);
        }
    }

    public AccountDTO getByOwnerId() {
        try {
            return client.getByOwnerId();
        } catch (Exception ex) {
            throw ErrorHandler.rethrow(ex);
        }
    }

    public void deposit(String accountId, BigDecimal amount) {
        try {
            client.deposit(accountId, amount);
        } catch (Exception ex) {
            throw ErrorHandler.rethrow(ex);
        }
    }

    public void withdraw(String accountId, BigDecimal amount) {
        try {
            client.withdraw(accountId, amount);
        } catch (Exception ex) {
            throw ErrorHandler.rethrow(ex);
        }
    }

    public void transfer(String sourceAccountId, AccountTransferDTO dto) {
        try {
            client.transfer(sourceAccountId, dto);
        } catch (Exception ex) {
            throw ErrorHandler.rethrow(ex);
        }
    }
}

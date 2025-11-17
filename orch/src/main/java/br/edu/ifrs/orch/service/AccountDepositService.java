package br.edu.ifrs.orch.service;

import br.edu.ifrs.orch.client.AccountDepositClient;
import br.edu.ifrs.orch.exception.ErrorHandler;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.math.BigDecimal;

@ApplicationScoped
public class AccountDepositService {
    @Inject
    @RestClient
    AccountDepositClient client;

    public void deposit(String accountId, BigDecimal amount) {
        try {
            client.deposit(accountId, amount);
        } catch (Exception ex) {
            throw ErrorHandler.rethrow(ex);
        }
    }
}

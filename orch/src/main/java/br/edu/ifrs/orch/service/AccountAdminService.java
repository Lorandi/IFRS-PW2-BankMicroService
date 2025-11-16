package br.edu.ifrs.orch.service;

import br.edu.ifrs.orch.client.AccountAdminClient;
import br.edu.ifrs.orch.dto.AccountDTO;
import br.edu.ifrs.orch.exception.ErrorHandler;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;

@ApplicationScoped
public class AccountAdminService {

    @Inject
    @RestClient
    AccountAdminClient client;

    public List<AccountDTO> listAll() {
        try {
            return client.listAll();
        } catch (Exception ex) {
            throw ErrorHandler.rethrow(ex);
        }
    }

    public AccountDTO getByAccountId(String accountId) {
        try {
            return client.getByAccountId(accountId);
        } catch (Exception ex) {
            throw ErrorHandler.rethrow(ex);
        }
    }

    public AccountDTO toggleStatus(String accountId) {
        try {
            return client.toggleStatus(accountId);
        } catch (Exception ex) {
            throw ErrorHandler.rethrow(ex);
        }
    }
}

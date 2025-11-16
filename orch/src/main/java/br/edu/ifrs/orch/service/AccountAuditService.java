package br.edu.ifrs.orch.service;

import br.edu.ifrs.orch.client.AccountAuditClient;
import br.edu.ifrs.orch.dto.AccountAuditDTO;
import br.edu.ifrs.orch.exception.ErrorHandler;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;

@ApplicationScoped
public class AccountAuditService {
    @Inject
    @RestClient
    AccountAuditClient client;

    public List<AccountAuditDTO> listAll() {
        try {
            return client.listAll();
        } catch (Exception ex) {
            throw ErrorHandler.rethrow(ex);
        }
    }

    public List<AccountAuditDTO> recent(int limit) {
        try {
            return client.recent(limit);
        } catch (Exception ex) {
            throw ErrorHandler.rethrow(ex);
        }
    }

    public List<AccountAuditDTO> byOwner(Long ownerId) {
        try {
            return client.byOwner(ownerId);
        } catch (Exception ex) {
            throw ErrorHandler.rethrow(ex);
        }
    }

    public List<AccountAuditDTO> byAccount(String accountId) {
        try {
            return client.byAccount(accountId);
        } catch (Exception ex) {
            throw ErrorHandler.rethrow(ex);
        }
    }
}

package br.edu.ifrs.orch.service;

import br.edu.ifrs.orch.client.UserAuditClient;
import br.edu.ifrs.orch.dto.UserAuditDTO;
import br.edu.ifrs.orch.exception.ErrorHandler;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;

@ApplicationScoped
public class UsersAuditService {

    @Inject
    @RestClient
    UserAuditClient client;

    public List<UserAuditDTO>listAll() {
        try {
            return client.listAll();
        } catch (Exception ex) {
            throw ErrorHandler.rethrow(ex);
        }
    }

    public List<UserAuditDTO> recent(int limit) {
        try {
            return client.recent(limit);
        } catch (Exception ex) {
            throw ErrorHandler.rethrow(ex);
        }
    }

    public List<UserAuditDTO> byOwner(Long ownerId) {
        try {
            return client.byOwner(ownerId);
        } catch (Exception ex) {
            throw ErrorHandler.rethrow(ex);
        }
    }
}

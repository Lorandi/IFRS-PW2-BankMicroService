package br.edu.ifrs.orch.service;

import br.edu.ifrs.orch.client.UsersAdminClient;
import br.edu.ifrs.orch.dto.UserMinDTO;
import br.edu.ifrs.orch.enums.RoleTypeEnum;
import br.edu.ifrs.orch.exception.ErrorHandler;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.List;

@ApplicationScoped
public class UsersAdminService {

    @Inject
    @RestClient
    UsersAdminClient client;

    public List<UserMinDTO> listAll() {
        try {
            return client.listAll();
        } catch (Exception ex) {
            throw ErrorHandler.rethrow(ex);
        }
    }

    public UserMinDTO getByUsername(String username) {
        try {
            return client.getByUsername(username);
        } catch (Exception ex) {
            throw ErrorHandler.rethrow(ex);
        }
    }

    public UserMinDTO getByCpf(String cpf) {
        try {
            return client.getByCpf(cpf);
        } catch (Exception ex) {
            throw ErrorHandler.rethrow(ex);
        }
    }

    public void delete(Long id) {
        try {
            client.delete(id);
        } catch (Exception ex) {
            throw ErrorHandler.rethrow(ex);
        }
    }

    public void changeRole(Long id, RoleTypeEnum newRole) {
        try {
            client.changeRole(id, newRole.name());
        } catch (Exception ex) {
            throw ErrorHandler.rethrow(ex);
        }
    }
}

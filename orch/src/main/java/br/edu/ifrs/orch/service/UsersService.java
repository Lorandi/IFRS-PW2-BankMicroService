package br.edu.ifrs.orch.service;

import br.edu.ifrs.orch.client.UsersClient;
import br.edu.ifrs.orch.dto.UserChangePasswordDTO;
import br.edu.ifrs.orch.dto.UserMinDTO;
import br.edu.ifrs.orch.exception.ErrorHandler;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class UsersService {

    @Inject
    @RestClient
    UsersClient client;

    public UserMinDTO getById(Long id) {
        try{
            return client.getById(id);
        } catch (Exception ex) {
            throw ErrorHandler.rethrow(ex);
        }
    }

    public void changePassword(UserChangePasswordDTO dto) {
        client.changePassword(dto);
    }

    public void changePasswordWithAuth(UserChangePasswordDTO dto) {
        try{
            client.changePasswordWithAuth(dto);
        } catch (Exception ex) {
            throw ErrorHandler.rethrow(ex);
        }
    }
}
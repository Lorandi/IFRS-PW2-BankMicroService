package br.edu.ifrs.orch.service;

import br.edu.ifrs.orch.client.UserLoginClient;
import br.edu.ifrs.orch.dto.UserLoginRequestDTO;
import br.edu.ifrs.orch.dto.UserSignupRequestDTO;
import br.edu.ifrs.orch.dto.UserTokenResponseDTO;
import br.edu.ifrs.orch.exception.ErrorHandler;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import java.util.Map;

@ApplicationScoped
public class UsersLoginService {

    @Inject
    @RestClient
    UserLoginClient client;

    public Response signup(UserSignupRequestDTO dto) {
        try {
            client.signup(dto);

            return Response.status(Response.Status.CREATED)
                    .entity(Map.of("message", "Usuário criado com sucesso"))
                    .build();

        } catch (Exception ex) {
            return ErrorHandler.toResponse(ex);
        }
    }

    public UserTokenResponseDTO login(@Valid UserLoginRequestDTO dto) {
        try {
            return client.login(dto);
        } catch (Exception ex) {
            throw ErrorHandler.rethrow(ex);
        }
    }
}


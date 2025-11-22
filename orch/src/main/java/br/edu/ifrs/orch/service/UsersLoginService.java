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

import org.eclipse.microprofile.faulttolerance.*;

import java.util.Map;

@ApplicationScoped
public class UsersLoginService {

    @Inject
    @RestClient
    UserLoginClient client;

//    public Response signup(UserSignupRequestDTO dto) {
//        try {
//            client.signup(dto);
//
//            return Response.status(Response.Status.CREATED)
//                    .entity(Map.of("message", "Usuário criado com sucesso"))
//                    .build();
//
//        } catch (Exception ex) {
//            return ErrorHandler.toResponse(ex);
//        }
//    }
//
//    public UserTokenResponseDTO login(@Valid UserLoginRequestDTO dto) {
//        try {
//            return client.login(dto);
//        } catch (Exception ex) {
//            throw ErrorHandler.rethrow(ex);
//        }
//    }

    @Retry(maxRetries = 3, delay = 1000)
    @Timeout(3000)
//    @Fallback(fallbackMethod = "signupFallback")
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

    public Response signupFallback(UserSignupRequestDTO dto) {
        return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                .entity(Map.of("error", "Serviço indisponível, tente novamente mais tarde (fallback signup)"))
                .build();
    }

   @CircuitBreaker(
            requestVolumeThreshold = 4,
            failureRatio = 0.5,
            delay = 5000
    )
    @Retry(maxRetries = 2, delay = 500)
    @Timeout(2000)
//    @Fallback(fallbackMethod = "loginFallback")
    public UserTokenResponseDTO login(@Valid UserLoginRequestDTO dto) {
        try {
            return client.login(dto);
        } catch (Exception ex) {
            throw ErrorHandler.rethrow(ex);
        }
    }

    public UserTokenResponseDTO loginFallback(@Valid UserLoginRequestDTO dto) {
        return new UserTokenResponseDTO(
                "Serviço indisponível no momento (fallback login)"
        );
    }

    @Bulkhead(value = 2)
    public String healthCheck() {
        return "OK";
    }
}


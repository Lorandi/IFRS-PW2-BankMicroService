package br.edu.ifrs.user.resource;

import br.edu.ifrs.user.dto.*;
import br.edu.ifrs.user.service.LoginService;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Timed;

import java.util.Map;

@Path("/api/v1/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoginResource {

    @Inject
    LoginService service;

//    http://localhost:8081/q/metrics/application
    @POST
    @Counted(name = "users_creation_count")
    @Timed(name = "users_creation_time")
    public Response signup(@Valid SignupRequestDTO body) {
        service.signup(body);
        return Response.status(Response.Status.CREATED)
                .entity(Map.of(
                        "message", "Usuário criado com sucesso"))
                .build();
    }

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Counted(name = "users_login_count", description = "Quantidade de logins executados")
    @Timed(name = "users_login_time", description = "Tempo para processar login", unit = MetricUnits.MILLISECONDS)
    public TokenResponseDTO login(LoginRequestDTO dto) {
        return service.login(dto.userId(), dto.password());
    }
}

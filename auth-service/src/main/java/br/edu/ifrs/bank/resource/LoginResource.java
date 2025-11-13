package br.edu.ifrs.bank.resource;

import br.edu.ifrs.bank.dto.*;
import br.edu.ifrs.bank.entity.User;
import br.edu.ifrs.bank.enums.RoleTypeEnum;
import br.edu.ifrs.bank.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;

@Path("/api/v1/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoginResource {

    @Inject
    UserService service;

    @POST
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
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginRequestDTO dto) {
        TokenResponseDTO token = service.login(dto.userId(), dto.password());
        return Response.ok(token).build();
    }
}

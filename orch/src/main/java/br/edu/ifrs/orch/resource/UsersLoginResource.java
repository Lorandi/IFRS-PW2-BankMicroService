package br.edu.ifrs.orch.resource;

import br.edu.ifrs.orch.dto.UserLoginRequestDTO;
import br.edu.ifrs.orch.dto.UserSignupRequestDTO;
import br.edu.ifrs.orch.dto.UserTokenResponseDTO;
import br.edu.ifrs.orch.service.UsersLoginService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/orch/users/login")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UsersLoginResource {

    @Inject
    UsersLoginService service;

    @POST
    @Path("/signup")
    public Response signup(UserSignupRequestDTO dto) {
        return service.signup(dto);
    }


    @POST
    @Path("/login")
    public UserTokenResponseDTO login(UserLoginRequestDTO dto) {
        return service.login(dto);
    }
}


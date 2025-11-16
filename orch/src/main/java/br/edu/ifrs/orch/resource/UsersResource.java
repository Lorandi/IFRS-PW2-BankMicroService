package br.edu.ifrs.orch.resource;

import br.edu.ifrs.orch.dto.UserChangePasswordDTO;
import br.edu.ifrs.orch.dto.UserMinDTO;
import br.edu.ifrs.orch.service.UsersService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import jakarta.annotation.security.RolesAllowed;
import java.util.Map;

@Path("/orch/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed({"CUSTOMER", "ADMIN", "AUDITOR"})
public class UsersResource {

    @Inject
    UsersService service;

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        UserMinDTO user = service.getById(id);
        return Response.ok(user).build();
    }

    @PATCH
    @Path("/password")
    public Response changePassword(UserChangePasswordDTO dto) {
        service.changePassword(dto);
        return Response.ok(Map.of("message", "Senha alterada com sucesso")).build();
    }

    @PATCH
    @Path("/change-password")
    public Response changePasswordWithAuth(UserChangePasswordDTO dto) {
        service.changePasswordWithAuth(dto);
        return Response.ok(Map.of("message", "Senha alterada com sucesso!")).build();
    }
}


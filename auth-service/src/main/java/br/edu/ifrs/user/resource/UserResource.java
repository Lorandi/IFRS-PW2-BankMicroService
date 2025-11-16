package br.edu.ifrs.user.resource;

import br.edu.ifrs.user.dto.ChangePasswordDTO;
import br.edu.ifrs.user.dto.UserMinDTO;
import br.edu.ifrs.user.service.UserService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.Map;

@Path("/api/v1/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed({"CUSTOMER", "ADMIN", "AUDITOR"})
public class UserResource {

    @Inject
    UserService service;


    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        UserMinDTO user = service.getById(id);
        return Response.ok(user).build();
    }

    @PATCH
    @Path("/password")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response changePassword(ChangePasswordDTO dto) {
        service.changePassword(dto);
        return Response.ok(Map.of(
                "message", "Senha alterada com sucesso"
        )).build();
    }

    @PATCH
    @Path("/change-password")
    public Response changePasswordWithAuth(ChangePasswordDTO dto) {
        service.changePasswordWithAuth(dto);
        return Response.ok(Map.of("message", "Senha alterada com sucesso!")).build();
    }
}

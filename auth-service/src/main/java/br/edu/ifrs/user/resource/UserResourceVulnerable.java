package br.edu.ifrs.user.resource;

import br.edu.ifrs.user.dto.ChangePasswordDTO;
import br.edu.ifrs.user.dto.UserMinDTO;
import br.edu.ifrs.user.entity.User;
import br.edu.ifrs.user.enums.RoleTypeEnum;
import br.edu.ifrs.user.service.UserService;
import io.quarkus.security.Authenticated;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;

@Path("/api/v1/vulnerable")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserResourceVulnerable {

    @Inject
    UserService service;

    @GET
    @Path("/user/{id}")
    public Response getUserById(@PathParam("id") Long id) {
        User user = service.getUserById(id);
        return Response.ok(user).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(@PathParam("id") Long id) {
        UserMinDTO user = service.getById(id);
        return Response.ok(user).build();
    }

    @Authenticated
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
    @GET
    public Response listAll() {
        List<UserMinDTO> users = service.listAll();
        return Response.ok(users).build();
    }

    @GET
    @Path("/username/{username}")
    public Response getByUsername(@PathParam("username") String username) {
        UserMinDTO user = service.getByUsername(username);
        return Response.ok(user).build();
    }

    @GET
    @Path("/cpf/{cpf}")
    public Response getByCpf(@PathParam("cpf") String cpf) {
        UserMinDTO user = service.getByCpf(cpf);
        return Response.ok(user).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        service.delete(id);
        return Response.ok(Map.of(
                "message", "Usuário excluído com sucesso",
                "id", id
        )).build();
    }

    @PATCH
    @Path("/{id}/role")
    @Produces(MediaType.APPLICATION_JSON)
    public Response changeRole(
            @PathParam("id") Long userId,
            @QueryParam("role") RoleTypeEnum newRole
    ) {
        service.changeRole(userId, newRole);

        return Response.ok(Map.of(
                "message", "Role do usuário atualizada com sucesso",
                "userId", userId,
                "newRole", newRole.toString()
        )).build();
    }
}

package br.edu.ifrs.bank.resource;

import br.edu.ifrs.bank.dto.ChangePasswordDTO;
import br.edu.ifrs.bank.dto.SignupRequestDTO;
import br.edu.ifrs.bank.dto.UserMinDTO;
import br.edu.ifrs.bank.dto.UserUpdateDTO;
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
@RolesAllowed("ADMIN")
public class AdminResource {

    @Inject
    UserService service;

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

package br.edu.ifrs.orch.client;

import br.edu.ifrs.orch.dto.UserMinDTO;
import io.quarkus.oidc.token.propagation.common.AccessToken;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@RegisterRestClient(configKey = "auth-api")
@Path("/api/v1/users")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@AccessToken
public interface UsersAdminClient {

    @GET
    List<UserMinDTO> listAll();

    @GET
    @Path("/username/{username}")
    UserMinDTO getByUsername(@PathParam("username") String username);

    @GET
    @Path("/cpf/{cpf}")
    UserMinDTO getByCpf(@PathParam("cpf") String cpf);

    @DELETE
    @Path("/{id}")
    void delete(@PathParam("id") Long id);

    @PATCH
    @Path("/{id}/role")
    void changeRole(
            @PathParam("id") Long userId,
            @QueryParam("role") String newRole
//            @QueryParam("role") RoleTypeEnum newRole
    );
}

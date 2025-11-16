package br.edu.ifrs.orch.client;

import br.edu.ifrs.orch.dto.UserChangePasswordDTO;
import br.edu.ifrs.orch.dto.UserMinDTO;
import io.quarkus.oidc.token.propagation.common.AccessToken;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "auth-api")
@Path("/api/v1/users")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@AccessToken
public interface UsersClient {

    @GET
    @Path("/{id}")
    UserMinDTO getById(@PathParam("id") Long id);

    @PATCH
    @Path("/password")
    void changePassword(UserChangePasswordDTO dto);

    @PATCH
    @Path("/change-password")
    void changePasswordWithAuth(UserChangePasswordDTO dto);
}

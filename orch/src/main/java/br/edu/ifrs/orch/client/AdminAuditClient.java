package br.edu.ifrs.orch.client;


import br.edu.ifrs.orch.dto.UserAuditDTO;
import io.quarkus.oidc.token.propagation.common.AccessToken;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@AccessToken
@RegisterRestClient(configKey = "auth-api")
@Path("/api/v1/audit")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface AdminAuditClient {

    @GET
    List<UserAuditDTO> listAll();

    @GET
    @Path("/recent")
    List<UserAuditDTO> recent(@QueryParam("limit") int limit);

    @GET
    @Path("/owner/{id}")
    List<UserAuditDTO> byOwner(@PathParam("id") Long ownerId);
}

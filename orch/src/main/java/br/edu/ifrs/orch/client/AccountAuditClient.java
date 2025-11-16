package br.edu.ifrs.orch.client;

import br.edu.ifrs.orch.dto.AccountAuditDTO;
import io.quarkus.oidc.token.propagation.common.AccessToken;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import java.util.List;

@AccessToken
@RegisterRestClient(configKey = "account-api")
@Path("/api/v1/audit")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface AccountAuditClient {

    @GET
    List<AccountAuditDTO> listAll();

    @GET
    @Path("/recent")
    List<AccountAuditDTO> recent(@QueryParam("limit") int limit);

    @GET
    @Path("/owner/{id}")
    List<AccountAuditDTO> byOwner(@PathParam("id") Long ownerId);

    @GET
    @Path("/account/{accountId}")
    List<AccountAuditDTO> byAccount(@PathParam("accountId") String accountId);
}

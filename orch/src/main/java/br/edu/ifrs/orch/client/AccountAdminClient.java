package br.edu.ifrs.orch.client;

import br.edu.ifrs.orch.dto.AccountDTO;
import io.quarkus.oidc.token.propagation.common.AccessToken;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.util.List;

@AccessToken
@RegisterRestClient(configKey = "account-api")
@Path("/api/v1/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface AccountAdminClient {

    @GET
    List<AccountDTO> listAll();

    @GET
    @Path("/{accountId}")
    AccountDTO getByAccountId(@PathParam("accountId") String accountId);

    @PATCH
    @Path("/{accountId}/account-status-toggle")
    AccountDTO toggleStatus(@PathParam("accountId") String accountId);
}

package br.edu.ifrs.orch.client;

import br.edu.ifrs.orch.dto.AccountDTO;
import br.edu.ifrs.orch.dto.AccountTransferDTO;
import io.quarkus.oidc.token.propagation.common.AccessToken;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.math.BigDecimal;

@AccessToken
@RegisterRestClient(configKey = "account-api")
@Path("/api/v1/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface AccountClient {

    @POST
    void create();

    @GET
    @Path("/owner")
    AccountDTO getByOwnerId();

    @PATCH
    @Path("/{accountId}/deposit")
    void deposit(
            @PathParam("accountId") String accountId,
            @QueryParam("amount") BigDecimal amount
    );

    @PATCH
    @Path("/{accountId}/withdraw")
    void withdraw(
            @PathParam("accountId") String accountId,
            @QueryParam("amount") BigDecimal amount
    );

    @PATCH
    @Path("/{accountId}/transfer")
    void transfer(
            @PathParam("accountId") String accountId,
            AccountTransferDTO dto
    );
}

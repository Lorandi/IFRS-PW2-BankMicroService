package br.edu.ifrs.orch.client;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import java.math.BigDecimal;


@RegisterRestClient(configKey = "account-api")
@Path("/api/v1/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface AccountDepositClient {

    @PATCH
    @Path("/{accountId}/deposit")
    void deposit(
            @PathParam("accountId") String accountId,
            @QueryParam("amount") BigDecimal amount
    );

}

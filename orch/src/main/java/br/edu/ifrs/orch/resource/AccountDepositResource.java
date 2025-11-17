package br.edu.ifrs.orch.resource;

import br.edu.ifrs.orch.service.AccountDepositService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import java.util.Map;

@Path("/orch/accounts/deposit")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class AccountDepositResource {

    @Inject
    AccountDepositService service;

    @PATCH
    @Path("/{accountId}/deposit")
    public Response deposit(
            @PathParam("accountId") String accountId,
            @QueryParam("amount") BigDecimal amount
    ) {
        service.deposit(accountId, amount);
        return Response.ok(Map.of("message", "Depósito realizado com sucesso!")).build();
    }
}


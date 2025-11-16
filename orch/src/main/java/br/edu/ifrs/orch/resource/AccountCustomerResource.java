package br.edu.ifrs.orch.resource;

import br.edu.ifrs.orch.dto.AccountDTO;
import br.edu.ifrs.orch.dto.AccountTransferDTO;
import br.edu.ifrs.orch.service.AccountCustomerService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
        import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import java.util.Map;

@Path("/orch/accounts/customer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("CUSTOMER")
public class AccountCustomerResource {

    @Inject
    AccountCustomerService service;

    @POST
    public Response create() {
        service.create();
        return Response.status(Response.Status.CREATED)
                .entity(Map.of("message", "Conta criada com sucesso!"))
                .build();
    }

    @GET
    @Path("/owner")
    public AccountDTO getByOwnerId() {
        return service.getByOwnerId();
    }

    @PermitAll
    @PATCH
    @Path("/{accountId}/deposit")
    public Response deposit(
            @PathParam("accountId") String accountId,
            @QueryParam("amount") BigDecimal amount
    ) {
        service.deposit(accountId, amount);
        return Response.ok(Map.of("message", "Depósito realizado com sucesso!")).build();
    }

    @PATCH
    @Path("/{accountId}/withdraw")
    public Response withdraw(
            @PathParam("accountId") String accountId,
            @QueryParam("amount") BigDecimal amount
    ) {
        service.withdraw(accountId, amount);
        return Response.ok(Map.of("message", "Saque realizado com sucesso!")).build();
    }

    @PATCH
    @Path("/{accountId}/transfer")
    public Response transfer(
            @PathParam("accountId") String sourceAccountId,
            AccountTransferDTO dto
    ) {
        service.transfer(sourceAccountId, dto);
        return Response.ok(Map.of(
                "message", "Transferência realizada com sucesso!",
                "from", sourceAccountId,
                "to", dto.targetAccountId(),
                "amount", dto.amount()
        )).build();
    }
}


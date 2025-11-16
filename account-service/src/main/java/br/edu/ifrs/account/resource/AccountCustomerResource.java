package br.edu.ifrs.account.resource;

import br.edu.ifrs.account.dto.AccountDTO;
import br.edu.ifrs.account.dto.AccountTransferDTO;
import br.edu.ifrs.account.service.AccountCustomerService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.math.BigDecimal;
import java.util.Map;

@Path("/api/v1/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("CUSTOMER")
public class AccountCustomerResource {

    @Inject
    AccountCustomerService service;

     @POST
    @Transactional
    public Response create() {
        try {
            service.create();

            return Response.status(Response.Status.CREATED)
                    .entity("{\"message\":\"Conta criada com sucesso!\"}")
                    .build();

        } catch (IllegalStateException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    @GET
    @Path("/owner/")
    public Response getByOwnerId() {
        try {
            AccountDTO account = service.getByOwnerId();
            return Response.ok(account).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    @PermitAll
    @PATCH
    @Path("/{accountId}/deposit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deposit(
            @PathParam("accountId") String accountId,
            @QueryParam("amount") BigDecimal amount) {
        try {
            service.deposit(accountId, amount);

            return Response.ok(Map.of("message", "Depósito realizado com sucesso!")).build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        } catch (IllegalStateException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    @PATCH
    @Path("/{accountId}/withdraw")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response withdraw(
            @PathParam("accountId") String accountId,
            @QueryParam("amount") BigDecimal amount) {

        try {
            service.withdraw(accountId, amount);

            return Response.ok(
                    Map.of("message", "Saque realizado com sucesso!")).build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        } catch (IllegalStateException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

    @PATCH
    @Path("/{accountId}/transfer")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response transfer(
            @PathParam("accountId") String sourceAccountId,
            AccountTransferDTO request) {

        try {
            service.transfer(sourceAccountId, request.targetAccountId(), request.amount());

            return Response.ok(
                    Map.of(
                            "message", "Transferência realizada com sucesso!",
                            "from", sourceAccountId,
                            "to", request.targetAccountId(),
                            "amount", request.amount()
                    )
            ).build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        } catch (IllegalStateException e) {
            return Response.status(Response.Status.CONFLICT)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }
}

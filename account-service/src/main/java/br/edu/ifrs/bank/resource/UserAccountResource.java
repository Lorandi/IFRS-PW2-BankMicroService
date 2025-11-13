package br.edu.ifrs.bank.resource;

import br.edu.ifrs.bank.dto.AccountDepositDTO;
import br.edu.ifrs.bank.dto.AccountRequestDTO;
import br.edu.ifrs.bank.dto.AccountTransferDTO;
import br.edu.ifrs.bank.dto.AccountWithdrawDTO;
import br.edu.ifrs.bank.entity.Account;
import br.edu.ifrs.bank.service.AccountService;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.Map;

@Path("/api/v1/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class UserAccountResource {

    @Inject
    AccountService service;

    @Inject
    JsonWebToken jwt;

    @POST
    @Transactional
    public Response create(AccountRequestDTO request) {
        try {
            service.create(request);
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
    @Path("/owner/{ownerId}")
    public Response getByOwnerId(@PathParam("ownerId") Long ownerId) {
        try {
            Account account = service.getByOwnerId(ownerId);
            return Response.ok(account).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    @PATCH
    @Path("/{accountId}/deposit")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deposit(
            @PathParam("accountId") String accountId,
            AccountDepositDTO amount) {
        try {
            service.deposit(accountId, amount.amount());

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
            AccountWithdrawDTO request) {

        try {
            Account updated = service.withdraw(accountId, request.ownerId(), request.amount());

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
            service.transfer(sourceAccountId, request.ownerId(), request.targetAccountId(), request.amount());

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
    @GET
    @Path("/whoami")
    @Authenticated
    public Response whoami() {
        return Response.ok(Map.of(
                "userId", jwt.getClaim("userId"),
                "username", jwt.getName(),
                "groups", jwt.getGroups()
        )).build();
    }

}

package br.edu.ifrs.account.resource;

import br.edu.ifrs.account.entity.Account;
import br.edu.ifrs.account.service.AdminService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;

@Path("/api/v1/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("ADMIN")
public class AdminResource {

    @Inject
    AdminService service;

    @GET
    public List<Account> listAll() {
        return service.listAll();
    }


    @GET
    @Path("/{accountId}")
    public Response getByAccountId(@PathParam("accountId") String accountId) {
        try {
            Account account = service.getByAccountId(accountId);
            return Response.ok(account).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\":\"" + e.getMessage() + "\"}")
                    .build();
        }
    }

    @PATCH
    @Path("/{accountId}/account-status-toggle")
    @Produces(MediaType.APPLICATION_JSON)
    public Response toggleStatus(@PathParam("accountId") String accountId) {
        try {
            Account updated = service.toggleStatus(accountId);

            return Response.ok(
                    Map.of(
                            "message", "Status da conta alterado com sucesso!",
                            "accountId", updated.getAccountId(),
                            "status", updated.getStatus().name()
                    )
            ).build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", e.getMessage()))
                    .build();
        }
    }

}

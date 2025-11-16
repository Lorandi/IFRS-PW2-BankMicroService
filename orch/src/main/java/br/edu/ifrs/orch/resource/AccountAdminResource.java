package br.edu.ifrs.orch.resource;

import br.edu.ifrs.orch.dto.AccountDTO;
import br.edu.ifrs.orch.service.AccountAdminService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Map;

@Path("/orch/accounts/admin")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@RolesAllowed("ADMIN")
public class AccountAdminResource {

    @Inject
    AccountAdminService service;

    @GET
    public List<AccountDTO> listAll() {
        return service.listAll();
    }

    @GET
    @Path("/{accountId}")
    public AccountDTO getByAccountId(@PathParam("accountId") String accountId) {
        return service.getByAccountId(accountId);
    }

    @PATCH
    @Path("/{accountId}/account-status-toggle")
    public Response toggleStatus(@PathParam("accountId") String accountId) {

        AccountDTO updated = service.toggleStatus(accountId);

        return Response.ok(
                Map.of(
                        "message", "Status da conta alterado com sucesso!",
                        "accountId", updated.accountId(),
                        "status", updated.status()
                )
        ).build();
    }
}

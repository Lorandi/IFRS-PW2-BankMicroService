package br.edu.ifrs.account.resource;

import br.edu.ifrs.account.dto.AccountAuditDTO;
import br.edu.ifrs.account.service.AccountAuditService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/api/v1/audit")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed("AUDITOR")
public class AccountAuditResource {

    @Inject
    AccountAuditService service;

    @GET
    public List<AccountAuditDTO> listAll() {
        return service.listAll();
    }

    @GET
    @Path("/recent")
    public List<AccountAuditDTO> recent(@QueryParam("limit") @DefaultValue("10") int limit) {
        return service.recent(limit);
    }

    @GET
    @Path("/owner/{id}")
    public List<AccountAuditDTO> byOwner(@PathParam("id") Long ownerId) {
        return service.byOwner(ownerId);
    }

    @GET
    @Path("/account/{accountId}")
    public List<AccountAuditDTO> byAccount(@PathParam("accountId") String accountId) {
        return service.byAccountId(accountId);
    }
}

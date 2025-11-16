package br.edu.ifrs.account.resource;

import br.edu.ifrs.account.entity.AccountAudit;
import br.edu.ifrs.account.service.AuditService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/api/v1/audit")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed("AUDITOR")
public class AuditResource {

    @Inject
    AuditService service;

    @GET
    public List<AccountAudit> listAll() {
        return service.listAll();
    }

    @GET
    @Path("/recent")
    public List<AccountAudit> recent(@QueryParam("limit") @DefaultValue("10") int limit) {
        return service.recent(limit);
    }

    @GET
    @Path("/owner/{id}")
    public List<AccountAudit> byOwner(@PathParam("id") Long ownerId) {
        return service.byOwner(ownerId);
    }

    @GET
    @Path("/account/{accountId}")
    public List<AccountAudit> byAccount(@PathParam("accountId") String accountId) {
        return service.byAccountId(accountId);
    }
}

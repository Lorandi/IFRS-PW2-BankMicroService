package br.edu.ifrs.bank.resource;

import br.edu.ifrs.bank.entity.AdminAudit;
import br.edu.ifrs.bank.repository.AdminAuditRepository;
import br.edu.ifrs.bank.service.AdminAuditService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/api/v1/audit")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed("AUDITOR")
public class AdminAuditResource {

    @Inject
    AdminAuditService service;

    @GET
    public List<AdminAudit> listAll() {
        return service.listAll();
    }

    @GET
    @Path("/recent")
    public List<AdminAudit> recent(@QueryParam("limit") @DefaultValue("10") int limit) {
        return service.recent(limit);
    }

    @GET
    @Path("/owner/{id}")
    public List<AdminAudit> byOwner(@PathParam("id") Long ownerId) {
        return service.byOwner(ownerId);
    }

    @GET
    @Path("/account/{accountId}")
    public List<AdminAudit> byAccount(@PathParam("accountId") String accountId) {
        return service.byAccountId(accountId);
    }
}

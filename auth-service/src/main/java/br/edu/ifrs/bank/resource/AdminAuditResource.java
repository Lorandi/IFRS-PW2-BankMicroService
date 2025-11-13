package br.edu.ifrs.bank.resource;

import br.edu.ifrs.bank.entity.AdminAuditUser;
import br.edu.ifrs.bank.service.AdminAuditUserService;
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
    AdminAuditUserService service;

    @GET
    public List<AdminAuditUser> listAll() {
        return service.listAll();
    }

    @GET
    @Path("/recent")
    public List<AdminAuditUser> recent(@QueryParam("limit") @DefaultValue("10") int limit) {
        return service.recent(limit);
    }

    @GET
    @Path("/owner/{id}")
    public List<AdminAuditUser> byOwner(@PathParam("id") Long ownerId) {
        return service.byOwner(ownerId);
    }
}

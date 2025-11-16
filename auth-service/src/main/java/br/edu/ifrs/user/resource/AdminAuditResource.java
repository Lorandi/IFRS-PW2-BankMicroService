package br.edu.ifrs.user.resource;

import br.edu.ifrs.user.dto.AdminAuditUserDTO;
import br.edu.ifrs.user.service.UserAuditService;
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
    UserAuditService service;

    @GET
    public List<AdminAuditUserDTO> listAll() {
        return service.listAll();
    }

    @GET
    @Path("/recent")
    public List<AdminAuditUserDTO>recent(@QueryParam("limit") @DefaultValue("10") int limit) {
        return service.recent(limit);
    }

    @GET
    @Path("/owner/{id}")
    public List<AdminAuditUserDTO> byOwner(@PathParam("id") Long userId) {
        return service.userId(userId);
    }
}

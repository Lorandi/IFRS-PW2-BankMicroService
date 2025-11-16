package br.edu.ifrs.orch.resource;



import br.edu.ifrs.orch.dto.UserAuditDTO;
import br.edu.ifrs.orch.service.UsersAuditService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.util.List;

@Path("/orch/users/audit")
@Produces(MediaType.APPLICATION_JSON)
@RolesAllowed("AUDITOR")
public class UsersAuditResource {

    @Inject
    UsersAuditService service;

    @GET
    public List<UserAuditDTO>listAll() {
        return service.listAll();
    }

    @GET
    @Path("/recent")
    public List<UserAuditDTO> recent(@QueryParam("limit") @DefaultValue("10") int limit) {
        return service.recent(limit);
    }

    @GET
    @Path("/owner/{id}")
    public List<UserAuditDTO>byOwner(@PathParam("id") Long ownerId) {
        return service.byOwner(ownerId);
    }
}

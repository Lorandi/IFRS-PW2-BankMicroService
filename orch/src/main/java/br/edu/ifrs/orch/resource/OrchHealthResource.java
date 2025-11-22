package br.edu.ifrs.orch.resource;

import br.edu.ifrs.orch.service.HealthOrchService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import java.util.Map;

@Path("/orch/health")
@Produces(MediaType.APPLICATION_JSON)
public class OrchHealthResource {

    @Inject
    HealthOrchService healthService;

    @GET
    @Path("/account/live")
    public Map<String, Object> checkAccountLive() {
        return Map.of(
                "service", "account",
                "liveness", healthService.isAccountAlive() ? "UP" : "DOWN"
        );
    }

    @GET
    @Path("/account/ready")
    public Map<String, Object> checkAccountReady() {
        return Map.of(
                "service", "account",
                "readiness", healthService.isAccountReady() ? "UP" : "DOWN"
        );
    }


    @GET
    @Path("/users/live")
    public Map<String, Object> checkUsersLive() {
        return Map.of(
                "service", "users",
                "liveness", healthService.isUsersAlive() ? "UP" : "DOWN"
        );
    }

    @GET
    @Path("/users/ready")
    public Map<String, Object> checkUsersReady() {
        return Map.of(
                "service", "users",
                "readiness", healthService.isUsersReady() ? "UP" : "DOWN"
        );
    }


    @GET
    @Path("/full")
    public Map<String, Object> fullStatus() {
        return Map.of(
                "account", Map.of(
                        "live", healthService.isAccountAlive() ? "UP" : "DOWN",
                        "ready", healthService.isAccountReady() ? "UP" : "DOWN"
                ),
                "users", Map.of(
                        "live", healthService.isUsersAlive() ? "UP" : "DOWN",
                        "ready", healthService.isUsersReady() ? "UP" : "DOWN"
                )
        );
    }
}

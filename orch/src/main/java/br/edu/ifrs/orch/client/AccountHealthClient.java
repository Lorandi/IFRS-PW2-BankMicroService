package br.edu.ifrs.orch.client;

import jakarta.json.JsonObject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

@RegisterRestClient(configKey = "account-api")
@Path("/q/health")
public interface AccountHealthClient {

    @GET
    @Path("/live")
    @Produces(MediaType.APPLICATION_JSON)
    JsonObject getLiveness();

    @GET
    @Path("/ready")
    @Produces(MediaType.APPLICATION_JSON)
    JsonObject getReadiness();
}

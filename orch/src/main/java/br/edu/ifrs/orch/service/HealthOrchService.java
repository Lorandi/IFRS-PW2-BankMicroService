package br.edu.ifrs.orch.service;

import br.edu.ifrs.orch.client.AccountHealthClient;
import br.edu.ifrs.orch.client.UsersHealthClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class HealthOrchService {

    @Inject
    @RestClient
    AccountHealthClient accountHealthClient;

    @Inject
    @RestClient
    UsersHealthClient usersHealthClient;


    @Timed(name = "orch_account_ping_time", description = "Tempo para consultar health do account-service")
    public boolean isAccountAlive() {
        return checkUp(() -> accountHealthClient.getLiveness());
    }

    public boolean isAccountReady() {
        return checkUp(() -> accountHealthClient.getReadiness());
    }


//    http://localhost:8080/q/metrics/application
    @Timed(name = "orch_user_ping_time", description = "Tempo para consultar health do auth-service")
    public boolean isUsersAlive() {
        return checkUp(() -> usersHealthClient.getLiveness());
    }

    public boolean isUsersReady() {
        return checkUp(() -> usersHealthClient.getReadiness());
    }

    private boolean checkUp(HealthCall call) {
        try {
            JsonObject json = call.execute();
            return "UP".equalsIgnoreCase(json.getString("status"));
        } catch (Exception e) {
            return false;
        }
    }

    private interface HealthCall {
        JsonObject execute();
    }
}

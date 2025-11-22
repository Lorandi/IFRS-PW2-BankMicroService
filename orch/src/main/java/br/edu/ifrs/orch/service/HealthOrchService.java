package br.edu.ifrs.orch.service;

import br.edu.ifrs.orch.client.AccountHealthClient;
import br.edu.ifrs.orch.client.UsersHealthClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonObject;
import org.eclipse.microprofile.rest.client.inject.RestClient;

@ApplicationScoped
public class HealthOrchService {

    @Inject
    @RestClient
    AccountHealthClient accountHealthClient;

    @Inject
    @RestClient
    UsersHealthClient usersHealthClient;


    public boolean isAccountAlive() {
        return checkUp(() -> accountHealthClient.getLiveness());
    }

    public boolean isAccountReady() {
        return checkUp(() -> accountHealthClient.getReadiness());
    }


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

package br.edu.ifrs.account.health;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

@Liveness
public class Live implements HealthCheck {

    @Override
    public HealthCheckResponse call() {
       return HealthCheckResponse.up("Account-service is alive");
    }
    
}

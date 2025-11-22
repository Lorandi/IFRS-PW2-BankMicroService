package br.edu.ifrs.account.service;

import br.edu.ifrs.account.repository.AccountRepository;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Gauge;

@ApplicationScoped
public class AccountMetricsService {

    @Inject
    AccountRepository repo;

//    http://localhost:8082/q/metrics/application
    @Gauge(
            name = "accounts_active_total",
            unit = MetricUnits.NONE,
            description = "Número de contas ativas"
    )
    public long accountsActiveCount() {
        return repo.count("status = 'ACTIVE'");
    }

    @PostConstruct
    void init() {
        System.out.println("🚀 AccountMetrics carregado!");
    }
}
package br.edu.ifrs.bank.repository;

import br.edu.ifrs.bank.entity.AdminAudit;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class AdminAuditRepository implements PanacheRepository<AdminAudit> {

    public List<AdminAudit> findByOwner(Long ownerId) {
        return list("ownerId", ownerId);
    }

    public List<AdminAudit> findRecent(int limit) {
        return find("order by timestamp desc").page(0, limit).list();
    }
}
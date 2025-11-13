package br.edu.ifrs.bank.repository;

import br.edu.ifrs.bank.entity.AdminAuditUser;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class AdminAuditUserRepository implements PanacheRepository<AdminAuditUser> {

    public List<AdminAuditUser> findByOwner(Long ownerId) {
        return list("ownerId", ownerId);
    }

    public List<AdminAuditUser> findRecent(int limit) {
        return find("order by timestamp desc").page(0, limit).list();
    }
}
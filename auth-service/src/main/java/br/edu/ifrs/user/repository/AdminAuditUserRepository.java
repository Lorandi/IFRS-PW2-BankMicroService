package br.edu.ifrs.user.repository;

import br.edu.ifrs.user.entity.UserAudit;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class AdminAuditUserRepository implements PanacheRepository<UserAudit> {

    public List<UserAudit> findByOwner(Long ownerId) {
        return list("ownerId", ownerId);
    }

    public List<UserAudit> findRecent(int limit) {
        return find("order by timestamp desc").page(0, limit).list();
    }
}
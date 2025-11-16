package br.edu.ifrs.account.repository;

import br.edu.ifrs.account.entity.AccountAudit;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class AccountAuditRepository implements PanacheRepository<AccountAudit> {

    public List<AccountAudit> findByOwner(Long ownerId) {
        return list("ownerId", ownerId);
    }

    public List<AccountAudit> findRecent(int limit) {
        return find("order by timestamp desc").page(0, limit).list();
    }
}
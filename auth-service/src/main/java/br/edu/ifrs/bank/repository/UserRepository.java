package br.edu.ifrs.bank.repository;

import br.edu.ifrs.bank.entity.User;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {

    public User findByUsername(String username) {
        return find("username", username).firstResult();
    }

    public User findByCpf(String cpf) {
        return find("cpf", cpf).firstResult();
    }
}

package br.janioofi.financialcontrol.domain.repositories;

import br.janioofi.financialcontrol.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}

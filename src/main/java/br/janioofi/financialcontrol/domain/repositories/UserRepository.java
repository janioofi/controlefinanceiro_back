package br.janioofi.financialcontrol.domain.repositories;

import br.janioofi.financialcontrol.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}

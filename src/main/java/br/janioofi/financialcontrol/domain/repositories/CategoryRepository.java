package br.janioofi.financialcontrol.domain.repositories;

import br.janioofi.financialcontrol.domain.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByDescription(String description);
}

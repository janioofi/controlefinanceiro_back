package br.janioofi.financialcontrol.domain.repositories;

import br.janioofi.financialcontrol.domain.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}

package br.janioofi.financialcontrol.domain.repositories;

import br.janioofi.financialcontrol.domain.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}

package br.janioofi.financialcontrol.domain.repositories;

import br.janioofi.financialcontrol.domain.entities.Payment;
import br.janioofi.financialcontrol.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findAllByUser(User user);

    Optional<Payment> findByIdPaymentAndUser(Long id, User user);

    List<Payment> findByPaymentDateBetweenAndUser(LocalDate from, LocalDate to, User user);
}

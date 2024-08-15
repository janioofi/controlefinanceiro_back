package br.janioofi.financialcontrol.domain.repositories;

import br.janioofi.financialcontrol.domain.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    @Query(value = """
    SELECT * FROM tb_payment p
    WHERE CAST(p.payment_date AS date) BETWEEN CAST(:initialDate AS date) AND CAST(:finalDate AS date)
    ORDER BY p.payment_date;
    """, nativeQuery = true)
    List<Payment> findPaymentsByPeriod(@Param("initialDate") String initialDate, @Param("finalDate") String finalDate);
}

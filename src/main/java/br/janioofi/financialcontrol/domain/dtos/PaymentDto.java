package br.janioofi.financialcontrol.domain.dtos;

import br.janioofi.financialcontrol.domain.enums.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentDto(
        Long idPayment,
        String description,
        LocalDateTime paymentDate,
        BigDecimal value,
        String category,
        Long idCategory,
        Status status) {
}

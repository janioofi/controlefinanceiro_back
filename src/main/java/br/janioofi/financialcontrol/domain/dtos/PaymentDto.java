package br.janioofi.financialcontrol.domain.dtos;

import br.janioofi.financialcontrol.domain.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PaymentDto(
        Long idPayment,
        String description,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate paymentDate,
        BigDecimal value,
        String category,
        Long idCategory,
        Status status) {
}

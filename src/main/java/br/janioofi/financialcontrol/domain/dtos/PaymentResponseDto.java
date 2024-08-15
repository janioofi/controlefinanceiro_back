package br.janioofi.financialcontrol.domain.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PaymentResponseDto(
        Long idPayment,
        String description,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate paymentDate,
        BigDecimal value,
        String category,
        String status,
        String paymentMethod,
        Long idUser) {
}

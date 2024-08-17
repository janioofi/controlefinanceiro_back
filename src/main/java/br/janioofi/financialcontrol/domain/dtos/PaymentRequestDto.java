package br.janioofi.financialcontrol.domain.dtos;

import br.janioofi.financialcontrol.domain.enums.Category;
import br.janioofi.financialcontrol.domain.enums.PaymentMethod;
import br.janioofi.financialcontrol.domain.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PaymentRequestDto(
        String description,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate paymentDate,
        BigDecimal value,
        Category category,
        Status status,
        PaymentMethod paymentMethod) {
}

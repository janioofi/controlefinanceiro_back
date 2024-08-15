package br.janioofi.financialcontrol.domain.dtos;

import br.janioofi.financialcontrol.domain.enums.Category;
import br.janioofi.financialcontrol.domain.enums.PaymentMethod;
import br.janioofi.financialcontrol.domain.enums.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PaymentRequestDto(
        @NotBlank(message = "Description is mandatory") String description,
        @NotBlank(message = "Payment Date is mandatory") @JsonFormat(pattern = "yyyy-MM-dd") LocalDate paymentDate,
        @NotBlank(message = "Value is mandatory") BigDecimal value,
        @NotBlank(message = "Category is mandatory") Category category,
        @NotBlank(message = "Status is mandatory") Status status,
        @NotBlank(message = "Payment Method is mandatory") PaymentMethod paymentMethod) {
}

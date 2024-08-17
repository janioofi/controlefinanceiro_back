package br.janioofi.financialcontrol.domain.entities;

import br.janioofi.financialcontrol.domain.enums.Category;
import br.janioofi.financialcontrol.domain.enums.PaymentMethod;
import br.janioofi.financialcontrol.domain.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "tb_payment")
public class Payment{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_payment")
    private Long idPayment;

    @NotEmpty(message = "Description is mandatory")
    private String description;

    @NotNull(message = "Payment Date is mandatory")
    private LocalDate paymentDate;

    private BigDecimal value;

    @NotNull(message = "Category is mandatory")
    @Enumerated(EnumType.STRING)
    private Category category;

    @NotNull(message = "Status is mandatory")
    @Enumerated(EnumType.STRING)
    private Status status;

    @NotNull(message = "Payment Method is mandatory")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    @Enumerated(EnumType.ORDINAL)
    private User user;
}

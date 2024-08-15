package br.janioofi.financialcontrol.domain.entities;

import br.janioofi.financialcontrol.domain.enums.Category;
import br.janioofi.financialcontrol.domain.enums.PaymentMethod;
import br.janioofi.financialcontrol.domain.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_payment")
    private Long idPayment;

    @NotBlank(message = "Descrição é obrigatório")
    private String description;

    private LocalDate paymentDate;
    private BigDecimal value;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    @Enumerated(EnumType.ORDINAL)
    private User user;
}

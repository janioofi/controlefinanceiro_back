package br.janioofi.financialcontrol.domain.entity;

import br.janioofi.financialcontrol.domain.enums.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "tb_payment")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_payment")
    private Long idPayment;

    @NotBlank(message = "Descrição é obrigatório")
    private String description;

    private LocalDate paymentDate;
    private BigDecimal value;

    @ManyToOne
    @JoinColumn(name = "id_category")
    @Enumerated(EnumType.ORDINAL)
    private Category category;

    @Enumerated(EnumType.STRING)
    private Status status;
}

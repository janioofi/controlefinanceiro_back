package br.janioofi.financialcontrol.domain.enums;

import lombok.Getter;

@Getter
public enum PaymentMethod {

    MONEY(0, "Dinheiro"),
    PIX(1, "Pix"),
    DEBIT_CARD(2, "Cartão de débito"),
    CRED_CARD(3, "Cartão de crédito");

    private final Integer code;
    private final String description;

    PaymentMethod(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}

package br.janioofi.financialcontrol.domain.enums;

public enum PaymentMethod {
    MONEY(0, "MONEY"),
    PIX(1, "PIX"),
    DEBIT_CARD(2, "DEBIT CARD"),
    CRED_CARD(3, "CRED CARD");

    private final Integer code;
    private final String description;

    PaymentMethod(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}

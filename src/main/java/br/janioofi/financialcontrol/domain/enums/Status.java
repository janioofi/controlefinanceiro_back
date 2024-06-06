package br.janioofi.financialcontrol.domain.enums;

public enum Status {
    PENDING(0, "PENDING"),
    APPROVED(1, "APPROVED"),
    CANCELED(2, "CANCELED");

    private final Integer code;
    private final String description;

    Status(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}

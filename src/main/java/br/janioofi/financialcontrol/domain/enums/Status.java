package br.janioofi.financialcontrol.domain.enums;

import lombok.Getter;

@Getter
public enum Status {

    PENDING(0, "Pendente"),
    APPROVED(1, "Aprovado"),
    CANCELED(2, "Cancelado");

    private final Integer code;
    private final String description;

    Status(Integer code, String description) {
        this.code = code;
        this.description = description;
    }
}

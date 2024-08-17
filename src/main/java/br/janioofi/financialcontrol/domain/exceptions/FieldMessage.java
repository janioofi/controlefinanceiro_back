package br.janioofi.financialcontrol.domain.exceptions;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class FieldMessage implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private String fieldName;
    private String message;

    public FieldMessage(String fieldName, String message) {
        this.fieldName = fieldName;
        this.message = message;
    }
}
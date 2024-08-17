package br.janioofi.financialcontrol.domain.exceptions;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class ValidationErrors implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private LocalDateTime timestamp = LocalDateTime.now();
    private String error;
    private String path;
    private Integer status;
    private List<FieldMessage> errors = new ArrayList<>();

    public void addErrors(String fieldName, String message){
        this.errors.add(new FieldMessage(fieldName, message));
    }
}
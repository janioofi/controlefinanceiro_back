package br.janioofi.financialcontrol.controllers;

import br.janioofi.financialcontrol.domain.exceptions.ResourceNotFoundException;
import br.janioofi.financialcontrol.domain.exceptions.ValidationErrors;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApplicationControllerAdvice {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFoundException(ResourceNotFoundException ex){
        log.error(ex.getMessage());
        return ex.getMessage();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrors validationErrors(ConstraintViolationException ex, HttpServletRequest request){
        ValidationErrors field = new ValidationErrors();
        field.setPath(request.getRequestURI());
        field.setStatus(HttpStatus.BAD_REQUEST.value());
        field.setError("Validation Error");
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            field.addErrors("Constraint Violation Exception", violation.getMessage());
        }
        log.error(field.toString());
        return field;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        log.error(ex.getMessage());
        return ex.getMessage();
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String dataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.error(ex.getMessage());
        return ex.getMessage();
    }
}

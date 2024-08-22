package br.janioofi.financialcontrol.controllers;

import br.janioofi.financialcontrol.domain.exceptions.ResourceNotFoundException;
import br.janioofi.financialcontrol.domain.exceptions.ValidationErrors;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class ApplicationControllerAdviceTest {

    @InjectMocks
    private ApplicationControllerAdvice applicationControllerAdvice;

    @Mock
    private HttpServletRequest request;

    @Mock
    private ConstraintViolation<?> constraintViolation;

    @Mock
    private Path propertyPath;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testHandleNotFoundException() {
        ResourceNotFoundException ex = new ResourceNotFoundException("Resource not found");
        String response = applicationControllerAdvice.handleNotFoundException(ex);
        assertEquals("Resource not found", response);
    }

    @Test
    void testValidationErrors() {
        when(request.getRequestURI()).thenReturn("/test/uri");
        when(constraintViolation.getPropertyPath()).thenReturn(propertyPath);
        when(propertyPath.toString()).thenReturn("fieldName");
        when(constraintViolation.getMessage()).thenReturn("Validation error");

        Set<ConstraintViolation<?>> violations = new HashSet<>();
        violations.add(constraintViolation);

        ConstraintViolationException ex = new ConstraintViolationException(violations);

        ValidationErrors response = applicationControllerAdvice.validationErrors(ex, request);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatus());
        assertEquals("/test/uri", response.getPath());
        assertEquals("Validation Error", response.getError());
        assertEquals(1, response.getErrors().size());
        assertEquals("Constraint Violation Exception", response.getErrors().get(0).getFieldName());
        assertEquals("Validation error", response.getErrors().get(0).getMessage());
    }

    @Test
    void testHandleHttpMessageNotReadableException() {
        HttpMessageNotReadableException ex = new HttpMessageNotReadableException("Message not readable");
        String response = applicationControllerAdvice.handleHttpMessageNotReadableException(ex);
        assertEquals("Message not readable", response);
    }

    @Test
    void testDataIntegrityViolationException() {
        DataIntegrityViolationException ex = new DataIntegrityViolationException("Data integrity violation");
        String response = applicationControllerAdvice.dataIntegrityViolationException(ex);
        assertEquals("Data integrity violation", response);
    }
}

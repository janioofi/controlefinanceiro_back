package br.janioofi.financialcontrol.controllers;

import br.janioofi.financialcontrol.domain.dtos.PaymentRequestDto;
import br.janioofi.financialcontrol.domain.dtos.PaymentResponseDto;
import br.janioofi.financialcontrol.domain.services.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
@Tag(name = "Payment", description = "Payment API")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService service;
    private static final String ID  = "/{id}";

    @GetMapping
    @Operation(summary = "Search all payments in the system")
    public ResponseEntity<List<PaymentResponseDto>> findAll(HttpServletResponse response){
        return ResponseEntity.ok().body(service.findAll(response));
    }

    @GetMapping(ID)
    @Operation(summary = "Search payments by id")
    public ResponseEntity<PaymentResponseDto> findById(
            @PathVariable Long id,
            HttpServletResponse response){
        return ResponseEntity.ok().body(service.findById(id, response));
    }

    @PostMapping
    @Operation(summary = "Create a new payment")
    public ResponseEntity<PaymentResponseDto> create(
            @RequestBody PaymentRequestDto paymentRequestDto,
            HttpServletResponse response){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(paymentRequestDto, response));
    }

    @DeleteMapping(ID)
    @Operation(summary = "Delete a payment")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            HttpServletResponse response){
        service.delete(id, response);
        return ResponseEntity.ok().build();
    }

    @PutMapping(ID)
    @Operation(summary = "Update a payment")
    public ResponseEntity<PaymentResponseDto> update(
            @RequestBody PaymentRequestDto paymentRequestDto,
            @PathVariable Long id,
            HttpServletResponse response){
        return ResponseEntity.ok().body(service.update(id, paymentRequestDto, response));
    }

    @GetMapping("/period")
    @Operation(summary = "Searches for payments for a specified period")
    public ResponseEntity<List<PaymentResponseDto>> findPaymentsByPeriod(
            @RequestParam LocalDate initialDate,
            @RequestParam LocalDate finalDate,
            HttpServletResponse response){
        return ResponseEntity.ok().body(service.findPaymentsByPeriod(initialDate, finalDate, response));
    }
}

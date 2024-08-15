package br.janioofi.financialcontrol.controllers;

import br.janioofi.financialcontrol.domain.dtos.PaymentRequestDto;
import br.janioofi.financialcontrol.domain.dtos.PaymentResponseDto;
import br.janioofi.financialcontrol.domain.services.PaymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payment")
@Tag(name = "Payment", description = "Payment API")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService service;

    @GetMapping
    public ResponseEntity<List<PaymentResponseDto>> findAll(HttpServletResponse response){
        return ResponseEntity.ok().body(service.findAll(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponseDto> findById(
            @PathVariable Long id,
            HttpServletResponse response){
        return ResponseEntity.ok().body(service.findById(id, response));
    }

    @PostMapping
    public ResponseEntity<PaymentResponseDto> create(
            @RequestBody PaymentRequestDto paymentRequestDto,
            HttpServletResponse response){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(paymentRequestDto, response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            HttpServletResponse response){
        service.delete(id, response);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentResponseDto> update(
            @RequestBody PaymentRequestDto paymentRequestDto,
            @PathVariable Long id,
            HttpServletResponse response){
        return ResponseEntity.ok().body(service.update(id, paymentRequestDto, response));
    }

    @GetMapping("/period")
    public ResponseEntity<List<PaymentResponseDto>> findPaymentsByPeriod(
            @RequestParam String initialDate,
            @RequestParam String finalDate,
            HttpServletResponse response){
        return ResponseEntity.ok().body(service.findPaymentsByPeriod(initialDate, finalDate, response));
    }
}

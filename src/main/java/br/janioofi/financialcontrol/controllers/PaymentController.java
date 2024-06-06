package br.janioofi.financialcontrol.controllers;

import br.janioofi.financialcontrol.domain.dtos.PaymentDto;
import br.janioofi.financialcontrol.domain.services.PaymentService;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    public ResponseEntity<List<PaymentDto>> findAll(){
        return ResponseEntity.ok().body(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDto> findById(@PathVariable Long id){
        return ResponseEntity.ok().body(service.findById(id));
    }

    @PostMapping
    public ResponseEntity<PaymentDto> create(@RequestBody PaymentDto paymentDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(paymentDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentDto> update(@RequestBody PaymentDto paymentDto, @PathVariable Long id){
        return ResponseEntity.ok().body(service.update(id, paymentDto));
    }

    @GetMapping("/period")
    public ResponseEntity<List<PaymentDto>> findPaymentsByPeriod(@RequestParam String initialDate, @RequestParam String finalDate){
        return ResponseEntity.ok().body(service.findPaymentsByPeriod(initialDate, finalDate));
    }
}

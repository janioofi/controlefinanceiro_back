package br.janioofi.financialcontrol.domain.services;

import br.janioofi.financialcontrol.domain.dtos.Mapper;
import br.janioofi.financialcontrol.domain.dtos.PaymentRequestDto;
import br.janioofi.financialcontrol.domain.dtos.PaymentResponseDto;
import br.janioofi.financialcontrol.domain.entities.Payment;
import br.janioofi.financialcontrol.domain.exceptions.ResourceNotFoundException;
import br.janioofi.financialcontrol.domain.repositories.PaymentRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final PaymentRepository repository;
    private static final String NO_PAYMENT = "No payments were found with: ";

    public List<PaymentResponseDto> findAll(HttpServletResponse response){
        log.info("Listing payments.");
        return repository.findAll().stream().map(Mapper::toDto).toList();
    }

    public PaymentResponseDto findById(Long id, HttpServletResponse response){
        log.info("Seeking payment with ID: {}", id);
        return repository.findById(id).map(Mapper::toDto).orElseThrow(() -> new ResourceNotFoundException(NO_PAYMENT + id));
    }

    public PaymentResponseDto create(PaymentRequestDto paymentRequestDto, HttpServletResponse response){
        Payment payment = new Payment();

        payment.setPaymentDate(paymentRequestDto.paymentDate());
        payment.setDescription(paymentRequestDto.description());
        payment.setCategory(paymentRequestDto.category());
        payment.setValue(paymentRequestDto.value());
        payment.setStatus(paymentRequestDto.status());
        payment.setPaymentMethod(paymentRequestDto.paymentMethod());
        log.info("Creating new payment: {}", paymentRequestDto.description());

        return Mapper.toDto(repository.save(payment));
    }

    public PaymentResponseDto update(Long id, PaymentRequestDto paymentRequestDto, HttpServletResponse response){
        Payment payment = repository.findById(id).map(data -> {
            data.setStatus(paymentRequestDto.status());
            data.setPaymentDate(paymentRequestDto.paymentDate());
            data.setDescription(paymentRequestDto.description());
            data.setCategory(paymentRequestDto.category());
            data.setValue(paymentRequestDto.value());
            data.setPaymentMethod(paymentRequestDto.paymentMethod());
            return repository.save(data);
        }).orElseThrow(() -> new ResourceNotFoundException(NO_PAYMENT + id));
        log.info("Performing an update to payment with ID: {}", id);
        return Mapper.toDto(payment);
    }

    public void delete(Long id, HttpServletResponse response) {
        validateDelete(id);
        log.info("Deleting payment with ID: {}", id);
        repository.deleteById(id);
    }

    public List<PaymentResponseDto> findPaymentsByPeriod(String initialDate, String finalDate, HttpServletResponse response){
        List<Payment> payments = repository.findPaymentsByPeriod(initialDate, finalDate);
        return payments.stream().map(Mapper::toDto).toList();
    }

    private void validateDelete(Long id){
        Optional<Payment> payment = repository.findById(id);
        if (payment.isEmpty()) throw new ResourceNotFoundException(NO_PAYMENT + id);
    }
}

package br.janioofi.financialcontrol.domain.services;

import br.janioofi.financialcontrol.domain.dtos.Mapper;
import br.janioofi.financialcontrol.domain.dtos.PaymentRequestDto;
import br.janioofi.financialcontrol.domain.dtos.PaymentResponseDto;
import br.janioofi.financialcontrol.domain.entities.Payment;
import br.janioofi.financialcontrol.domain.entities.User;
import br.janioofi.financialcontrol.domain.exceptions.ResourceNotFoundException;
import br.janioofi.financialcontrol.domain.repositories.PaymentRepository;
import br.janioofi.financialcontrol.domain.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest; // Use HttpServletRequest em vez de HttpServletResponse
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final PaymentRepository repository;
    private final UserRepository userRepository;
    private static final String NO_PAYMENT = "No payments were found with ID: ";
    private static final String NO_USER_USERNAME = "No user found with Username: ";
    private static final String USER = "User-Agent"; // Verifique se este é o cabeçalho correto

    public List<PaymentResponseDto> findAll(HttpServletRequest request) {
        log.info("Listing payments.");
        return repository.findAllByUser(findUser(request)).stream().map(Mapper::toDto).toList();
    }

    public PaymentResponseDto findById(Long id, HttpServletRequest request) {
        log.info("Seeking payment with ID: {}", id);
        return repository.findByIdPaymentAndUser(id, findUser(request))
                .map(Mapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException(NO_PAYMENT + id));
    }

    public PaymentResponseDto create(PaymentRequestDto paymentRequestDto, HttpServletRequest request) {
        Payment payment = new Payment();
        payment.setPaymentDate(paymentRequestDto.paymentDate());
        payment.setDescription(paymentRequestDto.description());
        payment.setCategory(paymentRequestDto.category());
        payment.setValue(paymentRequestDto.value());
        payment.setStatus(paymentRequestDto.status());
        payment.setPaymentMethod(paymentRequestDto.paymentMethod());
        payment.setUser(findUser(request));
        log.info("Creating new payment: {}", paymentRequestDto.description());

        return Mapper.toDto(repository.save(payment));
    }

    public PaymentResponseDto update(Long id, PaymentRequestDto paymentRequestDto, HttpServletRequest request) {
        Payment payment = repository.findByIdPaymentAndUser(id, findUser(request)).map(data -> {
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

    public void delete(Long id, HttpServletRequest request) {
        validateDelete(id, findUser(request));
        log.info("Deleting payment with ID: {}", id);
        repository.deleteById(id);
    }

    public List<PaymentResponseDto> findPaymentsByPeriod(LocalDate initialDate, LocalDate finalDate, HttpServletRequest request) {
        List<Payment> payments = repository.findByPaymentDateBetweenAndUser(initialDate, finalDate, findUser(request));
        log.info("Seeking payments for the period {} to {}", initialDate, finalDate);
        return payments.stream().map(Mapper::toDto).toList();
    }

    private User findUser(HttpServletRequest request) {
        String username = request.getHeader(USER);
        if (username == null) {
            throw new ResourceNotFoundException("User-Agent header is missing");
        }
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(NO_USER_USERNAME + username));
    }

    private void validateDelete(Long id, User user) {
        Optional<Payment> payment = repository.findByIdPaymentAndUser(id, user);
        if (payment.isEmpty()) {
            throw new ResourceNotFoundException(NO_PAYMENT + id);
        }
    }
}

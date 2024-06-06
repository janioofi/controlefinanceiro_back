package br.janioofi.financialcontrol.domain.services;

import br.janioofi.financialcontrol.domain.dtos.PaymentDto;
import br.janioofi.financialcontrol.domain.entity.Category;
import br.janioofi.financialcontrol.domain.entity.Payment;
import br.janioofi.financialcontrol.domain.exceptions.RecordNotFoundException;
import br.janioofi.financialcontrol.domain.repositories.CategoryRepository;
import br.janioofi.financialcontrol.domain.repositories.PaymentRepository;
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
    private final CategoryRepository categoryRepository;
    private static final String NO_PAYMENT = "No payments were found with: ";
    private static final String NO_CATEGORIES = "No categories were found with: ";

    public List<PaymentDto> findAll(){
        log.info("Listing payments.");
        return repository.findAll().stream().map(this::toDto).toList();
    }

    public PaymentDto findById(Long id){
        log.info("Seeking payment with ID: " + id);
        return this.toDto(repository.findById(id).orElseThrow(() -> new RecordNotFoundException(NO_PAYMENT + id)));
    }

    public PaymentDto create(PaymentDto paymentDto){
        Payment payment = new Payment();
        Category category = categoryRepository.findById(paymentDto.idCategory()).orElseThrow(() -> new RecordNotFoundException(NO_CATEGORIES + paymentDto.idCategory()));

        payment.setPaymentDate(paymentDto.paymentDate());
        payment.setDescription(paymentDto.description());
        payment.setCategory(category);
        payment.setValue(paymentDto.value());
        payment.setStatus(paymentDto.status());
        log.info("Creating new payment: " + paymentDto.description());

        return this.toDto(repository.save(payment));
    }

    public PaymentDto update(Long id,PaymentDto paymentDto){
        Category category = categoryRepository.findById(paymentDto.idCategory()).orElseThrow(() -> new RecordNotFoundException(NO_CATEGORIES + paymentDto.idCategory()));
        Payment payment = repository.findById(id).map(data -> {
            data.setStatus(paymentDto.status());
            data.setPaymentDate(paymentDto.paymentDate());
            data.setDescription(paymentDto.description());
            data.setCategory(category);
            data.setValue(paymentDto.value());
            return repository.save(data);
        }).orElseThrow(() -> new RecordNotFoundException(NO_PAYMENT + id));
        return this.toDto(payment);
    }

    public void delete(Long id){
        Optional<Payment> payment = repository.findById(id);
        if(payment.isEmpty()) throw new RecordNotFoundException(NO_PAYMENT + id);
        repository.deleteById(id);
    }

    private PaymentDto toDto(Payment payment){
        return new PaymentDto(payment.getIdPayment(), payment.getDescription(),
                payment.getPaymentDate(), payment.getValue(), payment.getCategory().getDescription(),
                payment.getCategory().getIdCategory(), payment.getStatus());
    }
}

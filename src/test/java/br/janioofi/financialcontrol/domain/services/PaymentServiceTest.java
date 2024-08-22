package br.janioofi.financialcontrol.domain.services;

import br.janioofi.financialcontrol.domain.dtos.PaymentRequestDto;
import br.janioofi.financialcontrol.domain.dtos.PaymentResponseDto;
import br.janioofi.financialcontrol.domain.entities.Payment;
import br.janioofi.financialcontrol.domain.entities.User;
import br.janioofi.financialcontrol.domain.enums.Category;
import br.janioofi.financialcontrol.domain.enums.PaymentMethod;
import br.janioofi.financialcontrol.domain.enums.Status;
import br.janioofi.financialcontrol.domain.repositories.PaymentRepository;
import br.janioofi.financialcontrol.domain.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Mock
    private HttpServletRequest request;

    private static final String USER_AGENT = "User-Agent";
    private static final String USERNAME = "username";

    private User mockUser(Long userId) {
        User user = new User();
        user.setIdUser(userId);
        return user;
    }

    private Payment mockPayment(User user, Long paymentId) {
        Payment payment = new Payment();
        payment.setIdPayment(paymentId);
        payment.setUser(user);
        payment.setCategory(Category.LEISURE);
        payment.setStatus(Status.PENDING);
        payment.setPaymentMethod(PaymentMethod.MONEY);
        return payment;
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll() {
        User user = mockUser(1L);
        Payment payment = mockPayment(user, 1L);
        List<Payment> payments = List.of(payment);

        when(request.getHeader(USER_AGENT)).thenReturn(USERNAME);
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(paymentRepository.findAllByUser(user)).thenReturn(payments);

        List<PaymentResponseDto> responseDtos = paymentService.findAll(request);

        assertEquals(1, responseDtos.size());
        assertNotNull(responseDtos.get(0));
        verify(paymentRepository).findAllByUser(user);
    }

    @Test
    void testFindById() {
        User user = mockUser(1L);
        Payment payment = mockPayment(user, 1L);

        when(request.getHeader(USER_AGENT)).thenReturn(USERNAME);
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(paymentRepository.findByIdPaymentAndUser(payment.getIdPayment(), user)).thenReturn(Optional.of(payment));

        PaymentResponseDto responseDto = paymentService.findById(payment.getIdPayment(), request);

        assertNotNull(responseDto);
        assertEquals(payment.getIdPayment(), responseDto.idPayment());
        verify(paymentRepository).findByIdPaymentAndUser(payment.getIdPayment(), user);
    }

    @Test
    void testCreate() {
        User user = mockUser(1L);
        PaymentRequestDto requestDto = new PaymentRequestDto(
                "description",
                LocalDate.now(),
                BigDecimal.ONE,
                Category.LEISURE,
                Status.PENDING,
                PaymentMethod.MONEY
        );

        when(request.getHeader(USER_AGENT)).thenReturn(USERNAME);
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));

        Payment payment = new Payment();
        payment.setPaymentDate(requestDto.paymentDate());
        payment.setDescription(requestDto.description());
        payment.setCategory(requestDto.category());
        payment.setValue(requestDto.value());
        payment.setStatus(requestDto.status());
        payment.setPaymentMethod(requestDto.paymentMethod());
        payment.setUser(user);

        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        PaymentResponseDto responseDto = paymentService.create(requestDto, request);

        assertNotNull(responseDto);
        assertEquals(requestDto.description(), responseDto.description());
        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    void testUpdate() {
        User user = mockUser(1L);
        Payment existingPayment = mockPayment(user, 1L);

        PaymentRequestDto requestDto = new PaymentRequestDto(
                "description",
                LocalDate.now(),
                BigDecimal.ONE,
                Category.LEISURE,
                Status.PENDING,
                PaymentMethod.MONEY
        );

        when(request.getHeader(USER_AGENT)).thenReturn(USERNAME);
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(paymentRepository.findByIdPaymentAndUser(existingPayment.getIdPayment(), user)).thenReturn(Optional.of(existingPayment));

        Payment updatedPayment = new Payment();
        updatedPayment.setIdPayment(existingPayment.getIdPayment());
        updatedPayment.setPaymentDate(requestDto.paymentDate());
        updatedPayment.setDescription(requestDto.description());
        updatedPayment.setCategory(requestDto.category());
        updatedPayment.setValue(requestDto.value());
        updatedPayment.setStatus(requestDto.status());
        updatedPayment.setPaymentMethod(requestDto.paymentMethod());
        updatedPayment.setUser(user);

        when(paymentRepository.save(any(Payment.class))).thenReturn(updatedPayment);

        PaymentResponseDto responseDto = paymentService.update(existingPayment.getIdPayment(), requestDto, request);

        assertNotNull(responseDto);
        assertEquals(requestDto.description(), responseDto.description());
        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    void testDelete() {
        User user = mockUser(1L);
        Payment payment = mockPayment(user, 1L);

        when(request.getHeader(USER_AGENT)).thenReturn(USERNAME);
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(paymentRepository.findByIdPaymentAndUser(payment.getIdPayment(), user)).thenReturn(Optional.of(payment));

        doNothing().when(paymentRepository).deleteById(payment.getIdPayment());

        paymentService.delete(payment.getIdPayment(), request);

        verify(paymentRepository).deleteById(payment.getIdPayment());
    }

    @Test
    void testFindPaymentsByPeriod() {
        User user = mockUser(1L);
        LocalDate startDate = LocalDate.now().minusDays(10);
        LocalDate endDate = LocalDate.now();
        Payment payment = mockPayment(user, 1L);
        List<Payment> payments = List.of(payment);

        when(request.getHeader(USER_AGENT)).thenReturn(USERNAME);
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(paymentRepository.findByPaymentDateBetweenAndUser(startDate, endDate, user)).thenReturn(payments);

        List<PaymentResponseDto> responseDtos = paymentService.findPaymentsByPeriod(startDate, endDate, request);

        assertEquals(1, responseDtos.size());
        assertNotNull(responseDtos.get(0));
        verify(paymentRepository).findByPaymentDateBetweenAndUser(startDate, endDate, user);
    }
}

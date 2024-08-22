package br.janioofi.financialcontrol.controllers;

import br.janioofi.financialcontrol.domain.dtos.PaymentRequestDto;
import br.janioofi.financialcontrol.domain.dtos.PaymentResponseDto;
import br.janioofi.financialcontrol.domain.enums.Category;
import br.janioofi.financialcontrol.domain.enums.PaymentMethod;
import br.janioofi.financialcontrol.domain.enums.Status;
import br.janioofi.financialcontrol.domain.services.PaymentService;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class PaymentControllerTest {

    private static final Long ID = 1L;
    private static final String DESCRIPTION = "Desc";
    private static final LocalDate DATE = LocalDate.now();
    private static final BigDecimal VALUE = BigDecimal.valueOf(100.00);
    private static final Status STATUS = Status.PENDING;
    private static final Category CATEGORY = Category.LEISURE;
    private static final PaymentMethod METHOD = PaymentMethod.MONEY;

    @InjectMocks
    private PaymentController paymentController;

    @Mock
    private PaymentService paymentService;

    @Mock
    private HttpServletResponse response;

    private PaymentRequestDto paymentRequestDto;
    private PaymentResponseDto paymentResponseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startTarefa();
        paymentController = new PaymentController(paymentService);
    }

    @Test
    void testFindAll() {
        when(paymentService.findAll(any())).thenReturn(Arrays.asList(paymentResponseDto));

        ResponseEntity<List<PaymentResponseDto>> response = paymentController.findAll(this.response);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(paymentService, times(1)).findAll(any(HttpServletResponse.class));
    }

    @Test
    void testFindById() {
        when(paymentService.findById(anyLong(), any(HttpServletResponse.class))).thenReturn(paymentResponseDto);

        ResponseEntity<PaymentResponseDto> response = paymentController.findById(1L, this.response);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(paymentResponseDto, response.getBody());
        verify(paymentService, times(1)).findById(anyLong(), any(HttpServletResponse.class));
    }

    @Test
    void testCreate() {
        when(paymentService.create(any(PaymentRequestDto.class), any(HttpServletResponse.class))).thenReturn(paymentResponseDto);

        ResponseEntity<PaymentResponseDto> response = paymentController.create(paymentRequestDto, this.response);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(paymentResponseDto, response.getBody());
        verify(paymentService, times(1)).create(any(PaymentRequestDto.class), any(HttpServletResponse.class));
    }

    @Test
    void testDelete() {
        doNothing().when(paymentService).delete(anyLong(), any(HttpServletResponse.class));

        ResponseEntity<Void> response = paymentController.delete(1L, this.response);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(paymentService, times(1)).delete(anyLong(), any(HttpServletResponse.class));
    }

    @Test
    void testUpdate() {
        when(paymentService.update(anyLong(), any(PaymentRequestDto.class), any(HttpServletResponse.class))).thenReturn(paymentResponseDto);

        ResponseEntity<PaymentResponseDto> response = paymentController.update( paymentRequestDto, 1L, this.response);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(paymentResponseDto, response.getBody());
        verify(paymentService, times(1)).update(anyLong(), any(PaymentRequestDto.class), any(HttpServletResponse.class));
    }

    @Test
    void testFindPaymentsByPeriod() {
        when(paymentService.findPaymentsByPeriod(any(LocalDate.class), any(LocalDate.class), any(HttpServletResponse.class)))
                .thenReturn(Arrays.asList(paymentResponseDto));

        ResponseEntity<List<PaymentResponseDto>> response = paymentController.findPaymentsByPeriod(
                LocalDate.now().minusDays(1), LocalDate.now(), this.response);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        verify(paymentService, times(1)).findPaymentsByPeriod(any(LocalDate.class), any(LocalDate.class), any(HttpServletResponse.class));
    }

    private void startTarefa() {
        paymentRequestDto = new PaymentRequestDto(DESCRIPTION, DATE, VALUE, CATEGORY, STATUS, METHOD);

        paymentResponseDto = new PaymentResponseDto(ID, DESCRIPTION, DATE, VALUE, CATEGORY.getDescription(), STATUS.getDescription(), METHOD.getDescription(), "test");
    }
}
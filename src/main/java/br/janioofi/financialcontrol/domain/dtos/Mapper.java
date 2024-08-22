package br.janioofi.financialcontrol.domain.dtos;

import br.janioofi.financialcontrol.domain.entities.Payment;
import br.janioofi.financialcontrol.domain.entities.User;
import br.janioofi.financialcontrol.domain.exceptions.ResourceNotFoundException;

public class Mapper {
    private Mapper(){}

    public static PaymentResponseDto toDto(Payment payment){
        if (payment == null) {
            throw new ResourceNotFoundException("Payment not found");
        }
        return new PaymentResponseDto(
                payment.getIdPayment(),
                payment.getDescription(),
                payment.getPaymentDate(),
                payment.getValue(),
                payment.getCategory().getDescription(),
                payment.getStatus().getDescription(),
                payment.getPaymentMethod().getDescription(),
                payment.getUser().getUsername());
    }

    public static UserResponseDto toDto(User user){
        if (user == null) {
            throw new ResourceNotFoundException("User not found");
        }
        return new UserResponseDto(user.getIdUser(), user.getUsername());
    }


}

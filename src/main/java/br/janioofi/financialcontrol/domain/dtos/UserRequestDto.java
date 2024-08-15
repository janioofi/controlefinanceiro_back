package br.janioofi.financialcontrol.domain.dtos;

import jakarta.validation.constraints.NotBlank;

public record UserRequestDto(
        @NotBlank(message = "E-mail is mandatory") String email,
        @NotBlank(message = "Password is mandatory") String password
) {
}

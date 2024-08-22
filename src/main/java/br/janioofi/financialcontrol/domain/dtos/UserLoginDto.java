package br.janioofi.financialcontrol.domain.dtos;

import jakarta.validation.constraints.NotBlank;

public record UserLoginDto(
        @NotBlank(message = "Username is mandatory") String username,
        @NotBlank(message = "Password is mandatory") String password
) {
}

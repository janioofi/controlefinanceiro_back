package br.janioofi.financialcontrol.domain.dtos;

import jakarta.validation.constraints.NotBlank;

public record UserRegisterDto(
        @NotBlank(message = "Usuário é obrigatório") String username,
        @NotBlank(message = "Senha é obrigatório") String password,
        @NotBlank(message = "Confirmação de senha é obrigatório") String confirmPassword

) {
}

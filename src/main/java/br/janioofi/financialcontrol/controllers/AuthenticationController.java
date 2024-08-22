package br.janioofi.financialcontrol.controllers;

import br.janioofi.financialcontrol.domain.dtos.LoginResponseDto;
import br.janioofi.financialcontrol.domain.dtos.UserLoginDto;
import br.janioofi.financialcontrol.domain.dtos.UserRegisterDto;
import br.janioofi.financialcontrol.domain.services.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication API")
public class AuthenticationController {
    private final AuthenticationService service;

    @PostMapping("/login")
    @Operation(summary = "Log in to the system")
    public ResponseEntity<LoginResponseDto> login(@RequestBody UserLoginDto user, HttpServletResponse res){
        LoginResponseDto response = service.login(user);
        res.addHeader("Authorization", "Bearer " + response.token());
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/register")
    @Operation(summary = "Register in the system")
    public ResponseEntity<String> register(@RequestBody UserRegisterDto user){
        return ResponseEntity.status(HttpStatus.CREATED).body(service.register(user));
    }
}
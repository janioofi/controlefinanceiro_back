package br.janioofi.financialcontrol.domain.services;

import br.janioofi.financialcontrol.domain.dtos.LoginResponseDto;
import br.janioofi.financialcontrol.domain.dtos.UserRequestDto;
import br.janioofi.financialcontrol.domain.entities.User;
import br.janioofi.financialcontrol.domain.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository repository;
    private final TokenService tokenService;

    public LoginResponseDto login(@Valid UserRequestDto user){
        validaRegistro(user);
        var usuarioSenha = new UsernamePasswordAuthenticationToken(user.email(), user.password());
        var auth = authenticationManager.authenticate(usuarioSenha);
        var token = tokenService.generateToken((User) auth.getPrincipal());

        log.info("{} Logging in", user.email());
        return new LoginResponseDto(token);
    }

    public String register(@Valid UserRequestDto user){
        validaRegistro(user);
        if(this.repository.findByEmail(user.email()) != null){
            throw new DataIntegrityViolationException("There is already a registered user with the same name");
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(user.password());
        User data = new User(null, user.email(), encryptedPassword);
        repository.save(data);
        log.info("{} registered", user.email());
        return "User registered successfully";
    }

    private void validaRegistro(UserRequestDto user){
        if(user.email().isEmpty() ||  user.password().isEmpty()){
            throw new DataIntegrityViolationException("All fields need to be filled in\n");
        }
    }
}
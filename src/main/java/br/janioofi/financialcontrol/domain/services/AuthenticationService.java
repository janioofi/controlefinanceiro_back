package br.janioofi.financialcontrol.domain.services;

import br.janioofi.financialcontrol.domain.dtos.LoginResponseDto;
import br.janioofi.financialcontrol.domain.dtos.UserLoginDto;
import br.janioofi.financialcontrol.domain.dtos.UserRegisterDto;
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

    public LoginResponseDto login(@Valid UserLoginDto user){
        validateLogin(user);
        var usuarioSenha = new UsernamePasswordAuthenticationToken(user.username(), user.password());
        var auth = authenticationManager.authenticate(usuarioSenha);
        var token = tokenService.generateToken((User) auth.getPrincipal());

        log.info("{} Logging in", user.username());
        return new LoginResponseDto(token);
    }

    public String register(@Valid UserRegisterDto user){
        validatePassword(user);
        validateRegister(user);
        String encryptedPassword = new BCryptPasswordEncoder().encode(user.password());
        User data = new User(null, user.username(), encryptedPassword);
        repository.save(data);
        log.info("{} registered", user.username());
        return "Usuário registrado com sucesso";
    }

    private void validatePassword(UserRegisterDto user){
        if(!user.password().equals(user.confirmPassword())){
            throw new DataIntegrityViolationException("As senhas precisam ser iguais!");
        }
    }

    private void validateRegister(UserRegisterDto user){
        if(this.repository.findByUsername(user.username()).isPresent()){
            throw new DataIntegrityViolationException("Já existe um usuário cadastrado com o mesmo username");
        }
        if(user.username().isEmpty() ||  user.password().isEmpty() || user.confirmPassword().isEmpty()){
            throw new DataIntegrityViolationException("Todos os campos precisam ser preenchidos");
        }
    }

    private void validateLogin(UserLoginDto user){
        if(user.username().isEmpty() ||  user.password().isEmpty()){
            throw new DataIntegrityViolationException("Todos os campos precisam ser preenchidos");
        }
    }
}
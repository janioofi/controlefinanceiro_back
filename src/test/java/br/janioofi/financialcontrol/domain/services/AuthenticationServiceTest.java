package br.janioofi.financialcontrol.domain.services;

import br.janioofi.financialcontrol.domain.dtos.LoginResponseDto;
import br.janioofi.financialcontrol.domain.dtos.UserLoginDto;
import br.janioofi.financialcontrol.domain.dtos.UserRegisterDto;
import br.janioofi.financialcontrol.domain.entities.User;
import br.janioofi.financialcontrol.domain.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AuthenticationServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository repository;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void login_ValidUser_ReturnsToken() {
        // Arrange
        UserLoginDto userLoginDto = new UserLoginDto("username", "password");
        Authentication authentication = mock(Authentication.class);
        String token = "token";
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(tokenService.generateToken(any(User.class))).thenReturn(token);
        when(authentication.getPrincipal()).thenReturn(new User(1L, "username", "encodedPassword"));

        // Act
        LoginResponseDto response = authenticationService.login(userLoginDto);

        // Assert
        assertEquals(token, response.token());
    }

    @Test
    void register_ValidUser_ReturnsSuccessMessage() {
        // Arrange
        UserRegisterDto userRegisterDto = new UserRegisterDto("username", "password", "password");
        when(repository.findByUsername(userRegisterDto.username())).thenReturn(Optional.empty());
        String encryptedPassword = new BCryptPasswordEncoder().encode(userRegisterDto.password());
        User user = new User(null, userRegisterDto.username(), encryptedPassword);
        when(repository.save(any(User.class))).thenReturn(user);

        // Act
        String result = authenticationService.register(userRegisterDto);

        // Assert
        assertEquals("Usuário registrado com sucesso", result);
    }

    @Test
    void register_UsernameAlreadyExists_ThrowsDataIntegrityViolationException() {
        UserRegisterDto userRegisterDto = new UserRegisterDto("username", "password", "password");
        when(repository.findByUsername(userRegisterDto.username())).thenReturn(Optional.of(new User()));

        DataIntegrityViolationException thrown = assertThrows(
                DataIntegrityViolationException.class,
                () -> authenticationService.register(userRegisterDto),
                "Expected register() to throw DataIntegrityViolationException, but it didn't"
        );
        assertEquals("Já existe um usuário cadastrado com o mesmo username", thrown.getMessage());
    }

    @Test
    void register_PasswordsDoNotMatch_ThrowsDataIntegrityViolationException() {
        // Arrange
        UserRegisterDto userRegisterDto = new UserRegisterDto("username", "password", "differentPassword");

        // Act & Assert
        DataIntegrityViolationException thrown = assertThrows(
                DataIntegrityViolationException.class,
                () -> authenticationService.register(userRegisterDto),
                "Expected register() to throw DataIntegrityViolationException, but it didn't"
        );
        assertEquals("As senhas precisam ser iguais!", thrown.getMessage());
    }

    @Test
    void register_EmptyFields_ThrowsDataIntegrityViolationException() {
        // Arrange
        UserRegisterDto userRegisterDto = new UserRegisterDto("", "", "");

        // Act & Assert
        DataIntegrityViolationException thrown = assertThrows(
                DataIntegrityViolationException.class,
                () -> authenticationService.register(userRegisterDto),
                "Expected register() to throw DataIntegrityViolationException, but it didn't"
        );
        assertEquals("Todos os campos precisam ser preenchidos", thrown.getMessage());
    }

    @Test
    void login_EmptyFields_ThrowsDataIntegrityViolationException() {
        // Arrange
        UserLoginDto userLoginDto = new UserLoginDto("", "");

        // Act & Assert
        DataIntegrityViolationException thrown = assertThrows(
                DataIntegrityViolationException.class,
                () -> authenticationService.login(userLoginDto),
                "Expected login() to throw DataIntegrityViolationException, but it didn't"
        );
        assertEquals("Todos os campos precisam ser preenchidos", thrown.getMessage());
    }
}

package br.janioofi.financialcontrol.controllers;

import br.janioofi.financialcontrol.domain.dtos.LoginResponseDto;
import br.janioofi.financialcontrol.domain.dtos.UserLoginDto;
import br.janioofi.financialcontrol.domain.dtos.UserRegisterDto;
import br.janioofi.financialcontrol.domain.services.AuthenticationService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
class AuthenticationControllerTest {

    private static final String USERNAME = "testuser";
    private static final String PASSWORD = "password";
    private static final String CONFIRM_PASSWORD = "password";
    private static final String TOKEN = "test-token";

    @InjectMocks
    private AuthenticationController authenticationController;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private HttpServletResponse response;

    private UserLoginDto userLoginDto;
    private UserRegisterDto userRegisterDto;
    private LoginResponseDto loginResponseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startAuth();
        authenticationController = new AuthenticationController(authenticationService);
    }

    @Test
    void testLogin() {
        when(authenticationService.login(any(UserLoginDto.class))).thenReturn(loginResponseDto);

        ResponseEntity<LoginResponseDto> responseEntity = authenticationController.login(userLoginDto, response);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(loginResponseDto, responseEntity.getBody());
        verify(authenticationService, times(1)).login(any(UserLoginDto.class));
        verify(response, times(1)).addHeader("Authorization", "Bearer " + TOKEN);
    }

    @Test
    void testRegister() {
        when(authenticationService.register(any(UserRegisterDto.class))).thenReturn("Registration successful");

        ResponseEntity<String> responseEntity = authenticationController.register(userRegisterDto);

        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals("Registration successful", responseEntity.getBody());
        verify(authenticationService, times(1)).register(any(UserRegisterDto.class));
    }

    private void startAuth() {
        userLoginDto = new UserLoginDto(USERNAME, PASSWORD);
        userRegisterDto = new UserRegisterDto(USERNAME, PASSWORD, CONFIRM_PASSWORD);
        loginResponseDto = new LoginResponseDto(TOKEN);
    }
}

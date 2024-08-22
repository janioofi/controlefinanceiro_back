package br.janioofi.financialcontrol.controllers;

import br.janioofi.financialcontrol.domain.dtos.UserRegisterDto;
import br.janioofi.financialcontrol.domain.dtos.UserResponseDto;
import br.janioofi.financialcontrol.domain.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
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

@ActiveProfiles("test")
@SpringBootTest
class UserControllerTest {

    private static final Long ID = 1L;
    private static final String USERNAME = "testuser";
    private static final String PASSWORD = "password";
    private static final String CONFIRM_PASSWORD = "password";

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    @Mock
    private HttpServletRequest response;

    private UserRegisterDto userRegisterDto;
    private UserResponseDto userResponseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        startUser();
        userController = new UserController(userService);
    }

    @Test
    void testDelete() {
        doNothing().when(userService).delete(anyLong(), any(HttpServletRequest.class));

        ResponseEntity<Void> response = userController.delete(ID, this.response);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userService, times(1)).delete(anyLong(), any(HttpServletRequest.class));
    }

    @Test
    void testUpdate() {
        when(userService.update(anyLong(), any(UserRegisterDto.class), any(HttpServletRequest.class)))
                .thenReturn(userResponseDto);

        ResponseEntity<UserResponseDto> response = userController.update(userRegisterDto, ID, this.response);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userResponseDto, response.getBody());
        verify(userService, times(1)).update(anyLong(), any(UserRegisterDto.class), any(HttpServletRequest.class));
    }

    @Test
    void testFindById() {
        when(userService.findById(anyLong(), any(HttpServletRequest.class))).thenReturn(userResponseDto);

        ResponseEntity<UserResponseDto> response = userController.findById(ID, this.response);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userResponseDto, response.getBody());
        verify(userService, times(1)).findById(anyLong(), any(HttpServletRequest.class));
    }

    @Test
    void testFindByUsername() {
        when(userService.findByUsername(anyString(), any(HttpServletRequest.class))).thenReturn(userResponseDto);

        ResponseEntity<UserResponseDto> response = userController.findByUsername(USERNAME, this.response);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(userResponseDto, response.getBody());
        verify(userService, times(1)).findByUsername(anyString(), any(HttpServletRequest.class));
    }

    private void startUser() {
        userRegisterDto = new UserRegisterDto(USERNAME, PASSWORD, CONFIRM_PASSWORD);
        userResponseDto = new UserResponseDto(ID, USERNAME);
    }
}

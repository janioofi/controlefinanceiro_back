package br.janioofi.financialcontrol.domain.services;

import br.janioofi.financialcontrol.domain.dtos.UserRegisterDto;
import br.janioofi.financialcontrol.domain.dtos.UserResponseDto;
import br.janioofi.financialcontrol.domain.entities.User;
import br.janioofi.financialcontrol.domain.exceptions.ResourceNotFoundException;
import br.janioofi.financialcontrol.domain.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateUser_Success() {
        UserRegisterDto dto = new UserRegisterDto("username", "password", "password");
        User existingUser = new User(1L, "username", new BCryptPasswordEncoder().encode("oldPassword"));
        User updatedUser = new User(1L, "username", new BCryptPasswordEncoder().encode("password"));

        when(request.getHeader("User-Agent")).thenReturn("username");
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        UserResponseDto result = userService.update(1L, dto, request);

        assertNotNull(result);
        assertEquals("username", result.username());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testUpdateUser_UserNotFound() {
        UserRegisterDto dto = new UserRegisterDto("username", "password", "password");

        when(request.getHeader("User-Agent")).thenReturn("username");
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.update(1L, dto, request));
    }

    @Test
    void testFindById_Success() {
        // Configurar o DTO com dados válidos
        User user = new User(1L, "username", "password");

        // Mockar o retorno do cabeçalho do HttpServletResponse
        when(request.getHeader("User-Agent")).thenReturn("username");

        // Mockar o retorno dos métodos do UserRepository
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));

        // Executar o método findById
        UserResponseDto result = userService.findById(1L, request);

        // Verificar os resultados
        assertNotNull(result);
        assertEquals("username", result.username());
    }

    @Test
    void testFindById_UserNotFound() {
        when(request.getHeader("User-Agent")).thenReturn("username");
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.findById(1L, request));
    }

    @Test
    void testFindByUsername_Success() {
        User user = new User(1L, "username", "password");
        when(request.getHeader("User-Agent")).thenReturn("username");
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));

        UserResponseDto result = userService.findByUsername("username", request);

        assertNotNull(result);
        assertEquals("username", result.username());
    }

    @Test
    void testFindByUsername_UserNotFound() {
        when(request.getHeader("User-Agent")).thenReturn("username");
        when(userRepository.findByUsername("username")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.findByUsername("username", request));
    }

    @Test
    void testDeleteUser_Success() {
        Long userId = 1L;
        User existingUser = new User(userId, "username", "password");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        doNothing().when(userRepository).deleteById(userId);
        when(request.getHeader("User-Agent")).thenReturn("username");
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(existingUser));
        userService.delete(userId, request);
        verify(userRepository).deleteById(userId);
    }


    @Test
    void testDeleteUser_UserNotFound() {
        when(request.getHeader("User-Agent")).thenReturn("username");
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.delete(1L, request));
    }

    @Test
    void testValidatePassword_PasswordMismatch() {
        UserRegisterDto dto = new UserRegisterDto("username", "password", "differentPassword");

        when(request.getHeader("User-Agent")).thenReturn("username");
        when(userRepository.findById(1L)).thenReturn(Optional.of(new User(1L, "username", "oldPassword")));
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(new User(1L, "username", "oldPassword")));

        assertThrows(DataIntegrityViolationException.class, () -> userService.update(1L, dto, request));
    }

    @Test
    void testValidateUpdate_UserExists() {
        Long userId = 1L;
        String username = "username";
        UserRegisterDto dto = new UserRegisterDto(username, "password", "password");
        User existingUser = new User();
        existingUser.setIdUser(userId);
        existingUser.setUsername(username);
        existingUser.setPassword("oldPassword");

        // Mock do HttpServletRequest
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("User-Agent")).thenReturn(username);

        // Mock do UserRepository
        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        // Verifique se o método update não lança exceção
        assertDoesNotThrow(() -> userService.update(userId, dto, request));
    }

}

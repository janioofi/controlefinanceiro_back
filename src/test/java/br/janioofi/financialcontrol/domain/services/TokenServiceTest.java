package br.janioofi.financialcontrol.domain.services;

import br.janioofi.financialcontrol.domain.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    private final String secret = "testSecret";

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);

        // Usando reflexão para injetar o valor de 'secret' na instância de 'tokenService'
        Field secretField = TokenService.class.getDeclaredField("secret");
        secretField.setAccessible(true);
        secretField.set(tokenService, secret);
    }

    @Test
    void testGenerateToken_Success() {
        User user = new User();
        user.setUsername("testUser");

        String token = tokenService.generateToken(user);

        assertNotNull(token);
        assertTrue(!token.isEmpty());
    }

    @Test
    void testValidateToken_Success() {
        User user = new User();
        user.setUsername("testUser");

        String token = tokenService.generateToken(user);
        String subject = tokenService.validateToken(token);

        assertEquals(user.getUsername(), subject);
    }

    @Test
    void testValidateToken_Failure() {
        String invalidToken = "invalidToken";

        String subject = tokenService.validateToken(invalidToken);

        assertTrue(subject.isEmpty());
    }
}

package br.janioofi.financialcontrol.domain.services.util;

import br.janioofi.financialcontrol.domain.util.TokenUtil;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

class TokenUtilTest {

    @Test
    void testGenerateExpirationDateToken() {
        long timeLogged = 2;
        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");
        Instant expirationDate = TokenUtil.generateExpirationDateToken(timeLogged, zoneId);

        Instant expectedDate = LocalDateTime.now().plusDays(timeLogged).atZone(zoneId).toInstant();
        assertEquals(expectedDate.getEpochSecond(), expirationDate.getEpochSecond());
    }
}
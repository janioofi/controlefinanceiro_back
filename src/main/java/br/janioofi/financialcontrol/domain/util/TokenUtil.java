package br.janioofi.financialcontrol.domain.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TokenUtil {

    public static Instant generateExpirationDateToken(long timeLogged, ZoneId zoneId) {
        return LocalDateTime.now().plusDays(timeLogged).atZone(zoneId).toInstant();
    }
}
package br.janioofi.financialcontrol.domain.services;

import br.janioofi.financialcontrol.domain.entities.User;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import java.time.*;

@Service
public class TokenService {

    @Value("${api.security.token.secret}")
    private String secret;

    public String generateToken(User user){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.create()
                    .withIssuer("api")
                    .withSubject(user.getUsername())
                    .withExpiresAt(generateExpirationDateToken())
                    .sign(algorithm);
        }catch (JWTCreationException e){
            return e.getMessage();
        }
    }

    public String validateToken(String token){
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("api")
                    .build()
                    .verify(token)
                    .getSubject();
        }catch (JWTVerificationException e){
            return "";
        }
    }

    private Instant generateExpirationDateToken(){
        int timeLogged = 8;
        ZoneId zoneId = ZoneId.of("America/Sao_Paulo");
        return LocalDateTime.now().plusHours(timeLogged).atZone(zoneId).toInstant();
    }
}

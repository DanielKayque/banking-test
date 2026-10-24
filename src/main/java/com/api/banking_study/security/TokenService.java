package com.api.banking_study.security;

import com.api.banking_study.entities.UserEntity;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TokenService {
    @Value("${jwt.secret}")
    private String secret;

    @Value("${auth.jwt.access-token-expiration-seconds:900}")
    private long accessTokenExpirationSeconds;

    public String generateAccessToken(UserEntity entity) {
        return generateAccessToken(entity.getEmail());
    }

    public String generateAccessToken(String subject) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            Instant now = Instant.now();

            return JWT.create()
                    .withIssuer("auth-login")
                    .withSubject(subject)
                    .withClaim("token_use", "access")
                    .withIssuedAt(now)
                    .withExpiresAt(getAccessTokenExpirationDate())
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Failed to create token");
        }
    }

    public String validateAccessToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm)
                    .withIssuer("auth-login")
                    .withClaim("token_use", "access")
                    .build()
                    .verify(token)
                    .getSubject();
        } catch (JWTVerificationException ex) {
            throw new JWTVerificationException("Invalid token");
        }
    }

    public Instant getAccessTokenExpirationDate(){
        return Instant.now().plusSeconds(accessTokenExpirationSeconds);
    }
}

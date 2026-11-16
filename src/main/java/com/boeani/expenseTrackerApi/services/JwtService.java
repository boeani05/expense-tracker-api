package com.boeani.expenseTrackerApi.services;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

@Service
public class JwtService {

    private static final long JWT_VALIDITY_IN_MILLISECONDS = 600_000L;

    private final SecretKey key;

    public JwtService(@Value("${jwt.secret}") String secret) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String email) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + JWT_VALIDITY_IN_MILLISECONDS);

        return Jwts.builder()
                .subject(email)
                .signWith(key)
                .issuedAt(now)
                .expiration(expiration)
                .compact();
    }

    public Optional<String> extractEmail(String token) {
        try {
            return Optional.of(Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject());
        } catch (JwtException _) {
            return Optional.empty();
        }
    }
}

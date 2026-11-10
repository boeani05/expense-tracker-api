
package com.boeani.expenseTrackerApi.services;

import com.boeani.expenseTrackerApi.entities.User;
import com.boeani.expenseTrackerApi.exceptions.InvalidCredentialsException;
import com.boeani.expenseTrackerApi.repositories.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

@Service
public class LoginService {

    private final static String INVALID_CREDENTIALS = "Invalid credentials";

    @Value("${jwt.secret}")
    private String secret;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public String loginUser(String email, String password) {

        User correctUser = userRepository.findByEmail(email).orElseThrow(() -> new InvalidCredentialsException(INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(password, correctUser.getPasswordHash())) {
            throw new InvalidCredentialsException(INVALID_CREDENTIALS);
        }

        return Jwts.builder()
                .subject(correctUser.getEmail())
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .compact();
    }
}

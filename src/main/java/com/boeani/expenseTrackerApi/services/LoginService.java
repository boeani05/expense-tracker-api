
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
    private final JwtService jwtService;

    public LoginService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public String loginUser(String email, String password) {

        User correctUser = userRepository.findByEmail(email).orElseThrow(() -> new InvalidCredentialsException(INVALID_CREDENTIALS));

        if (!passwordEncoder.matches(password, correctUser.getPasswordHash())) {
            throw new InvalidCredentialsException(INVALID_CREDENTIALS);
        }

        return jwtService.generateToken(email);
    }
}

package com.boeani.expenseTrackerApi.services;

import com.boeani.expenseTrackerApi.exceptions.InvalidCredentialsException;
import com.boeani.expenseTrackerApi.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private final UserRepository userRepository;

    public LoginService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String loginUser(String email, String password) {
        userRepository.findByEmail(email).orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));

        return "test_token";
    }
}

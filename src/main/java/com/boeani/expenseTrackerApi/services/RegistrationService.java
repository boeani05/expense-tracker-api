package com.boeani.expenseTrackerApi.services;

import com.boeani.expenseTrackerApi.entities.User;
import com.boeani.expenseTrackerApi.exceptions.EmailAlreadyRegisteredException;
import com.boeani.expenseTrackerApi.repositories.UserRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {
    
    private static final String UNIQUE_USER_EMAIL_CONSTRAINT = "unique_user_email";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(String email, String password) {
        String passwordHash = passwordEncoder.encode(password);

        try {
            return userRepository.save(new User(email, passwordHash));
        } catch (DataIntegrityViolationException exception) {
            if (exception.getCause() instanceof ConstraintViolationException constraintException) {
                if (UNIQUE_USER_EMAIL_CONSTRAINT.equals(constraintException.getConstraintName())) {
                    throw new EmailAlreadyRegisteredException("Email is already registered", exception);
                }
            }
            throw exception;
        }
    }
}

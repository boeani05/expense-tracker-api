package com.boeani.expenseTrackerApi.controllers;

import com.boeani.expenseTrackerApi.dtos.RegistrationRequest;
import com.boeani.expenseTrackerApi.dtos.RegistrationResponse;
import com.boeani.expenseTrackerApi.entities.User;
import com.boeani.expenseTrackerApi.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class RegistrationController {

    private final UserRepository userRepository;

    public RegistrationController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping(path = "/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegistrationResponse registerUser(@RequestBody RegistrationRequest registrationRequest) {
        User savedUser = userRepository.save(new User(registrationRequest.email()));

        return new RegistrationResponse(savedUser.getEmail(), savedUser.getId());
    }
}

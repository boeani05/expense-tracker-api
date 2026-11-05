package com.boeani.expenseTrackerApi.controllers;

import com.boeani.expenseTrackerApi.dtos.RegistrationRequest;
import com.boeani.expenseTrackerApi.dtos.RegistrationResponse;
import com.boeani.expenseTrackerApi.entities.User;
import com.boeani.expenseTrackerApi.services.RegistrationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class RegistrationController {

    private final RegistrationService registrationService;

    public RegistrationController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping(path = "/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegistrationResponse registerUser(@RequestBody @Valid RegistrationRequest registrationRequest) {
        User savedUser = registrationService.registerUser(registrationRequest.email(), registrationRequest.password());

        return new RegistrationResponse(savedUser.getEmail(), savedUser.getId());
    }
}

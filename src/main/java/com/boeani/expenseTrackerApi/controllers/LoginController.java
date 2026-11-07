package com.boeani.expenseTrackerApi.controllers;

import com.boeani.expenseTrackerApi.dtos.LoginRequest;
import com.boeani.expenseTrackerApi.dtos.LoginResponse;
import com.boeani.expenseTrackerApi.services.LoginService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @PostMapping(path = "/login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponse loginUser(@Valid @RequestBody LoginRequest loginRequest) {
        return new LoginResponse(loginService.loginUser(loginRequest.email(), loginRequest.password()));
    }
}

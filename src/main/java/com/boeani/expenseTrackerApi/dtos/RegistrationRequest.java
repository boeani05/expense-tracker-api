package com.boeani.expenseTrackerApi.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegistrationRequest(
        String email,
        String password
) {
}

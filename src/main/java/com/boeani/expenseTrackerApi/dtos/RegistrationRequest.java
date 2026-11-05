package com.boeani.expenseTrackerApi.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegistrationRequest(
        @NotNull @NotBlank String email,
        String password
) {
}

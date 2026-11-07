package com.boeani.expenseTrackerApi.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegistrationRequest(
        @NotBlank String email,
        @NotBlank @Size(min = 8) String password
) {
}

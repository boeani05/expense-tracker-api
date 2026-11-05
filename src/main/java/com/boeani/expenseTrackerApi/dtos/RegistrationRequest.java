package com.boeani.expenseTrackerApi.dtos;

import jakarta.validation.constraints.NotNull;

public record RegistrationRequest(
        @NotNull String email,
        String password
) {
}

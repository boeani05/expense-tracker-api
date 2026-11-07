package com.boeani.expenseTrackerApi.dtos;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank String email,
        String password
) {
}

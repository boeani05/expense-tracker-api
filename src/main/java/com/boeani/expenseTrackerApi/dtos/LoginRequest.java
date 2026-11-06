package com.boeani.expenseTrackerApi.dtos;

public record LoginRequest(
        String email,
        String password
) {
}

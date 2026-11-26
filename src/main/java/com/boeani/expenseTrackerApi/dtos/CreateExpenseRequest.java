package com.boeani.expenseTrackerApi.dtos;

import com.boeani.expenseTrackerApi.entities.Category;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateExpenseRequest(@NotNull @PositiveOrZero BigDecimal amount, @NotNull Category category,
                                   @NotNull String description,
                                   @NotNull LocalDate expenseDate) {
}
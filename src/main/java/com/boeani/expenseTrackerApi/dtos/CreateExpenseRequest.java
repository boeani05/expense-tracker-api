package com.boeani.expenseTrackerApi.dtos;

import com.boeani.expenseTrackerApi.entities.Category;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateExpenseRequest(@NotNull BigDecimal amount, @NotNull Category category, @NotNull String description,
                                   LocalDate expenseDate) {
}

package com.boeani.expenseTrackerApi.dtos;

import com.boeani.expenseTrackerApi.entities.Category;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateExpenseRequest(BigDecimal amount, Category category, String description, LocalDate expenseDate) {
}

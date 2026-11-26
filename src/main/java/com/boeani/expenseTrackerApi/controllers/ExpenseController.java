package com.boeani.expenseTrackerApi.controllers;

import com.boeani.expenseTrackerApi.dtos.CreateExpenseRequest;
import com.boeani.expenseTrackerApi.dtos.ExpenseResponse;
import com.boeani.expenseTrackerApi.services.ExpenseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public void listExpenses() {
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ExpenseResponse createExpense(@Valid @RequestBody CreateExpenseRequest expenseRequest, @AuthenticationPrincipal String email) {
        return expenseService.createExpense(expenseRequest, email);
    }
}

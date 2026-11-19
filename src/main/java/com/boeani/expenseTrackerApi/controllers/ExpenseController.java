package com.boeani.expenseTrackerApi.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public void listExpenses() {
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createExpense() {
    }
}

package com.boeani.expenseTrackerApi.services;

import com.boeani.expenseTrackerApi.dtos.CreateExpenseRequest;
import com.boeani.expenseTrackerApi.dtos.ExpenseResponse;
import com.boeani.expenseTrackerApi.entities.Expense;
import com.boeani.expenseTrackerApi.entities.User;
import com.boeani.expenseTrackerApi.repositories.ExpenseRepository;
import com.boeani.expenseTrackerApi.repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class ExpenseService {

    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;

    public ExpenseService(UserRepository userRepository, ExpenseRepository expenseRepository) {
        this.userRepository = userRepository;
        this.expenseRepository = expenseRepository;
    }

    public ExpenseResponse createExpense(CreateExpenseRequest expenseRequest, String email) {
        User user = userRepository.findByEmail(email).orElseThrow();

        Expense expense = new Expense(expenseRequest.amount(), expenseRequest.category(), expenseRequest.description(), expenseRequest.expenseDate(), user);

        Expense savedExpense = expenseRepository.save(expense);

        return new ExpenseResponse(savedExpense.getId(), savedExpense.getAmount(), savedExpense.getCategory(), savedExpense.getDescription(), savedExpense.getExpenseDate());
    }
}

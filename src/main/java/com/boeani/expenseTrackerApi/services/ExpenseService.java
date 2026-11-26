package com.boeani.expenseTrackerApi.services;

import com.boeani.expenseTrackerApi.dtos.CreateExpenseRequest;
import com.boeani.expenseTrackerApi.dtos.ExpenseResponse;
import com.boeani.expenseTrackerApi.entities.Expense;
import com.boeani.expenseTrackerApi.entities.User;
import com.boeani.expenseTrackerApi.exceptions.ExpenseDateInFutureException;
import com.boeani.expenseTrackerApi.repositories.ExpenseRepository;
import com.boeani.expenseTrackerApi.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDate;

@Service
public class ExpenseService {

    private final UserRepository userRepository;
    private final ExpenseRepository expenseRepository;
    private final Clock clock;

    public ExpenseService(UserRepository userRepository, ExpenseRepository expenseRepository, Clock clock) {
        this.userRepository = userRepository;
        this.expenseRepository = expenseRepository;
        this.clock = clock;
    }

    public ExpenseResponse createExpense(CreateExpenseRequest expenseRequest, String email) {

        if (expenseRequest.expenseDate().isAfter(LocalDate.now(clock))) {
            throw new ExpenseDateInFutureException("The entered expense date is in the future. Select a valid date!");
        }

        User user = userRepository.findByEmail(email).orElseThrow();

        Expense expense = new Expense(expenseRequest.amount(), expenseRequest.category(), expenseRequest.description(), expenseRequest.expenseDate(), user);

        Expense savedExpense = expenseRepository.save(expense);

        return new ExpenseResponse(savedExpense.getId(), savedExpense.getAmount(), savedExpense.getCategory(), savedExpense.getDescription(), savedExpense.getExpenseDate());
    }
}

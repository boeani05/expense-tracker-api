package com.boeani.expenseTrackerApi.repositories;

import com.boeani.expenseTrackerApi.entities.Expense;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
}

package com.boeani.expenseTrackerApi.exceptions;

public class ExpenseDateInFutureException extends RuntimeException {
    public ExpenseDateInFutureException(String message) {
        super(message);
    }
}

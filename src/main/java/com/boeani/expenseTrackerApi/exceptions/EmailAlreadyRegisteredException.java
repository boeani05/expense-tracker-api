package com.boeani.expenseTrackerApi.exceptions;

public class EmailAlreadyRegisteredException extends RuntimeException {
    public EmailAlreadyRegisteredException(String message, Throwable cause) {
        super(message, cause);
    }
}

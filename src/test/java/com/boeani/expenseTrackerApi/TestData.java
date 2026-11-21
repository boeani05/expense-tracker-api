package com.boeani.expenseTrackerApi;

import com.boeani.expenseTrackerApi.entities.Category;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class TestData {
    public static final String TEST_EMAIL = "max@x.at";
    public static final String TEST_PASSWORD = "12345678";
    public static final BigDecimal TEST_AMOUNT = new BigDecimal("100.00");
    public static final Category TEST_CATEGORY = Category.GROCERIES;
    public static final String TEST_DESCRIPTION = "Test description";
    public static final LocalDate TEST_DATE = LocalDate.of(2026, 10, 1);

    private TestData() {
    }
}

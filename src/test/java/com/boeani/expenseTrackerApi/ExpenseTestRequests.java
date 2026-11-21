package com.boeani.expenseTrackerApi;

import com.boeani.expenseTrackerApi.entities.Category;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public final class ExpenseTestRequests {

    private static final String ENDPOINT = "/expenses";

    private ExpenseTestRequests() {
    }

    public static MockHttpServletRequestBuilder listExpenses(String jwt) {
        return withJwt(get(ENDPOINT), jwt);
    }

    public static MockHttpServletRequestBuilder createExpense(String jwt, BigDecimal amount, Category category, String description, LocalDate expenseDate) {
        return withExpenseBody(withJwt(post(ENDPOINT), jwt), amount, category, description, expenseDate);
    }

    private static MockHttpServletRequestBuilder withJwt(MockHttpServletRequestBuilder requestBuilder, String jwt) {
        return requestBuilder.header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
    }

    private static MockHttpServletRequestBuilder withExpenseBody(MockHttpServletRequestBuilder requestBuilder, BigDecimal amount, Category category, String description, LocalDate expenseDate) {
        return requestBuilder
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format("""
                        {
                            "amount": %s,
                            "category": "%s",
                            "description": "%s",
                            "expenseDate": "%s"
                        }
                        """, amount, category, description, expenseDate));
    }
}

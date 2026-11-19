package com.boeani.expenseTrackerApi;

import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public final class ExpenseTestRequests {

    private static final String ENDPOINT = "/expenses";

    private ExpenseTestRequests() {
    }

    public static MockHttpServletRequestBuilder listExpenses(String jwt) {
        return withJwt(get(ENDPOINT), jwt);
    }

    public static MockHttpServletRequestBuilder createExpense(String jwt) {
        return withJwt(post(ENDPOINT), jwt);
    }

    private static MockHttpServletRequestBuilder withJwt(MockHttpServletRequestBuilder requestBuilder, String jwt) {
        return requestBuilder.header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
    }
}

package com.boeani.expenseTrackerApi;

import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

public final class ExpenseTestRequests {

    private ExpenseTestRequests() {
    }

    public static MockHttpServletRequestBuilder listExpenses(String jwt) {
        return get("/expenses")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt);
    }
}

package com.boeani.expenseTrackerApi;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class AuthTestRequests {

    public static MockHttpServletRequestBuilder register(String email, String password) {
        return withJsonBody(post("/auth/register"), email, password);
    }

    public static MockHttpServletRequestBuilder login(String email, String password) {
        return withJsonBody(post("/auth/login"), email, password);
    }

    private static MockHttpServletRequestBuilder withJsonBody(MockHttpServletRequestBuilder request, String email, String password) {
        return request
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format("""
                        {
                            "email": "%s",
                            "password": "%s"
                        }
                        """, email, password));
    }
}

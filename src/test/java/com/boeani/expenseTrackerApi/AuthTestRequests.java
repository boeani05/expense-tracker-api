package com.boeani.expenseTrackerApi;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

public class AuthTestRequests {

    private static final String TEST_PASSWORD = "123";

    public static MockHttpServletRequestBuilder register(String email) {
        return withJsonBody(post("/auth/register"), email);
    }

    public static MockHttpServletRequestBuilder login(String email) {
        return withJsonBody(post("/auth/login"), email);
    }

    private static MockHttpServletRequestBuilder withJsonBody(MockHttpServletRequestBuilder request, String email) {
        return request
                .contentType(MediaType.APPLICATION_JSON)
                .content(String.format("""
                        {
                            "email": "%s",
                            "password": "%s"
                        }
                        """, email, TEST_PASSWORD));
    }
}

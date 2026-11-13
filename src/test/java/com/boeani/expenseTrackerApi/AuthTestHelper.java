package com.boeani.expenseTrackerApi;

import com.jayway.jsonpath.JsonPath;
import org.springframework.test.web.servlet.MockMvc;

import static com.boeani.expenseTrackerApi.TestData.TEST_EMAIL;
import static com.boeani.expenseTrackerApi.TestData.TEST_PASSWORD;

public final class AuthTestHelper {

    private AuthTestHelper() {
    }

    public static String registerAndLogin(MockMvc mockMvc) throws Exception {
        mockMvc.perform(AuthTestRequests.register(TEST_EMAIL, TEST_PASSWORD));

        String responseBody = mockMvc.perform(AuthTestRequests.login(TEST_EMAIL, TEST_PASSWORD))
                .andReturn()
                .getResponse()
                .getContentAsString();

        return JsonPath.read(responseBody, "$.token");
    }
}

package com.boeani.expenseTrackerApi;

import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.boeani.expenseTrackerApi.TestData.TEST_EMAIL;
import static com.boeani.expenseTrackerApi.TestData.TEST_PASSWORD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Import(TestcontainersConfiguration.class)
public class ExpenseEndpointTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void callingAnyExpensesEndpointWithNoJwtShouldBeRejected() throws Exception {
        mockMvc.perform(post("/expenses"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void callingExpensesEndpointWithValidJwtShouldBeSuccessful() throws Exception {
        mockMvc.perform(AuthTestRequests.register(TEST_EMAIL, TEST_PASSWORD));

        String responseBody = mockMvc.perform(AuthTestRequests.login(TEST_EMAIL, TEST_PASSWORD))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String jwt = JsonPath.read(responseBody, "$.token");

        mockMvc.perform(get("/expenses")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
                .andExpect(status().isOk());
    }
}

package com.boeani.expenseTrackerApi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.boeani.expenseTrackerApi.TestData.TEST_EMAIL;
import static com.boeani.expenseTrackerApi.TestData.TEST_PASSWORD;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Import(TestcontainersConfiguration.class)
public class UserLoginTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void unknownEmailShouldBeRejected() throws Exception {
        mockMvc.perform(AuthTestRequests.login("unknown@email.at", TEST_PASSWORD))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void userIsLoggedInSuccessfully() throws Exception {
        mockMvc.perform(AuthTestRequests.register(TEST_EMAIL, TEST_PASSWORD));

        mockMvc.perform(AuthTestRequests.login(TEST_EMAIL, TEST_PASSWORD))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void unknownPasswordShouldBeRejected() throws Exception {
        mockMvc.perform(AuthTestRequests.register(TEST_EMAIL, TEST_PASSWORD));

        mockMvc.perform(AuthTestRequests.login(TEST_EMAIL, "wrong_password"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void emptyEmailOnLoginShouldBeRejected() throws Exception {
        mockMvc.perform(AuthTestRequests.login("", TEST_PASSWORD))
                .andExpect(status().isBadRequest());
    }

    @Test
    void emptyPasswordOnLoginShouldBeRejected() throws Exception {
        mockMvc.perform(AuthTestRequests.login(TEST_EMAIL, ""))
                .andExpect(status().isBadRequest());
    }
}

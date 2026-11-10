package com.boeani.expenseTrackerApi;

import com.jayway.jsonpath.JsonPath;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static com.boeani.expenseTrackerApi.TestData.TEST_EMAIL;
import static com.boeani.expenseTrackerApi.TestData.TEST_PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Import(TestcontainersConfiguration.class)
public class UserLoginTests {

    @Autowired
    private MockMvc mockMvc;

    @Value("${jwt.secret}")
    private String secret;

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

    @Test
    void passwordExceedingMaxBytesOnLoginShouldBeRejected() throws Exception {
        mockMvc.perform(AuthTestRequests.register(TEST_EMAIL, TEST_PASSWORD));

        mockMvc.perform(AuthTestRequests.login(TEST_EMAIL, "ä".repeat(37)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void jwtTokenShouldBeValid() throws Exception {
        mockMvc.perform(AuthTestRequests.register(TEST_EMAIL, TEST_PASSWORD));

        String responseBody = mockMvc.perform(AuthTestRequests.login(TEST_EMAIL, TEST_PASSWORD))
                .andReturn()
                .getResponse()
                .getContentAsString();

        String jwt = JsonPath.read(responseBody, "$.token");

        String email = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(jwt)
                .getPayload()
                .getSubject();

        assertThat(email).isEqualTo(TEST_EMAIL);
    }
}

package com.boeani.expenseTrackerApi;

import io.jsonwebtoken.Claims;
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
import java.time.Duration;
import java.util.Date;

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
        String jwt = AuthTestHelper.registerAndLogin(mockMvc);

        String email = getJwtPayload(jwt).getSubject();

        assertThat(email).isEqualTo(TEST_EMAIL);
    }

    @Test
    void differenceBetweenCreationAndExpirationShouldBeTenMinutes() throws Exception {
        String jwt = AuthTestHelper.registerAndLogin(mockMvc);

        Claims jwtPayload = getJwtPayload(jwt);

        Date issuedAt = jwtPayload.getIssuedAt();

        Date expiration = jwtPayload.getExpiration();

        Duration timeDifference = Duration.between(issuedAt.toInstant(), expiration.toInstant());

        assertThat(timeDifference.getSeconds()).isEqualTo(600L);
    }

    private Claims getJwtPayload(String jwt) {
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }
}

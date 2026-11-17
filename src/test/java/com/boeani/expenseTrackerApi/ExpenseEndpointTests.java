package com.boeani.expenseTrackerApi;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import static com.boeani.expenseTrackerApi.TestData.TEST_EMAIL;
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

    @Value("${jwt.secret}")
    private String secret;

    @Test
    void callingAnyExpensesEndpointWithNoJwtShouldBeRejected() throws Exception {
        mockMvc.perform(post("/expenses"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void callingExpensesEndpointWithValidJwtShouldBeSuccessful() throws Exception {
        String jwt = AuthTestHelper.registerAndLogin(mockMvc);

        mockMvc.perform(get("/expenses")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt))
                .andExpect(status().isOk());
    }

    @Test
    void corruptJwtShouldBeRejected() throws Exception {
        String jwt = AuthTestHelper.registerAndLogin(mockMvc);

        String corruptedJwt = jwt.substring(0, jwt.length() - 1);

        mockMvc.perform(get("/expenses")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + corruptedJwt))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void expiredTokenShouldBeRejected() throws Exception {

        Date now = new Date();

        Date issuedAt = new Date(now.getTime() - 660_000L);
        Date expiration = new Date(now.getTime() - 60_000L);

        String expiredJwt = Jwts.builder()
                .subject(TEST_EMAIL)
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)))
                .issuedAt(issuedAt)
                .expiration(expiration)
                .compact();

        mockMvc.perform(get("/expenses")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + expiredJwt))
                .andExpect(status().isUnauthorized());
    }
}
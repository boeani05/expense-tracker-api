package com.boeani.expenseTrackerApi;

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
import java.util.Date;

import static com.boeani.expenseTrackerApi.TestData.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
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

        mockMvc.perform(ExpenseTestRequests.listExpenses(jwt))
                .andExpect(status().isOk());
    }

    @Test
    void corruptJwtShouldBeRejected() throws Exception {
        String jwt = AuthTestHelper.registerAndLogin(mockMvc);

        String corruptedJwt = jwt.substring(0, jwt.length() - 1);

        mockMvc.perform(ExpenseTestRequests.listExpenses(corruptedJwt))
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

        mockMvc.perform(ExpenseTestRequests.listExpenses(expiredJwt))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void creatingExpenseResponseBodyShouldBeFilledCorrectly() throws Exception {
        String jwt = AuthTestHelper.registerAndLogin(mockMvc);

        mockMvc.perform(ExpenseTestRequests.createExpense(jwt, TEST_AMOUNT, TEST_CATEGORY, TEST_DESCRIPTION, TEST_DATE))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.amount").value(TEST_AMOUNT.doubleValue()))
                .andExpect(jsonPath("$.category").value(TEST_CATEGORY.name()))
                .andExpect(jsonPath("$.description").value(TEST_DESCRIPTION))
                .andExpect(jsonPath("$.expenseDate").value(TEST_DATE.toString()));
    }

    @Test
    void creatingExpenseWithNullAmountShouldBeRejected() throws Exception {
        String jwt = AuthTestHelper.registerAndLogin(mockMvc);

        mockMvc.perform(ExpenseTestRequests.createExpense(jwt, null, TEST_CATEGORY, TEST_DESCRIPTION, TEST_DATE))
                .andExpect(status().isBadRequest());
    }

    @Test
    void creatingExpenseWithNullCategoryShouldBeRejected() throws Exception {
        String jwt = AuthTestHelper.registerAndLogin(mockMvc);

        mockMvc.perform(ExpenseTestRequests.createExpense(jwt, TEST_AMOUNT, null, TEST_DESCRIPTION, TEST_DATE))
                .andExpect(status().isBadRequest());
    }

    @Test
    void creatingExpenseWithNullDescriptionShouldBeRejected() throws Exception {
        String jwt = AuthTestHelper.registerAndLogin(mockMvc);

        mockMvc.perform(ExpenseTestRequests.createExpense(jwt, TEST_AMOUNT, TEST_CATEGORY, null, TEST_DATE))
                .andExpect(status().isBadRequest());
    }

    @Test
    void creatingExpenseWithEmptyDescriptionShouldBeCreated() throws Exception {
        String jwt = AuthTestHelper.registerAndLogin(mockMvc);

        mockMvc.perform(ExpenseTestRequests.createExpense(jwt, TEST_AMOUNT, TEST_CATEGORY, "", TEST_DATE))
                .andExpect(status().isCreated());
    }

    @Test
    void creatingExpenseWithNullExpenseDateShouldBeRejected() throws Exception {
        String jwt = AuthTestHelper.registerAndLogin(mockMvc);

        mockMvc.perform(ExpenseTestRequests.createExpense(jwt, TEST_AMOUNT, TEST_CATEGORY, TEST_DESCRIPTION, null))
                .andExpect(status().isBadRequest());
    }
}
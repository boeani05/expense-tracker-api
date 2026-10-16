package com.boeani.expenseTrackerApi;

import com.boeani.expenseTrackerApi.entities.Category;
import com.boeani.expenseTrackerApi.entities.Expense;
import com.boeani.expenseTrackerApi.entities.User;
import com.boeani.expenseTrackerApi.repositories.ExpenseRepository;
import com.boeani.expenseTrackerApi.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestcontainersConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ExpenseRepositoryTests {

    private static final String TEST_EMAIL = "max@x.at";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @Test
    void addedExpenseOfUserIsSavedAndLoaded() {
        BigDecimal testAmount = new BigDecimal("100.00");
        Category testCategory = Category.GROCERIES;
        String testDescription = "Test Description";
        LocalDate testDate = LocalDate.of(2026, 10, 1);

        User testUser = new User(TEST_EMAIL);
        User savedUser = userRepository.save(testUser);

        Expense testExpense = new Expense(testAmount, testCategory, testDescription, testDate, savedUser);
        Expense savedExpense = expenseRepository.save(testExpense);

        testEntityManager.flush();
        testEntityManager.clear();

        Long expenseId = savedExpense.getId();

        Optional<Expense> foundExpense = expenseRepository.findById(expenseId);

        Expense loadedExpense = foundExpense.orElseThrow();

        assertThat(loadedExpense.getAmount()).isEqualTo(testAmount);
        assertThat(loadedExpense.getCategory()).isEqualTo(testCategory);
        assertThat(loadedExpense.getDescription()).isEqualTo(testDescription);
        assertThat(loadedExpense.getExpenseDate()).isEqualTo(testDate);
        assertThat(loadedExpense.getUser().getId()).isEqualTo(savedUser.getId());
    }
}

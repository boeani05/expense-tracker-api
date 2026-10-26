package com.boeani.expenseTrackerApi;

import com.boeani.expenseTrackerApi.entities.Category;
import com.boeani.expenseTrackerApi.entities.Expense;
import com.boeani.expenseTrackerApi.entities.User;
import com.boeani.expenseTrackerApi.repositories.ExpenseRepository;
import com.boeani.expenseTrackerApi.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import(TestcontainersConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ExpenseRepositoryTests {

    private static final String TEST_EMAIL = "max@x.at";
    private static final Category TEST_CATEGORY = Category.GROCERIES;
    private static final String TEST_DESCRIPTION = "Test Description";
    private static final LocalDate TEST_DATE = LocalDate.of(2026, 10, 1);
    private static final BigDecimal TEST_AMOUNT = new BigDecimal("100.00");

    private User savedUser;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void initializeUser() {
        User testUser = new User(TEST_EMAIL);
        savedUser = userRepository.saveAndFlush(testUser);
    }

    @Test
    void addedExpenseOfUserIsSavedAndLoaded() {
        Expense savedExpense = expenseRepository.saveAndFlush(createExpenseWithAmount(TEST_AMOUNT));

        testEntityManager.clear();

        Long expenseId = savedExpense.getId();

        Optional<Expense> foundExpense = expenseRepository.findById(expenseId);

        Expense loadedExpense = foundExpense.orElseThrow();

        assertThat(loadedExpense.getAmount()).isEqualTo(TEST_AMOUNT);
        assertThat(loadedExpense.getCategory()).isEqualTo(TEST_CATEGORY);
        assertThat(loadedExpense.getDescription()).isEqualTo(TEST_DESCRIPTION);
        assertThat(loadedExpense.getExpenseDate()).isEqualTo(TEST_DATE);
        assertThat(loadedExpense.getUser().getId()).isEqualTo(savedUser.getId());
    }

    @Test
    void negativeAmountExpenseShouldBeRejected() {
        Expense testExpense = createExpenseWithAmount(new BigDecimal("-100.00"));

        assertThatThrownBy(() -> this.expenseRepository.saveAndFlush(testExpense)).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void nullAmountShouldBeRejected() {
        Expense nullTestExpense = createExpenseWithAmount(null);

        assertThatThrownBy(() -> expenseRepository.saveAndFlush(nullTestExpense)).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void nullCategoryShouldBeRejected() {
        Expense nullTestExpense = new Expense(TEST_AMOUNT, null, TEST_DESCRIPTION, TEST_DATE, savedUser);

        assertThatThrownBy(() -> expenseRepository.saveAndFlush(nullTestExpense)).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void nullDescriptionShouldBeRejected() {
        Expense nullTestExpense = new Expense(TEST_AMOUNT, TEST_CATEGORY, null, TEST_DATE, savedUser);

        assertThatThrownBy(() -> expenseRepository.saveAndFlush(nullTestExpense)).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void nullExpenseDateShouldBeRejected() {
        Expense nullTestExpense = new Expense(TEST_AMOUNT, TEST_CATEGORY, TEST_DESCRIPTION, null, savedUser);

        assertThatThrownBy(() -> expenseRepository.saveAndFlush(nullTestExpense)).isInstanceOf(DataIntegrityViolationException.class);
    }

    private Expense createExpenseWithAmount(BigDecimal amount) {
        return new Expense(amount, TEST_CATEGORY, TEST_DESCRIPTION, TEST_DATE, savedUser);
    }
}

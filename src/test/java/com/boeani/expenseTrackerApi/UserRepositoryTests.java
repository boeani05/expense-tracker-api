package com.boeani.expenseTrackerApi;

import com.boeani.expenseTrackerApi.entities.User;
import com.boeani.expenseTrackerApi.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.util.StringUtils;

import java.util.Optional;

import static com.boeani.expenseTrackerApi.TestData.TEST_EMAIL;
import static com.boeani.expenseTrackerApi.TestData.TEST_PASSWORD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import(TestcontainersConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTests {

    private User testUser;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    void initializeUser() {
        testUser = new User(TEST_EMAIL, TEST_PASSWORD);
    }

    @Test
    void userIsCorrectlySavedAndLoaded() {

        User savedUser = userRepository.saveAndFlush(testUser);

        testEntityManager.clear();

        Long userId = savedUser.getId();

        Optional<User> foundUser = userRepository.findById(userId);

        assertThat(foundUser.orElseThrow().getEmail()).isEqualTo(TEST_EMAIL);
    }

    @Test
    void sameEmailTwiceShouldReject() {
        userRepository.saveAndFlush(testUser);

        User sameUser = new User(TEST_EMAIL, TEST_PASSWORD);

        assertThatThrownBy(() -> userRepository.saveAndFlush(sameUser)).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void caseInsensitiveSameEmailTwiceShouldBeRejected() {
        userRepository.saveAndFlush(testUser);

        User capitalizedSameUser = new User(StringUtils.capitalize(TEST_EMAIL), TEST_PASSWORD);

        assertThatThrownBy(() -> userRepository.saveAndFlush(capitalizedSameUser)).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void nullEmailShouldBeRejected() {
        User nullTestUser = new User(null, TEST_PASSWORD);

        assertThatThrownBy(() -> userRepository.saveAndFlush(nullTestUser)).isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void nullPasswordHashShouldBeRejected() {
        User nullPasswordUser = new User(TEST_EMAIL, null);

        assertThatThrownBy(() -> userRepository.saveAndFlush(nullPasswordUser)).isInstanceOf(DataIntegrityViolationException.class);
    }
}
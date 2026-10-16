package com.boeani.expenseTrackerApi;

import com.boeani.expenseTrackerApi.entities.User;
import com.boeani.expenseTrackerApi.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(TestcontainersConfiguration.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTests {

    private static final String TEST_EMAIL = "max@x.at";

    @Autowired
    private UserRepository userRepository;

    @Test
    void userIsCorrectlySavedAndLoaded() {
        User testUser = new User(TEST_EMAIL);
        User savedUser = userRepository.save(testUser);

        Long userId = savedUser.getId();

        Optional<User> foundUser = userRepository.findById(userId);

        assertThat(foundUser.orElseThrow().getEmail()).isEqualTo(TEST_EMAIL);
    }
}
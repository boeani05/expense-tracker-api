package com.boeani.expenseTrackerApi;

import com.boeani.expenseTrackerApi.entities.User;
import com.boeani.expenseTrackerApi.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Import(TestcontainersConfiguration.class)
public class UserRegistrationTests {

    private static final String TEST_EMAIL = "max@x.at";
    private static final String TEST_PASSWORD = "123";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void registrationIsSuccessful() throws Exception {
        mockMvc.perform(AuthTestRequests.register(TEST_EMAIL, TEST_PASSWORD))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(TEST_EMAIL))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test
    void passwordIsSavedHashed() throws Exception {
        mockMvc.perform(AuthTestRequests.register(TEST_EMAIL, TEST_PASSWORD));

        User testUser = userRepository.findByEmail(TEST_EMAIL).orElseThrow();

        assertThat(testUser.getPasswordHash()).isNotEqualTo(TEST_PASSWORD);
        assertThat(passwordEncoder.matches(TEST_PASSWORD, testUser.getPasswordHash())).isEqualTo(true);
    }

    @Test
    void nonUniqueEmailShouldBeRejected() throws Exception {
        mockMvc.perform(AuthTestRequests.register(TEST_EMAIL, TEST_PASSWORD));

        mockMvc.perform(AuthTestRequests.register(TEST_EMAIL, TEST_PASSWORD))
                .andExpect(status().isConflict());
    }

    @Test
    void missingEmailOnRegisterShouldBeRejected() throws Exception {
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(String.format("""
                                {
                                    "password": "%s"
                                }
                                """, TEST_PASSWORD)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void emptyEmailOnRegisterShouldBeRejected() throws Exception {
        mockMvc.perform(AuthTestRequests.register("", TEST_PASSWORD))
                .andExpect(status().isBadRequest());
    }
}

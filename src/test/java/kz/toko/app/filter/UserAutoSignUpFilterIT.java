package kz.toko.app.filter;

import kz.toko.app.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static kz.toko.app.util.TestConstants.MOCK_USER_DEFAULT_USERNAME;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class UserAutoSignUpFilterIT extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Should automatically create a new user when current one is not in database yet")
    void createNewUserAutomatically() throws Exception {
        this.mockMvc.perform(
                        get("/users")
                                .with(jwt().jwt(jwt -> jwt
                                        .claim("email", "user@example.com")
                                        .claim("given_name", "John")
                                        .claim("family_name", "Locke")
                                ))
                                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*.id", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$.*.username", hasItem(MOCK_USER_DEFAULT_USERNAME)))
                .andExpect(jsonPath("$.*.email", hasItem("user@example.com")))
                .andExpect(jsonPath("$.*.firstName", hasItem("John")))
                .andExpect(jsonPath("$.*.lastName", hasItem("Locke")));
    }

    @Test
    @DisplayName("Should return 400 (bad request) code for JWT token with missing claims")
    void shouldReturn400CodeOnInvalidJwtToken() throws Exception {
        this.mockMvc.perform(
                        delete("/users/{userId}", "1")
                                .with(jwt())
                                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}

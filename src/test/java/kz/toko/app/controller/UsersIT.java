package kz.toko.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.toko.api.model.CreateUserRequest;
import kz.toko.api.model.UpdateUserRequest;
import kz.toko.app.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static kz.toko.app.enumeration.Role.USER;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class UsersIT extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @DisplayName("Should return users list for authenticated user")
    void shouldReturnUsersList() throws Exception {
        this.mockMvc.perform(
                        get("/users")
                                .with(validJwtToken())
                                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*.id", hasSize(1)))
                .andExpect(jsonPath("$.[0].username", is("adam.smith")));
    }

    @Test
    @DisplayName("Should return 401 (unauthorized) code for missing Authorization header")
    void shouldReturn401CodeOnMissingAuthHeader() throws Exception {
        this.mockMvc.perform(
                        delete("/users/{userId}", "1")
                                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Should delete manually inserted user")
    void deleteUser() throws Exception {
        this.mockMvc.perform(
                        delete("/users/{userId}", "1")
                                .with(validJwtToken())
                                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @Tag("exception-handling")
    @DisplayName("Should return not found response code when passing non-existing user ID")
    void deleteAbsentUser() throws Exception {
        this.mockMvc.perform(
                        delete("/users/{userId}", Integer.MAX_VALUE)
                                .with(validJwtToken())
                                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(APPLICATION_PROBLEM_JSON));
    }

    @Test
    @DisplayName("Should create new user with default role")
    void createUser() throws Exception {
        final var createUserRequest = new CreateUserRequest()
                .username("j_weak")
                .firstName("John")
                .lastName("Weak")
                .password("password");

        this.mockMvc.perform(
                post("/users")
                        .with(validJwtToken())
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createUserRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.username", is("j_weak")))
                .andExpect(jsonPath("$.firstName", is("John")))
                .andExpect(jsonPath("$.lastName", is("Weak")))
                .andExpect(jsonPath("$.roles", hasSize(1)))
                .andExpect(jsonPath("$.roles", hasItem(USER.getAuthority())));

    }

    @Test
    @DisplayName("Should update only user email")
    void updateUser() throws Exception {
        final var updateUserRequest = new UpdateUserRequest()
                .email("updated_email@example.com");

        this.mockMvc.perform(
                patch("/users/{id}", 1)
                        .with(validJwtToken())
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateUserRequest)))
                .andDo(print())
                .andExpect(status().isNoContent());

        this.mockMvc.perform(
                get("/users/{id}", 1)
                        .with(validJwtToken())
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("adam.smith")))
                .andExpect(jsonPath("$.firstName", is("Adam")))
                .andExpect(jsonPath("$.lastName", is("Smith")))
                .andExpect(jsonPath("$.email", is("updated_email@example.com")))
                .andExpect(jsonPath("$.phone", is("+7-777-999-88-77")));
    }
}

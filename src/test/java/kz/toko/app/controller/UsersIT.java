package kz.toko.app.controller;

import kz.toko.app.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc
@SqlGroup({
        @Sql(value = "classpath:/db.scripts/add_one_user.sql", executionPhase = BEFORE_TEST_METHOD),
        @Sql(value = "classpath:/db.scripts/delete_all_users.sql", executionPhase = AFTER_TEST_METHOD)
})
class UsersIT extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

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
}

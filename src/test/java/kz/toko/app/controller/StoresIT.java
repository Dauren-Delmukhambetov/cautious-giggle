package kz.toko.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.toko.api.model.CreateStoreRequest;
import kz.toko.app.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static kz.toko.api.model.CreateStoreRequest.ModeEnum.SELLER;
import static kz.toko.app.util.TestConstants.TEST_USER_FULL_NAME;
import static kz.toko.app.util.data.provider.AddressDataProvider.buildAddress;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.BEFORE_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class StoresIT extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @DisplayName("Should create a new store")
    void createNewStore() throws Exception {
        final var createStoreRequest = new CreateStoreRequest()
                .address(buildAddress())
                .mode(SELLER)
                .name("Store #1");

        this.mockMvc.perform(
                        post("/stores")
                                .with(validJwtToken())
                                .contentType(APPLICATION_JSON)
                                .content(mapper.writeValueAsString(createStoreRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is(createStoreRequest.getName())))
                .andExpect(jsonPath("$.mode", is(createStoreRequest.getMode().toString())))
                .andExpect(jsonPath("$.address.addressLine", is(createStoreRequest.getAddress().getAddressLine())))
                .andExpect(jsonPath("$.address.city", is(createStoreRequest.getAddress().getCity())))
                .andExpect(jsonPath("$.address.postalCode", is(createStoreRequest.getAddress().getPostalCode())))
                .andExpect(jsonPath("$.ownerFullName", is(TEST_USER_FULL_NAME)));
    }

    @Test
    @DisplayName("Should return current user's stores")
    @Sql(value = "classpath:/db.scripts/add_adam_store.sql", executionPhase = BEFORE_TEST_METHOD)
    void shouldReturnCurrentUserStores() throws Exception {
        this.mockMvc.perform(
                        get("/stores")
                                .with(validJwtToken())
                                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*.id", hasSize(1)));
    }
}

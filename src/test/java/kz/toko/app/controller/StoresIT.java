package kz.toko.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.toko.api.model.CreateStoreRequest;
import kz.toko.app.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import static kz.toko.api.model.CreateStoreRequest.ModeEnum.SELLER;
import static kz.toko.app.util.TestConstants.TEST_USER_FULL_NAME;
import static kz.toko.app.util.data.provider.AddressDataProvider.buildAddress;
import static kz.toko.app.util.data.provider.FakeDataProvider.faker;
import static org.hamcrest.Matchers.hasKey;
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
    void shouldCreateNewStore() throws Exception {
        final var createStoreRequest = new CreateStoreRequest()
                .address(buildAddress())
                .mode(SELLER)
                .name(faker.company().name());

        this.mockMvc.perform(
                        post("/stores")
                                .with(validJwtToken())
                                .contentType(APPLICATION_JSON)
                                .content(mapper.writeValueAsString(createStoreRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpectAll(
                        jsonPath("$.id", notNullValue()),
                        jsonPath("$.name", is(createStoreRequest.getName())),
                        jsonPath("$.mode", is(createStoreRequest.getMode().toString())),
                        jsonPath("$.address.addressLine", is(createStoreRequest.getAddress().getAddressLine())),
                        jsonPath("$.address.city", is(createStoreRequest.getAddress().getCity())),
                        jsonPath("$.address.postalCode", is(createStoreRequest.getAddress().getPostalCode())),
                        jsonPath("$.ownerFullName", is(TEST_USER_FULL_NAME))
                );
    }

    @Test
    @DisplayName("Should return 400 code (Bad request) on input request w/o address")
    void shouldReturnBadRequestCode() throws Exception {
        final var createStoreRequest = new CreateStoreRequest()
                .mode(SELLER)
                .name(faker.company().name());

        this.mockMvc.perform(
                        post("/stores")
                                .with(validJwtToken())
                                .contentType(APPLICATION_JSON)
                                .content(mapper.writeValueAsString(createStoreRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpectAll(
                        jsonPath("$.message", notNullValue()),
                        jsonPath("$.exceptionClass", notNullValue()),
                        jsonPath("$.params", hasKey("address"))
                );
    }

    @Test
    @DisplayName("Should return only current user's stores")
    @SqlGroup({
            @Sql(value = "classpath:/db.scripts/add_adam_store.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:/db.scripts/add_another_user.sql", executionPhase = BEFORE_TEST_METHOD),
            @Sql(value = "classpath:/db.scripts/add_john_locke_store.sql", executionPhase = BEFORE_TEST_METHOD)
    })
    void shouldReturnOnlyCurrentUserStores() throws Exception {
        this.mockMvc.perform(
                        get("/stores")
                                .with(validJwtToken())
                                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*.id", hasSize(1)));
    }
}

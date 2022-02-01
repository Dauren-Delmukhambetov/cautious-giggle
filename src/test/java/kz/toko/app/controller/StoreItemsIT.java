package kz.toko.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.toko.app.IntegrationTest;
import kz.toko.app.util.data.builder.TestDataSetBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;

import javax.transaction.Transactional;

import static kz.toko.app.util.data.provider.StoreItemDataProvider.buildCreateStoreItemRequest;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class StoreItemsIT extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @SqlGroup({
            @Sql(value = "classpath:/db.scripts/add_one_product.sql"),
            @Sql(value = "classpath:/db.scripts/add_adam_store.sql"),
    })
    @DisplayName("Should create a new store item")
    void shouldCreateNewStoreItem() throws Exception {
        final var request = buildCreateStoreItemRequest(1L, 1L);

        this.mockMvc.perform(
                        post("/store-items")
                                .with(validJwtToken())
                                .contentType(APPLICATION_JSON)
                                .content(mapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpectAll(
                        jsonPath("$.itemId", notNullValue()),
                        jsonPath("$.productId", is(1)),
                        jsonPath("$.productName", is("Product # 1")),
                        jsonPath("$.storeId", is(1)),
                        jsonPath("$.storeName", is("Store #1")),
                        jsonPath("$.price", is(request.getPrice())),
                        jsonPath("$.amount", is(request.getAmount())),
                        jsonPath("$.measureUnit", equalToIgnoringCase(request.getMeasureUnit().name())),
                        jsonPath("$.activeSince", is(request.getActiveSince().toString())),
                        jsonPath("$.activeTill", is(request.getActiveTill().toString()))
                );
    }

    @Test
    @DisplayName("Should return 404 Bad Request code on missing product and store IDs")
    void shouldReturnBadRequestCode() throws Exception {
        final var request = buildCreateStoreItemRequest(null, null);

        this.mockMvc.perform(
                        post("/store-items")
                                .with(validJwtToken())
                                .contentType(APPLICATION_JSON)
                                .content(mapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpectAll(
                        jsonPath("$.params.*", hasSize(2)),
                        jsonPath("$.params", hasKey("productId")),
                        jsonPath("$.params", hasKey("storeId"))
                );
    }

    @Test
    @Transactional
    @DisplayName("Should return active store items by default")
    void shouldReturnActiveStoreItemsByDefault() throws Exception {

        new TestDataSetBuilder(entityManager)
                .user()
                .product()
                .store()
                .expiredStoreItem()
                .activeStoreItem()
                .upcomingStoreItem();

        this.mockMvc.perform(
                        get("/store-items")
                                .with(validJwtToken())
                                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.totalCount", is(1)),
                        jsonPath("$.currentPage", is(1)),
                        jsonPath("$.items.*", hasSize(1))
                );
    }
}

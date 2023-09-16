package kz.toko.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.toko.api.model.CreateProductRequest;
import kz.toko.api.model.UpdateProductRequest;
import kz.toko.app.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;

import static kz.toko.app.util.data.provider.FakeDataProvider.faker;
import static kz.toko.app.util.data.provider.FakeDataProvider.randomPrice;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class ProductsIT extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @DisplayName("Should create a new product")
    void createNewProduct() throws Exception {
        final var createProductRequest = new CreateProductRequest()
                .name(faker.commerce().productName())
                .price(randomPrice().doubleValue());

        this.mockMvc.perform(
                        post("/products")
                                .with(validJwtToken())
                                .contentType(APPLICATION_JSON)
                                .content(mapper.writeValueAsString(createProductRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpectAll(
                        jsonPath("$.id", notNullValue()),
                        jsonPath("$.name", is(createProductRequest.getName())),
                        jsonPath("$.price", is(createProductRequest.getPrice())),
                        jsonPath("$.imageLink", nullValue())
                );
    }

    @Test
    @DisplayName("Should upload image for a product")
    @Sql(value = "classpath:/db.scripts/add_one_product.sql")
    void uploadProductImage() throws Exception {
        final var file = ResourceUtils.getFile("classpath:images/asu_lemon.png");

        try (final var fis = new FileInputStream(file)) {
            final var multipartFile = new MockMultipartFile("file", file.getName(), IMAGE_PNG_VALUE, fis);

            this.mockMvc.perform(
                            multipart("/products/{id}/image", 1)
                                    .file(multipartFile)
                                    .with(validJwtToken()))
                    .andDo(print())
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.link", notNullValue()));
        }
    }

    @Test
    @DisplayName("Should update only product name")
    @Sql(value = "classpath:/db.scripts/add_one_product.sql")
    void updateProduct() throws Exception {
        final var updateProductRequest = new UpdateProductRequest()
                .name(faker.commerce().productName());

        this.mockMvc.perform(
                        patch("/products/{id}", 1)
                                .with(validJwtToken())
                                .contentType(APPLICATION_JSON)
                                .content(mapper.writeValueAsString(updateProductRequest)))
                .andDo(print())
                .andExpect(status().isNoContent());

        this.mockMvc.perform(
                        get("/products/{id}", 1)
                                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.id", is(1)),
                        jsonPath("$.name", is(updateProductRequest.getName())),
                        jsonPath("$.price", is(123.45))
                );
    }

    @Test
    @DisplayName("Should delete product")
    @Sql(value = "classpath:/db.scripts/add_product_to_delete.sql")
    void deleteProduct() throws Exception {
        this.mockMvc.perform(
                        delete("/products/{id}", 2)
                                .with(validJwtToken()))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @Tag("exception-handling")
    @DisplayName("Should return gone response code when passing deleted product ID")
    @Sql(value = "classpath:/db.scripts/add_another_product_to_delete.sql")
    void getDeletedProduct() throws Exception {
        this.mockMvc.perform(
                        delete("/products/{id}", 3)
                                .with(validJwtToken()))
                .andDo(print())
                .andExpect(status().isNoContent());

        this.mockMvc.perform(
                        get("/products/{id}", 3)
                                .with(validJwtToken()))
                .andDo(print())
                .andExpect(status().isGone())
                .andExpect(content().contentType(APPLICATION_PROBLEM_JSON));
    }
}

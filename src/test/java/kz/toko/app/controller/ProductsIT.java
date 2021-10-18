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
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.springframework.http.MediaType.*;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
                .name("Brand new product")
                .price(123.456);

        this.mockMvc.perform(
                post("/products")
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(createProductRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Brand new product")))
                .andExpect(jsonPath("$.price", is(123.456)))
                .andExpect(jsonPath("$.imageLink", nullValue()));
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
                            .file(multipartFile))
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
                .name("Updated product name");

        this.mockMvc.perform(
                patch("/products/{id}", 1)
                        .contentType(APPLICATION_JSON)
                        .content(mapper.writeValueAsString(updateProductRequest)))
                .andDo(print())
                .andExpect(status().isNoContent());

        this.mockMvc.perform(
                get("/products/{id}", 1)
                        .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("Updated product name")))
                .andExpect(jsonPath("$.price", is(123.45)));
    }

    @Test
    @DisplayName("Should delete product")
    @Sql(value = "classpath:/db.scripts/add_product_to_delete.sql")
    void deleteProduct() throws Exception {
        this.mockMvc.perform(
                        delete("/products/{id}", 2))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    @Tag("exception-handling")
    @DisplayName("Should return gone response code when passing deleted product ID")
    @Sql(value = "classpath:/db.scripts/add_another_product_to_delete.sql")
    void getDeletedProduct() throws Exception {
        this.mockMvc.perform(
                        delete("/products/{id}", 3))
                .andDo(print())
                .andExpect(status().isNoContent());

        this.mockMvc.perform(
                        get("/products/{id}", 3))
                .andDo(print())
                .andExpect(status().isGone())
                .andExpect(content().contentType(APPLICATION_PROBLEM_JSON));
    }
}

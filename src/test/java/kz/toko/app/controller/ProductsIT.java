package kz.toko.app.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import kz.toko.api.model.CreateProductRequest;
import kz.toko.app.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;

import static org.hamcrest.Matchers.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.IMAGE_PNG_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class ProductsIT extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    @Order(1)
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
                .andExpect(jsonPath("$.name", is("Brand new product")))
                .andExpect(jsonPath("$.price", is(123.456)))
                .andExpect(jsonPath("$.imageLink", nullValue()));
    }

    @Test
    @Order(2)
    @DisplayName("Should upload image for a product")
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

}
package kz.toko.app.filter;

import kz.toko.app.IntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.options;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
public class CorsFilterIT extends IntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Tag("security")
    @DisplayName("Should return allowed methods on CORS request")
    void shouldReturnAllowedMethods() throws Exception {
        this.mockMvc.perform(
                        options("/products")
                                .contentType(APPLICATION_JSON)
                                .header("Access-Control-Request-Method", "GET")
                                .header("Origin", "http://www.someurl.com"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string("Access-Control-Allow-Methods", containsString("GET")));
    }

}

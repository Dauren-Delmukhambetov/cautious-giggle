package kz.toko.app.event.listener;

import kz.toko.api.model.Product;
import kz.toko.app.event.ProductImageChangeEvent;
import kz.toko.app.service.FileStorageService;
import kz.toko.app.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = ProductEventListener.class)
public class ProductEventListenerIT {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @MockBean
    private ProductService productService;

    @MockBean
    private FileStorageService fileStorageService;

    @Test
    @DisplayName("Should not delete image when found products linked to this image")
    void shouldNotDeleteImage_whenProductsWithTheSameImageAreFound() {
        final var productId = 123L;
        final var previousImagePath = "img/this.png";
        final var newImagePath = "img/water.png";
        final var productsWithTheSameImage = List.of(new Product());

        when(productService.findByImagePath(previousImagePath))
                .thenReturn(productsWithTheSameImage);

        final var event = new ProductImageChangeEvent(this, productId, previousImagePath, newImagePath);
        applicationEventPublisher.publishEvent(event);

        verifyNoInteractions(fileStorageService);
    }

    @Test
    @DisplayName("Should delete image when these are no other products linked to this image")
    void shouldDeleteImage_whenNoProductsWithTheSameImage() {
        final var productId = 123L;
        final var previousImagePath = "img/this.png";
        final var newImagePath = "img/water.png";

        when(productService.findByImagePath(previousImagePath))
                .thenReturn(emptyList());

        final var event = new ProductImageChangeEvent(this, productId, previousImagePath, newImagePath);
        applicationEventPublisher.publishEvent(event);

        verify(fileStorageService).delete(previousImagePath);
    }
}

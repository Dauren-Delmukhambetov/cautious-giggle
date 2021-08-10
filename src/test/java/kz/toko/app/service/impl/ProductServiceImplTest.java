package kz.toko.app.service.impl;

import kz.toko.api.model.Product;
import kz.toko.app.entity.ProductEntity;
import kz.toko.app.exception.EntityDeletedException;
import kz.toko.app.exception.EntityNotFoundException;
import kz.toko.app.mapper.ProductMapper;
import kz.toko.app.repository.ProductRepository;
import kz.toko.app.service.FileStorageService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository repository;

    @Mock
    private ProductMapper mapper;

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    @DisplayName("when user exists then return user")
    void findById() {
        when(repository.findById(123L))
                .thenReturn(Optional.of(new ProductEntity()));
        when(mapper.toDto(new ProductEntity()))
                .thenReturn(new Product());

        final var product = productService.findById(123L);

        assertThat(product).isNotNull();
    }

    @Test
    void shouldThrowException_WhenUserNotFound() {
        assertThrows(EntityNotFoundException.class, () -> productService.findById(99L));
    }

    @Test
    void shouldThrowException_WhenUserIsDeleted() {
        final var entity = new ProductEntity();
        entity.setDeletedAt(LocalDateTime.now());
        when(repository.findById(99L))
                .thenReturn(Optional.of(entity));
        assertThrows(EntityDeletedException.class, () -> productService.findById(99L));
    }
}
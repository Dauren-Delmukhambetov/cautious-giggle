package kz.toko.app.mapper;

import kz.toko.app.config.MappingConfig;
import kz.toko.app.mapper.converter.StoreItemProductConverter;
import kz.toko.app.mapper.converter.StoreItemStoreConverter;
import kz.toko.app.repository.ProductRepository;
import kz.toko.app.repository.StoreRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.math.BigDecimal;
import java.util.Optional;

import static kz.toko.app.util.data.provider.ProductDataProvider.buildProductEntity;
import static kz.toko.app.util.data.provider.StoreDataProvider.buildStoreEntity;
import static kz.toko.app.util.data.provider.StoreItemDataProvider.buildCreateStoreItemRequest;
import static kz.toko.app.util.data.provider.StoreItemDataProvider.buildStoreItemEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = {
        MappingConfig.class,
        StoreItemProductConverter.class,
        StoreItemStoreConverter.class,
        StoreItemMapper.class
})
class StoreItemMapperTest {

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private StoreRepository storeRepository;

    @SpyBean
    private ModelMapper modelMapper;

    @Autowired
    private StoreItemMapper mapper;

    @Test
    @DisplayName("should map CreateStoreItemRequest to StoreItemEntity with retrieving product and store from the database")
    void shouldMapCreateStoreItemRequestToStoreItemEntity() {
        final var request = buildCreateStoreItemRequest();
        when(productRepository.findById(any())).thenReturn(Optional.of(buildProductEntity(123L)));
        when(storeRepository.findById(any())).thenReturn(Optional.of(buildStoreEntity(456L)));

        final var entity = mapper.toEntity(request);

        assertThat(entity.getProduct()).isNotNull().hasFieldOrPropertyWithValue("id", request.getProductId());
        assertThat(entity.getStore()).isNotNull().hasFieldOrPropertyWithValue("id", request.getStoreId());
        assertThat(entity.getActiveSince().toLocalDate()).isEqualTo(request.getActiveSince());
        assertThat(entity.getActiveTill().toLocalDate()).isEqualTo(request.getActiveTill());
        assertThat(entity.getMeasureUnit().name()).isEqualTo(request.getMeasureUnit().name());
        assertThat(entity.getAmount().compareTo(BigDecimal.valueOf(request.getAmount()))).isZero();
        assertThat(entity.getPrice().compareTo(BigDecimal.valueOf(request.getPrice()))).isZero();
    }

    @Test
    @DisplayName("should map StoreItemEntity to StoreItem with flattening nested properties")
    void shouldMapStoreItemEntityToStoreItemDto() {
        final var entity = buildStoreItemEntity(123L, buildProductEntity(456L), buildStoreEntity(789L));

        final var dto = mapper.toDto(entity);

        assertThat(dto.getProductId()).isEqualTo(entity.getProduct().getId());
        assertThat(dto.getProductName()).isEqualTo(entity.getProduct().getName());
        assertThat(dto.getProductImageLink()).endsWith(entity.getProduct().getImagePath());

        assertThat(dto.getStoreId()).isEqualTo(entity.getStore().getId());
        assertThat(dto.getStoreName()).isEqualTo(entity.getStore().getName());

        assertThat(dto.getAmount()).isEqualTo(entity.getAmount().doubleValue());
        assertThat(dto.getPrice()).isEqualTo(entity.getPrice().doubleValue());

        assertThat(dto.getActiveSince()).isEqualTo(entity.getActiveSince().toLocalDate());
        assertThat(dto.getActiveTill()).isEqualTo(entity.getActiveTill().toLocalDate());
    }
}

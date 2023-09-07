package kz.toko.app.config;

import kz.toko.api.model.CreateStoreItemRequest;
import kz.toko.api.model.Product;
import kz.toko.api.model.Store;
import kz.toko.api.model.StoreItem;
import kz.toko.app.entity.ProductEntity;
import kz.toko.app.entity.StoreEntity;
import kz.toko.app.entity.StoreItemEntity;
import kz.toko.app.mapper.converter.ProductImageLinkConverter;
import kz.toko.app.mapper.converter.StoreItemProductConverter;
import kz.toko.app.mapper.converter.StoreItemStoreConverter;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;
import static org.modelmapper.convention.MatchingStrategies.STRICT;

@Setter
@Configuration
@ConfigurationProperties(prefix = "storage")
public class MappingConfig {

    private String endpoint;

    @Autowired
    private StoreItemProductConverter storeItemProductConverter;

    @Autowired
    private StoreItemStoreConverter storeItemStoreConverter;

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
                .setMatchingStrategy(STRICT)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true)
                .setFieldAccessLevel(PRIVATE);

        configureMappings(mapper);

        return mapper;
    }

    @Bean
    public ProductImageLinkConverter productImageLinkConverter() {
        return new ProductImageLinkConverter(this.endpoint);
    }


    private void configureMappings(ModelMapper modelMapper) {
        modelMapper.typeMap(ProductEntity.class, Product.class)
                .addMappings(mapper -> mapper.using(productImageLinkConverter())
                        .map(ProductEntity::getImagePath, Product::setImageLink));

        modelMapper.typeMap(StoreEntity.class, Store.class)
                .addMapping(src -> src.getOwner().getFullName(), Store::setOwnerFullName);

        modelMapper.typeMap(CreateStoreItemRequest.class, StoreItemEntity.class)
                .addMappings(mapper -> mapper.skip(StoreItemEntity::setItemId))
                .addMappings(mapper -> mapper.using(localDateToLocalDateTimeConverter)
                        .map(CreateStoreItemRequest::getActiveSince, StoreItemEntity::setActiveSince))
                .addMappings(mapper -> mapper.using(localDateToLocalDateTimeConverter)
                        .map(CreateStoreItemRequest::getActiveTill, StoreItemEntity::setActiveTill))
                .addMappings(mapper -> mapper.using(storeItemProductConverter)
                        .map(CreateStoreItemRequest::getProductId, StoreItemEntity::setProduct))
                .addMappings(mapper -> mapper.using(storeItemStoreConverter)
                        .map(CreateStoreItemRequest::getStoreId, StoreItemEntity::setStore));

        modelMapper.typeMap(StoreItemEntity.class, StoreItem.class)
                .addMappings(mapper -> mapper.map(src -> src.getProduct().getId(), StoreItem::setProductId))
                .addMappings(mapper -> mapper.map(src -> src.getProduct().getName(), StoreItem::setProductName))
                .addMappings(mapper -> mapper.using(productImageLinkConverter())
                        .map(src -> src.getProduct().getImagePath(), StoreItem::setProductImageLink))
                .addMappings(mapper -> mapper.map(src -> src.getStore().getId(), StoreItem::setStoreId))
                .addMappings(mapper -> mapper.map(src -> src.getStore().getName(), StoreItem::setStoreName))
                .addMappings(mapper -> mapper.using(localDateTimeToLocalDateConverter)
                        .map(StoreItemEntity::getActiveSince, StoreItem::setActiveSince))
                .addMappings(mapper -> mapper.using(localDateTimeToLocalDateConverter)
                        .map(StoreItemEntity::getActiveTill, StoreItem::setActiveTill));
    }

    private final Converter<LocalDate, LocalDateTime> localDateToLocalDateTimeConverter =
            context -> Objects.nonNull(context.getSource()) ? context.getSource().atStartOfDay() : null;

    private final Converter<LocalDateTime, LocalDate> localDateTimeToLocalDateConverter =
            context -> Objects.nonNull(context.getSource()) ? context.getSource().toLocalDate() : null;

}

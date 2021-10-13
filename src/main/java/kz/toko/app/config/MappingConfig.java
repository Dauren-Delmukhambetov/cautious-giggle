package kz.toko.app.config;

import kz.toko.api.model.Product;
import kz.toko.api.model.Store;
import kz.toko.app.entity.ProductEntity;
import kz.toko.app.entity.StoreEntity;
import kz.toko.app.mapper.converter.ProductImageLinkConverter;
import lombok.Setter;
import org.modelmapper.ModelMapper;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;
import static org.modelmapper.convention.MatchingStrategies.STRICT;

@Setter
@Configuration
@ConfigurationProperties(prefix = "storage")
public class MappingConfig {

    private String endpoint;

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
    }
}

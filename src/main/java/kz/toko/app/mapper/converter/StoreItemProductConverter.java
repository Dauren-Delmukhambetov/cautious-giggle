package kz.toko.app.mapper.converter;

import kz.toko.app.entity.ProductEntity;
import kz.toko.app.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

@Component
@RequiredArgsConstructor
public class StoreItemProductConverter implements Converter<Long, ProductEntity> {

    private final ProductRepository productRepository;

    @Override
    public ProductEntity convert(MappingContext<Long, ProductEntity> context) {
        if (ObjectUtils.isEmpty(context.getSource())) return null;

        return productRepository.findById(context.getSource()).orElse(null);
    }
}

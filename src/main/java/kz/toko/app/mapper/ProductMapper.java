package kz.toko.app.mapper;

import kz.toko.api.model.CreateProductRequest;
import kz.toko.api.model.Product;
import kz.toko.api.model.UpdateProductRequest;
import kz.toko.app.entity.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductMapper implements EntityDtoMapper<ProductEntity, Product> {

    private final ModelMapper modelMapper;

    @Override
    public Product toDto(ProductEntity entity) {
        return modelMapper.map(entity, Product.class);
    }

    @Override
    public ProductEntity toEntity(Product dto) {
        return modelMapper.map(dto, ProductEntity.class);
    }

    public ProductEntity toEntity(CreateProductRequest request) {
        return modelMapper.map(request, ProductEntity.class);
    }

    public ProductEntity toEntity(UpdateProductRequest request, ProductEntity entity) {
        modelMapper.map(request, entity);
        return entity;
    }
}

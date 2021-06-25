package kz.toko.app.service.impl;

import kz.toko.api.model.CreateProductRequest;
import kz.toko.api.model.Product;
import kz.toko.app.entity.ProductEntity;
import kz.toko.app.mapper.ProductMapper;
import kz.toko.app.repository.ProductRepository;
import kz.toko.app.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    @Override
    public List<Product> findAll() {
        final var entities = new LinkedList<ProductEntity>();
        repository.findAll().forEach(entities::add);
        return mapper.toDto(entities);
    }

    @Override
    public Product createNewProduct(CreateProductRequest request) {
        final var product = mapper.toEntity(request);
        product.setCreatedAt(LocalDateTime.now());
        return mapper.toDto(repository.save(product));
    }
}

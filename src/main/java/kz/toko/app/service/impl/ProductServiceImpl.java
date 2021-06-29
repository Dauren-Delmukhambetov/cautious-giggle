package kz.toko.app.service.impl;

import kz.toko.api.model.CreateProductRequest;
import kz.toko.api.model.Product;
import kz.toko.app.entity.ProductEntity;
import kz.toko.app.exception.EntityNotFoundException;
import kz.toko.app.mapper.ProductMapper;
import kz.toko.app.repository.ProductRepository;
import kz.toko.app.service.FileStorageService;
import kz.toko.app.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    private final FileStorageService fileStorageService;

    @Override
    public List<Product> findAll() {
        final var entities = new LinkedList<ProductEntity>();
        repository.findAll().forEach(entities::add);
        return mapper.toDto(entities);
    }

    @Override
    public Product findById(Long id) {
        final var entity = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product", id));
        return mapper.toDto(entity);
    }

    @Override
    public Product createNewProduct(CreateProductRequest request) {
        final var product = mapper.toEntity(request);
        product.setCreatedAt(LocalDateTime.now());
        return mapper.toDto(repository.save(product));
    }

    @SneakyThrows
    @Override
    public void setProductImage(Long productId, MultipartFile image) {
        final var product = repository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product", productId));

        final var imagePath = fileStorageService.write(image);
        product.setImagePath(imagePath);
        repository.save(product);
    }
}

package kz.toko.app.service.impl;

import kz.toko.api.model.CreateProductRequest;
import kz.toko.api.model.Product;
import kz.toko.api.model.UpdateProductRequest;
import kz.toko.app.entity.ProductEntity;
import kz.toko.app.event.ProductImageChangeEvent;
import kz.toko.app.exception.EntityDeletedException;
import kz.toko.app.exception.EntityNotFoundException;
import kz.toko.app.mapper.ProductMapper;
import kz.toko.app.repository.ProductRepository;
import kz.toko.app.service.FileStorageService;
import kz.toko.app.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final ProductMapper mapper;

    private final FileStorageService fileStorageService;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public List<Product> findAll() {
        return repository.findByDeletedAtIsNull()
                .stream()
                .map(mapper::toDto)
                .collect(toList());
    }

    @Override
    public List<Product> findByImagePath(final String imagePath) {
        return repository.findByImagePath(imagePath)
                .stream()
                .map(mapper::toDto)
                .collect(toList());
    }

    @Override
    public Product findById(final Long id) {
        final var entity = getAccessibleProduct(id);
        return mapper.toDto(entity);
    }

    @Override
    public Product createNewProduct(CreateProductRequest request) {
        final var product = mapper.toEntity(request);
        return mapper.toDto(repository.save(product));
    }

    @Override
    public void updateProduct(Long productId, UpdateProductRequest request) {
        final var product = getAccessibleProduct(productId);
        repository.save(mapper.toEntity(request, product));
    }

    @Override
    @SneakyThrows
    @Transactional
    public void setProductImage(Long productId, MultipartFile image) {
        final var product = getAccessibleProduct(productId);
        final var imagePath = fileStorageService.write(image);
        final var event = new ProductImageChangeEvent(this, productId, product.getImagePath(), imagePath);
        product.setImagePath(imagePath);
        repository.save(product);

        applicationEventPublisher.publishEvent(event);
    }

    @Override
    public void delete(Long productId) {
        final var product = getAccessibleProduct(productId);
        product.setDeletedAt(LocalDateTime.now());
        repository.save(product);
    }

    private ProductEntity getAccessibleProduct(Long productId) {
        final var product = repository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException(Product.class, productId));

        if (product.getDeletedAt() != null) {
            throw new EntityDeletedException(Product.class, productId, product.getDeletedAt());
        }

        return product;
    }
}

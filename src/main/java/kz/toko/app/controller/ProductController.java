package kz.toko.app.controller;

import kz.toko.api.model.CreateProductRequest;
import kz.toko.api.model.Link;
import kz.toko.api.model.Product;
import kz.toko.api.model.UpdateProductRequest;
import kz.toko.app.mapper.ProductMapper;
import kz.toko.app.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
public class ProductController implements ProductsApi {

    private final ProductService productService;
    private final ProductMapper mapper;

    @Override
    public ResponseEntity<Product> createProduct(@Valid final CreateProductRequest body) {
        return new ResponseEntity<>(productService.createNewProduct(body), CREATED);
    }

    @Override
    public ResponseEntity<Void> deleteProduct(Long id) {
        productService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Product> getProduct(Long id) {
        return ResponseEntity.ok(mapper.toDto(productService.findById(id)));
    }

    @Override
    public ResponseEntity<List<Product>> getProducts() {
        return ok(productService.findAll());
    }

    @Override
    public ResponseEntity<Void> updateProduct(Long id, UpdateProductRequest body) {
        productService.updateProduct(id, body);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Link> uploadImage(final Long id, final MultipartFile file) {
        productService.setProductImage(id, file);
        final var product = mapper.toDto(productService.findById(id));
        return new ResponseEntity<>(new Link().link(product.getImageLink()), CREATED);
    }
}

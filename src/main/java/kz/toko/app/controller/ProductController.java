package kz.toko.app.controller;

import kz.toko.api.ProductsApi;
import kz.toko.api.model.CreateProductRequest;
import kz.toko.api.model.Link;
import kz.toko.api.model.Product;
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

    @Override
    public ResponseEntity<Product> createProduct(@Valid final CreateProductRequest body) {
        return new ResponseEntity<>(productService.createNewProduct(body), CREATED);
    }

    @Override
    public ResponseEntity<List<Product>> getProducts() {
        return ok(productService.findAll());
    }

    @Override
    public ResponseEntity<Link> uploadImage(final Long id, final MultipartFile file) {
        productService.setProductImage(id, file);
        final var product = productService.findById(id);
        return new ResponseEntity<>(new Link().link(product.getImageLink()), CREATED);
    }
}

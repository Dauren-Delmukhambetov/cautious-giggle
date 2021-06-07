package kz.toko.app.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import kz.toko.api.ProductsApi;
import kz.toko.api.model.CreateProductRequest;
import kz.toko.api.model.Product;
import kz.toko.app.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
public class ProductController implements ProductsApi {

    private final ProductService productService;

    @Override
    @Tag(name = "Products")
    public ResponseEntity<Product> createProduct(@Valid CreateProductRequest body) {
        return new ResponseEntity(productService.createNewProduct(body), CREATED);
    }

    @Override
    @Tag(name = "Products")
    public ResponseEntity<List<Product>> getProducts() {
        return ok(productService.findAll());
    }
}

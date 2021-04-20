package kz.toko.app.controller;

import kz.toko.api.ProductsApi;
import kz.toko.api.model.Product;
import kz.toko.app.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequiredArgsConstructor
public class ProductController implements ProductsApi {

    private final ProductService productService;

    @Override
    public ResponseEntity<List<Product>> getProducts() {
        return ok(productService.findAll());
    }
}

package kz.toko.app.controller;

import kz.toko.api.ProductsApi;
import kz.toko.api.model.Product;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ProductsController implements ProductsApi {
    @Override
    public ResponseEntity<List<Product>> getProducts() {
        return null;
    }
}

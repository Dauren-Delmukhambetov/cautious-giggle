package kz.toko.app.service;

import kz.toko.api.model.CreateProductRequest;
import kz.toko.api.model.Product;

import java.util.List;

public interface ProductService {

    List<Product> findAll();

    Product createNewProduct(CreateProductRequest request);

}

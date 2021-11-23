package kz.toko.app.service;

import kz.toko.api.model.CreateProductRequest;
import kz.toko.api.model.Product;
import kz.toko.api.model.UpdateProductRequest;
import kz.toko.app.entity.ProductEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    List<Product> findAll();

    List<Product> findByImagePath(String imagePath);

    ProductEntity findById(Long id);

    Product createNewProduct(CreateProductRequest request);

    void updateProduct(Long productId, UpdateProductRequest request);

    void setProductImage(Long productId, MultipartFile image);

    void delete(Long productId);

}

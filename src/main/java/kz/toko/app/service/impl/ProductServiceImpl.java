package kz.toko.app.service.impl;

import kz.toko.api.model.Product;
import kz.toko.app.service.ProductService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    @Override
    public List<Product> findAll() {
        return null;
    }
}

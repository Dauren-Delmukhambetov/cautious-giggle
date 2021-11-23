package kz.toko.app.util.data.provider;

import kz.toko.app.entity.ProductEntity;

public class ProductDataProvider {

    public static ProductEntity buildProductEntity(final Long id) {
        final var entity = new ProductEntity();
        entity.setId(id);
        entity.setName("Product #1");
        entity.setPrice(123.45);
        entity.setImagePath("/images/product.png");
        return entity;
    }
}

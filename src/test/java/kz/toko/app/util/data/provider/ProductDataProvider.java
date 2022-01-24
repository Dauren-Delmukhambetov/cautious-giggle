package kz.toko.app.util.data.provider;

import kz.toko.app.entity.ProductEntity;

import static kz.toko.app.util.data.provider.FakeDataProvider.faker;
import static kz.toko.app.util.data.provider.FakeDataProvider.randomPrice;

public class ProductDataProvider {

    public static ProductEntity buildProductEntity(final Long id) {
        final var entity = new ProductEntity();
        entity.setId(id);
        entity.setName(faker.commerce().productName());
        entity.setPrice(randomPrice());
        entity.setImagePath(faker.file().fileName());
        return entity;
    }
}

package kz.toko.app.util.data.provider;

import kz.toko.app.entity.MeasureUnit;
import kz.toko.app.entity.ProductEntity;
import kz.toko.app.entity.StoreEntity;
import kz.toko.app.entity.StoreItemEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class StoreItemDataProvider {

    public static StoreItemEntity buildStoreItemEntity(final ProductEntity product, final StoreEntity store) {
        final var entity = new StoreItemEntity();
        entity.setProduct(product);
        entity.setStore(store);
        entity.setPrice(BigDecimal.valueOf(123.45));
        entity.setAmount(BigDecimal.valueOf(987.65));
        entity.setMeasureUnit(MeasureUnit.PIECES);
        entity.setActiveSince(LocalDateTime.now().minusDays(3));
        entity.setActiveTill(entity.getActiveSince().plusMonths(1));
        return entity;
    }

}

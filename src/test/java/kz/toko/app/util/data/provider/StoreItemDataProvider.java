package kz.toko.app.util.data.provider;

import kz.toko.api.model.CreateStoreItemRequest;
import kz.toko.api.model.CreateStoreRequest;
import kz.toko.app.entity.MeasureUnit;
import kz.toko.app.entity.ProductEntity;
import kz.toko.app.entity.StoreEntity;
import kz.toko.app.entity.StoreItemEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static kz.toko.api.model.MeasureUnit.PIECES;

public class StoreItemDataProvider {

    public static StoreItemEntity buildStoreItemEntity(final ProductEntity product, final StoreEntity store) {
        return buildStoreItemEntity(null, product, store);
    }

    public static StoreItemEntity buildStoreItemEntity(final Long itemId, final ProductEntity product, final StoreEntity store) {
        final var entity = new StoreItemEntity();
        entity.setItemId(itemId);
        entity.setProduct(product);
        entity.setStore(store);
        entity.setPrice(BigDecimal.valueOf(123.45));
        entity.setAmount(BigDecimal.valueOf(987.65));
        entity.setMeasureUnit(MeasureUnit.PIECES);
        entity.setActiveSince(LocalDateTime.now().minusDays(3));
        entity.setActiveTill(entity.getActiveSince().plusMonths(1));
        return entity;
    }

    public static CreateStoreItemRequest buildCreateStoreItemRequest() {
        return new CreateStoreItemRequest()
                .productId(123L)
                .storeId(456L)
                .price(123.45)
                .amount(987.65)
                .measureUnit(PIECES)
                .activeSince(LocalDate.now().minusDays(3))
                .activeTill(LocalDate.now().plusMonths(1));
    }

}

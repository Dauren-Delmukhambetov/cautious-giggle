package kz.toko.app.util.data.provider;

import kz.toko.api.model.CreateStoreItemRequest;
import kz.toko.app.entity.MeasureUnit;
import kz.toko.app.entity.ProductEntity;
import kz.toko.app.entity.StoreEntity;
import kz.toko.app.entity.StoreItemEntity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static kz.toko.api.model.MeasureUnit.PIECES;
import static kz.toko.app.util.data.provider.FakeDataProvider.randomAmount;
import static kz.toko.app.util.data.provider.FakeDataProvider.randomPrice;

public class StoreItemDataProvider {

    public static StoreItemEntity buildStoreItemEntity(final ProductEntity product, final StoreEntity store) {
        return buildStoreItemEntity(null, product, store);
    }

    public static StoreItemEntity buildStoreItemEntity(final Long itemId, final ProductEntity product, final StoreEntity store) {
        final var entity = new StoreItemEntity();
        entity.setItemId(itemId);
        entity.setProduct(product);
        entity.setStore(store);
        entity.setPrice(randomPrice());
        entity.setAmount(randomAmount());
        entity.setMeasureUnit(MeasureUnit.PIECES);
        entity.setActiveSince(LocalDateTime.now().minusDays(3));
        entity.setActiveTill(entity.getActiveSince().plusMonths(1));
        return entity;
    }

    public static CreateStoreItemRequest buildCreateStoreItemRequest() {
        return buildCreateStoreItemRequest(123L, 456L);
    }

    public static CreateStoreItemRequest buildCreateStoreItemRequest(final Long productId, final Long storeId) {
        return new CreateStoreItemRequest()
                .productId(productId)
                .storeId(storeId)
                .price(randomPrice().doubleValue())
                .amount(randomAmount().doubleValue())
                .measureUnit(PIECES)
                .activeSince(LocalDate.now().minusDays(3))
                .activeTill(LocalDate.now().plusMonths(1));
    }
}

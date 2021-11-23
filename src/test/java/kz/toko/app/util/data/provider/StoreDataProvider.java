package kz.toko.app.util.data.provider;

import kz.toko.app.entity.StoreEntity;
import kz.toko.app.entity.StoreMode;

import static kz.toko.app.util.data.provider.AddressDataProvider.buildAddressEntity;
import static kz.toko.app.util.data.provider.UserDataProvider.buildUserEntity;

public final class StoreDataProvider {

    public static StoreEntity buildStoreEntity() {
        return buildStoreEntity(null);
    }

    public static StoreEntity buildStoreEntity(final Long id) {
        final var entity = new StoreEntity();
        entity.setId(id);
        entity.setName("Store #1");
        entity.setMode(StoreMode.SELLER);
        entity.setAddress(buildAddressEntity());
        entity.setOwner(buildUserEntity());
        return entity;
    }
}

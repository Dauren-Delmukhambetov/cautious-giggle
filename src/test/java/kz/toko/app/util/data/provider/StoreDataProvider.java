package kz.toko.app.util.data.provider;

import kz.toko.app.entity.StoreEntity;
import kz.toko.app.entity.StoreMode;
import kz.toko.app.entity.UserEntity;

import static kz.toko.app.util.data.provider.AddressDataProvider.buildAddressEntity;
import static kz.toko.app.util.data.provider.FakeDataProvider.faker;
import static kz.toko.app.util.data.provider.UserDataProvider.buildUserEntity;

public final class StoreDataProvider {

    public static StoreEntity buildStoreEntity() {
        return buildStoreEntity(null, buildUserEntity());
    }

    public static StoreEntity buildStoreEntity(final Long id) {
        return buildStoreEntity(id, buildUserEntity());
    }

    public static StoreEntity buildStoreEntity(final Long id, final UserEntity owner) {
        final var entity = new StoreEntity();
        entity.setId(id);
        entity.setName(faker.company().name());
        entity.setMode(StoreMode.SELLER);
        entity.setAddress(buildAddressEntity());
        entity.setOwner(owner);
        return entity;
    }
}

package kz.toko.app.util.data.builder;

import kz.toko.app.entity.ProductEntity;
import kz.toko.app.entity.StoreEntity;
import kz.toko.app.entity.StoreItemEntity;
import kz.toko.app.entity.UserEntity;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static kz.toko.app.util.data.provider.ProductDataProvider.buildProductEntity;
import static kz.toko.app.util.data.provider.StoreDataProvider.buildStoreEntity;
import static kz.toko.app.util.data.provider.StoreItemDataProvider.buildStoreItemEntity;
import static kz.toko.app.util.data.provider.UserDataProvider.buildUserEntity;

public class TestDataSetBuilder {

    private final EntityManager entityManager;
    private final List<UserEntity> users = new LinkedList<>();
    private final List<ProductEntity> products = new LinkedList<>();
    private final List<StoreEntity> stores = new LinkedList<>();
    private final List<StoreItemEntity> storeItems = new LinkedList<>();

    public static TestDataSetBuilder builder(EntityManager entityManager) {
        return new TestDataSetBuilder(entityManager);
    }

    private TestDataSetBuilder(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public TestDataSetBuilder user() {
        final var entity = buildUserEntity();
        entityManager.persist(entity);
        users.add(entity);
        return this;
    }

    public TestDataSetBuilder product() {
        final var entity = buildProductEntity(null);
        entityManager.persist(entity);
        products.add(entity);
        return this;
    }

    public TestDataSetBuilder store() {
        final var entity = buildStoreEntity(null, getLastUser());
        entityManager.persist(entity);
        stores.add(entity);
        return this;
    }

    public TestDataSetBuilder activeStoreItem() {
        final var entity = buildStoreItemEntity(getLastProduct(), getLastStore());
        return storeItem(entity);
    }

    public TestDataSetBuilder expiredStoreItem() {
        final var entity = buildStoreItemEntity(getLastProduct(), getLastStore());
        entity.setActiveSince(LocalDateTime.now().minusMonths(3));
        entity.setActiveTill(entity.getActiveSince().plusMonths(2));
        return storeItem(entity);
    }

    public TestDataSetBuilder upcomingStoreItem() {
        final var entity = buildStoreItemEntity(getLastProduct(), getLastStore());
        entity.setActiveSince(LocalDateTime.now().plusMonths(3));
        entity.setActiveTill(entity.getActiveSince().plusMonths(2));
        return storeItem(entity);
    }
    private TestDataSetBuilder storeItem(final StoreItemEntity entity) {
        entityManager.persist(entity);
        storeItems.add(entity);
        return this;
    }

    public ProductEntity getLastProduct() {
        return products.isEmpty() ? null : products.get(products.size() - 1);
    }

    public StoreEntity getLastStore() {
        return stores.isEmpty() ? null : stores.get(stores.size() - 1);
    }

    public UserEntity getLastUser() {
        return users.isEmpty() ? null : users.get(users.size() - 1);
    }
}

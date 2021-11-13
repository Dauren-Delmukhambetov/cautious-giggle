package kz.toko.app.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;

import static kz.toko.app.util.data.provider.StoreItemDataProvider.buildStoreItemEntity;
import static org.assertj.core.api.Assertions.assertThat;

@Tag("persistence")
@SqlGroup({
        @Sql(value = "classpath:/db.scripts/add_one_user.sql"),
        @Sql(value = "classpath:/db.scripts/add_one_product.sql"),
        @Sql(value = "classpath:/db.scripts/add_adam_store.sql")
})
public class StoreItemRepositoryIT extends PersistenceTest {

    @Autowired
    private StoreItemRepository repository;

    @Autowired
    private StoreRepository storeRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("should save a valid store item")
    void shouldSaveStoreItem() {
        final var product = productRepository.findById(1L).get();
        final var store = storeRepository.findById(1L).get();
        final var storeItem = buildStoreItemEntity(product, store);

        final var savedStoreItem = repository.save(storeItem);

        assertThat(savedStoreItem).isNotNull();
        assertThat(savedStoreItem.getStore()).isNotNull();
        assertThat(savedStoreItem.getStore().getId()).isEqualTo(store.getId());
        assertThat(savedStoreItem.getProduct()).isNotNull();
        assertThat(savedStoreItem.getProduct().getId()).isEqualTo(product.getId());
    }
}

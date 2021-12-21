package kz.toko.app.repository;

import kz.toko.app.repository.specification.StoreItemSpecification;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlGroup;
import org.springframework.test.context.jdbc.SqlMergeMode;

import java.time.LocalDateTime;
import java.util.Set;

import static java.util.Collections.emptySet;
import static kz.toko.app.repository.specification.StoreItemSpecification.ExpirationStatus.CURRENTLY_ACTIVE;
import static kz.toko.app.repository.specification.StoreItemSpecification.ExpirationStatus.EXPIRED;
import static kz.toko.app.repository.specification.StoreItemSpecification.ExpirationStatus.UPCOMING;
import static kz.toko.app.util.data.provider.StoreItemDataProvider.buildStoreItemEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.SqlMergeMode.MergeMode.MERGE;

@Tag("persistence")
@SqlMergeMode(MERGE)
@SqlGroup({
        @Sql(value = "classpath:/db.scripts/add_one_user.sql"),
        @Sql(value = "classpath:/db.scripts/add_one_product.sql"),
        @Sql(value = "classpath:/db.scripts/add_adam_store.sql"),
        @Sql(value = "classpath:/db.scripts/add_store_items.sql")
})
class StoreItemRepositoryIT extends PersistenceTest {

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

    @Test
    @DisplayName("should filter store items by given stores")
    void shouldFilterByStoreIds() {
        final var storeIds = Set.of(1L);
        final var specification = new StoreItemSpecification(storeIds, emptySet(), null, null);

        final var storeItems = repository.findAll(specification);

        assertThat(storeItems)
                .hasSize(3)
                .allMatch(sie ->  storeIds.contains(sie.getStore().getId()))
                .allMatch(sie ->  sie.getStore().getDeletedAt() == null);
    }

    @Test
    @DisplayName("should filter out store items in deleted stores")
    @SqlGroup({
            @Sql(value = "classpath:/db.scripts/add_deleted_store_and_its_items.sql")
    })
    void shouldFilterOutStoreItemsFromDeletedStores() {
        final var specification = new StoreItemSpecification(emptySet(), emptySet(), null, null);

        final var storeItems = repository.findAll(specification);

        assertThat(storeItems)
                .hasSize(3)
                .allMatch(sie ->  sie.getStore().getDeletedAt() == null);
    }

    @Test
    @DisplayName("should return active on a specific date store items")
    void shouldReturnActiveOnDateStoreItems() {
        final var dateTime = LocalDateTime.now().minusMonths(11);
        final var specification = new StoreItemSpecification(emptySet(), emptySet(), dateTime.toLocalDate(), null);

        final var storeItems = repository.findAll(specification);

        assertThat(storeItems)
                .hasSize(1)
                .allMatch(sie ->  sie.getStore().getDeletedAt() == null)
                .allMatch(sie -> sie.getActiveSince().isBefore(dateTime))
                .allMatch(sie -> sie.getActiveTill().isAfter(dateTime));
    }

    @Test
    @DisplayName("should return expired store items")
    void shouldReturnExpiredStoreItems() {
        final var today = LocalDateTime.now();
        final var specification = new StoreItemSpecification(null, null, null, EXPIRED);

        final var storeItems = repository.findAll(specification);

        assertThat(storeItems)
                .hasSize(1)
                .allMatch(sie ->  sie.getStore().getDeletedAt() == null)
                .allMatch(sie -> sie.getActiveSince().isBefore(today))
                .allMatch(sie -> sie.getActiveTill().isBefore(today));
    }

    @Test
    @DisplayName("should return upcoming store items")
    void shouldReturnUpcomingStoreItems() {
        final var today = LocalDateTime.now();
        final var specification = new StoreItemSpecification(null, null, null, UPCOMING);

        final var storeItems = repository.findAll(specification);

        assertThat(storeItems)
                .hasSize(1)
                .allMatch(sie ->  sie.getStore().getDeletedAt() == null)
                .allMatch(sie -> sie.getActiveSince().isAfter(today))
                .allMatch(sie -> sie.getActiveTill().isAfter(today));
    }

    @Test
    @DisplayName("should return currently active store items")
    void shouldReturnCurrentlyActiveStoreItems() {
        final var today = LocalDateTime.now();
        final var specification = new StoreItemSpecification(null, null, null, CURRENTLY_ACTIVE);

        final var storeItems = repository.findAll(specification);

        assertThat(storeItems)
                .hasSize(1)
                .allMatch(sie ->  sie.getStore().getDeletedAt() == null)
                .allMatch(sie -> sie.getActiveSince().isBefore(today))
                .allMatch(sie -> sie.getActiveTill().isAfter(today));
    }
}

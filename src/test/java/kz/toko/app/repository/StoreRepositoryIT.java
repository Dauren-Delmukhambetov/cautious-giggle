package kz.toko.app.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import static kz.toko.app.util.data.provider.AddressDataProvider.buildAddressEntity;
import static kz.toko.app.util.data.provider.StoreDataProvider.buildStoreEntity;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;


@DataJpaTest
@WithMockUser
@ActiveProfiles("it")
@AutoConfigureTestDatabase(replace = NONE)
class StoreRepositoryIT {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StoreRepository repository;

    @Test
    @Tag("persistence")
    @DisplayName("should save nested address entity with newly created store entity")
    void shouldSaveNestedAddressWithStore() {
        final var store = buildStoreEntity();

        final var savedStore = repository.save(store);

        assertThat(savedStore).isNotNull();
        assertThat(savedStore.getId()).isNotNull();
        assertThat(savedStore.getAddress()).isNotNull();
        assertThat(savedStore.getAddress().getId()).isNotNull();
        assertThat(savedStore.getOwner()).isNotNull();
        assertThat(savedStore.getOwner().getId()).isNotNull();
    }

    @Test
    @Tag("persistence")
    @DisplayName("should not create new address entity for store entity update")
    void shouldNotCreateNewAddress() {
        final var savedStore = entityManager.persistAndFlush(buildStoreEntity());
        final var addressId = savedStore.getAddress().getId();
        final var address = buildAddressEntity();
        address.setId(addressId);
        address.setPostalCode("050300");
        savedStore.setAddress(address);

        final var store = repository.save(savedStore);
        entityManager.flush();

        assertThat(store.getAddress()).isNotNull();
        assertThat(savedStore.getAddress().getId()).isEqualTo(addressId);
        assertThat(savedStore.getAddress().getPostalCode()).isEqualTo("050300");
    }
}
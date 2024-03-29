package kz.toko.app.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static kz.toko.app.util.TestConstants.MOCK_USER_DEFAULT_USERNAME;
import static kz.toko.app.util.data.provider.UserDataProvider.buildUserEntity;
import static org.assertj.core.api.Assertions.assertThat;

class UserRepositoryIT extends PersistenceTest {

    @Autowired
    private UserRepository repository;

    @Test
    @Tag("audit")
    @DisplayName("should set createdAt and createdBy properties on a new user creation")
    void shouldSetAuditColumnsOnNewUserCreation() {
        final var userEntity = buildUserEntity();

        final var entity = repository.save(userEntity);

        assertThat(entity.getId()).isNotNull();
        assertThat(entity.getCreatedAt()).isBefore(LocalDateTime.now());
        assertThat(entity.getCreatedBy()).isEqualTo(MOCK_USER_DEFAULT_USERNAME);
        assertThat(entity.getUpdatedAt()).isNull();
        assertThat(entity.getUpdatedBy()).isNull();
    }

    @Test
    @Tag("audit")
    @DisplayName("should set updatedAt and updatedBy properties on user modification")
    void shouldSetAuditColumnsOnExistingUserModification() {
        final var userEntity = entityManager.persistAndFlush(buildUserEntity());
        userEntity.setFirstName(userEntity.getFirstName() + " (updated)");

        final var entity = repository.save(userEntity);

        // forcefully flushing to execute @PreUpdate callback
        entityManager.flush();

        assertThat(entity.getUpdatedAt()).isBefore(LocalDateTime.now());
        assertThat(entity.getUpdatedBy()).isEqualTo(MOCK_USER_DEFAULT_USERNAME);
    }
}

package kz.toko.app.repository;

import kz.toko.app.entity.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@DataJpaTest(properties = {"spring.flyway.ignore-missing-migrations=true"})
@ActiveProfiles("it")
@AutoConfigureTestDatabase(replace = NONE)
class UserRepositoryIT {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository repository;

    @Test
    @DisplayName("should set createdAt and createdBy properties on a new user creation")
    void shouldSetAuditColumnsOnNewUserCreation() {
        UserEntity entity = new UserEntity();
        entity.setUsername("new.user");
        entity.setFirstName("John");
        entity.setLastName("Doe");
        entity.setPassword("password");
        entity.setEmail("new.user@example.com");

        entity = repository.save(entity);

        assertThat(entity.getCreatedAt()).isBefore(LocalDateTime.now());
        assertThat(entity.getCreatedBy()).isNotEmpty();
    }

}
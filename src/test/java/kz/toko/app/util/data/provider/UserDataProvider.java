package kz.toko.app.util.data.provider;

import kz.toko.app.entity.UserEntity;

public final class UserDataProvider {

    public static UserEntity buildUserEntity() {
        final var entity = new UserEntity();
        entity.setUsername("john.doe");
        entity.setFirstName("John");
        entity.setLastName("Doe");
        entity.setPassword("password");
        entity.setEmail("john.doe@example.com");
        entity.setPhone("+997 (29) 123-45-67");
        return entity;
    }

}

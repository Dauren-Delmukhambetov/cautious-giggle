package kz.toko.app.util.data.provider;

import kz.toko.app.entity.UserEntity;

import static kz.toko.app.util.data.provider.FakeDataProvider.faker;

public final class UserDataProvider {

    public static UserEntity buildUserEntity() {
        final var entity = new UserEntity();
        entity.setUsername(faker.name().username());
        entity.setFirstName(faker.name().firstName());
        entity.setLastName(faker.name().lastName());
        entity.setPassword(faker.internet().password());
        entity.setEmail(faker.internet().emailAddress());
        entity.setPhone(faker.phoneNumber().phoneNumber());
        return entity;
    }

}

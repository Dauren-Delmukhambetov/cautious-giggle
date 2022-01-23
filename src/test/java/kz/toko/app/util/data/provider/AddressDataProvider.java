package kz.toko.app.util.data.provider;

import kz.toko.api.model.Address;
import kz.toko.app.entity.AddressEntity;

import static kz.toko.api.model.Address.CountryEnum.BY;
import static kz.toko.app.util.data.provider.FakeDataProvider.faker;

public final class AddressDataProvider {

    public static AddressEntity buildAddressEntity() {
        final var entity = new AddressEntity();
        entity.setAddressLine(faker.address().streetAddress());
        entity.setCity(faker.address().cityName());
        entity.setAdminArea(null);
        entity.setPostalCode(faker.numerify("######"));
        entity.setCountry(faker.address().countryCode());
        return entity;
    }

    public static Address buildAddress() {
        return new Address()
                .addressLine(faker.address().streetAddress())
                .city(faker.address().cityName())
                .postalCode(faker.numerify("######"))
                .country(BY);
    }
}

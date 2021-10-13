package kz.toko.app.util.data.provider;

import kz.toko.api.model.Address;
import kz.toko.app.entity.AddressEntity;

import static kz.toko.api.model.Address.CountryEnum.BY;

public final class AddressDataProvider {

    public static AddressEntity buildAddressEntity() {
        final var entity = new AddressEntity();
        entity.setAddressLine("st. Abay, 20");
        entity.setCity("Almaty");
        entity.setAdminArea(null);
        entity.setPostalCode("050000");
        entity.setCountry("KZ");
        return entity;
    }

    public static Address buildAddress() {
        return new Address()
                .addressLine("Lenin avenue, 5/B")
                .city("Minsk")
                .postalCode("220054")
                .country(BY);
    }
}

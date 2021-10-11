package kz.toko.app.util.data.provider;

import kz.toko.app.entity.AddressEntity;

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

}

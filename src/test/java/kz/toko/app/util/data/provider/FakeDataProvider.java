package kz.toko.app.util.data.provider;

import com.github.javafaker.Faker;

import java.math.BigDecimal;

public final class FakeDataProvider {

    public static Faker faker = new Faker();

    public static BigDecimal randomPrice() {
        return BigDecimal.valueOf(faker.number().randomDouble(2, 10, 100_000));
    }

    public static BigDecimal randomAmount() {
        return BigDecimal.valueOf(faker.number().randomDouble(2, 1, 100));
    }

}

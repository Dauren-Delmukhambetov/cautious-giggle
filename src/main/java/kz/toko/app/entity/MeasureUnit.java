package kz.toko.app.entity;

public enum MeasureUnit {
    GRAM("g"),
    KILOGRAM("kg"),
    LITRE("l"),
    METRE("m"),
    PIECES("pcs")
    ;

    private final String symbol;

    MeasureUnit(String symbol) {
        this.symbol = symbol;
    }
}

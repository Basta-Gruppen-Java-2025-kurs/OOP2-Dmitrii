package CarGarage;

import Helpers.Named;

public enum FuelType implements Named {
    E95(FuelType.E95_NAME),
    E98(FuelType.E98_NAME),
    DIESEL(FuelType.DIESEL_NAME),
    ELECTRIC(FuelType.ELECTRIC_NAME),
    HYBRID(FuelType.HYBRID_NAME);

    private static final String E95_NAME = "E95", E98_NAME = "E98", DIESEL_NAME = "diesel", ELECTRIC_NAME = "electric", HYBRID_NAME = "hybrid";

    private final String name;

    FuelType(final String name) {
        this.name = name;
    }

    public static FuelType fromString(String name) {
        return switch (name) {
            case E95_NAME -> E95;
            case E98_NAME -> E98;
            case DIESEL_NAME -> DIESEL;
            case ELECTRIC_NAME -> ELECTRIC;
            case HYBRID_NAME -> HYBRID;
            default -> null;
        };
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}

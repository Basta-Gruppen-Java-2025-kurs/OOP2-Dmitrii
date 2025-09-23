package CarGarage;

import Helpers.Mappable;
import Helpers.Named;

import java.util.Map;

public class Engine implements Named, Mappable {

    private String name;
    String type;
    double horsePower;
    FuelType fuelType;

    private static final String name_field = "name", type_field = "type", horsePower_field = "horsePower", fuelType_field = "fuelType";

    public Engine(String name, String type, double horsePower, FuelType fuelType) {
        this.name = name;
        this.type = type;
        this.horsePower = horsePower;
        this.fuelType = fuelType;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + " (Type: " + type + "; Horse power: " + horsePower + "; Fuel type: " + fuelType + ")";
    }

    @Override
    public Map<String, String> toMap() {
        return Map.of(name_field, name, type_field, type, horsePower_field, "" + horsePower, fuelType_field, "" + fuelType);
    }

    @Override
    public boolean fromMap(Map<String, String> definition) {
        String newName, newType;
        double newHorsePower;
        FuelType newFuelType;
        if (definition.containsKey(name_field)) {
            newName = definition.get(name_field);
        } else {
            return false;
        }
        if (definition.containsKey(type_field)) {
            newType = definition.get(type_field);
        } else {
            return false;
        }
        if (definition.containsKey(horsePower_field)) {
            newHorsePower = Double.parseDouble(definition.get(horsePower_field));
        } else {
            return false;
        }
        if (definition.containsKey(fuelType_field)) {
            newFuelType = FuelType.fromString(definition.get(fuelType_field));
        } else {
            return false;
        }

        this.fuelType = newFuelType;
        this.name = newName;
        this.horsePower = newHorsePower;
        this.type = newType;
        return true;
    }
}

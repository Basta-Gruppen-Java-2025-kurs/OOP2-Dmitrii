package CarGarage;

import Helpers.Mappable;
import Helpers.Named;

import java.util.Map;

public class Car implements Named, Mappable {

    private String model;
    private Engine engine;
    private String licensePlate;
    private Driver assignedDriver;

    private static final String model_field = "model", engine_field = "engine", licensePlate_field = "licensePlate";

    public Car(String model, Engine engine, String licensePlate) {
        this.model = model;
        this.engine = engine;
        this.licensePlate = licensePlate;
    }

    public void assignDriver(Driver driver) {
        assignedDriver = driver;
    }

    public Driver getAssignedDriver() {
        return assignedDriver;
    }

    @Override
    public String getName() {
        return licensePlate;
    }

    @Override
    public Map<String, String> toMap() {
        return Map.of(engine_field, engine.getName(), model_field, model, licensePlate_field, licensePlate);
    }

    public boolean fromMap(Map<String, String> definition) {
        Engine newEngine;
        String newModel, newLicensePlate;
        if (definition.containsKey(engine_field)) {
            newEngine = Garage.getInstance().findEngineByName(definition.get(engine_field));
        } else {
            return false;
        }
        if (definition.containsKey(model_field)) {
            newModel = definition.get(model_field);
        } else {
            return false;
        }
        if (definition.containsKey(licensePlate_field)) {
            newLicensePlate = definition.get(licensePlate_field);
        } else {
            return false;
        }
        this.engine = newEngine;
        this.licensePlate = newLicensePlate;
        this.model = newModel;
        return true;
    }
}

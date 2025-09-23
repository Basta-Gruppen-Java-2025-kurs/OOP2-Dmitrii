package CarGarage;

import Helpers.Mappable;
import Helpers.Menu;
import Helpers.Named;

import java.util.Date;
import java.util.Map;

public class Car implements Named, Mappable, Menu {

    private String model;
    private Engine engine;
    private String licensePlate;
    private Driver assignedDriver;
    private LicenceType licenseRequirement;
    private boolean engineOn;
    private Date lastService;
    private Date obtainedDate;

    private static final String model_field = "model", engine_field = "engine", licensePlate_field = "licensePlate";

    public Car(String model, Engine engine, String licensePlate, LicenceType licenseRequirement) {
        this.model = model;
        this.engine = engine;
        this.licensePlate = licensePlate;
        this.engineOn = false;
    }

    public void assignDriver(Driver driver) {
        assignedDriver = driver;
    }

    public Driver getAssignedDriver() {
        return assignedDriver;
    }

    @Override
    public String getName() {
        return licensePlate + " (" + model + ")";
    }

    @Override
    public String toString() {
        return this.model + ", engine: " + this.engine + " (" + (this.engineOn ? "on" : "off") + " license plate: " + this.licensePlate + "." + (this.assignedDriver == null ? "" : " Driver: " + this.assignedDriver.getName());
    }

    public void turnOn() {
        engineOn = true;
    }

    public void turnOff() {
        engineOn = false;
    }

    public boolean isOn() {
        return engineOn;
    }

    public Engine getEngine() {
        return engine;
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

    @Override
    public void menu() {
        System.out.println("Car menu.");
    }
}

package CarGarage;

import Helpers.Mappable;
import Helpers.Named;

import java.util.Map;

public class Driver implements Named, Mappable {

    private String name;
    private LicenceType license;
    private int yearsOfExperience;
    private Car assignedCar = null;

    private final String name_field = "name", licenseType_field = "licenseType", yearsOfExperience_field = "yearsOfExperience";

    public Driver(String name, LicenceType license, int yearsOfExperience) {
        this.name = name;
        this.license = license;
        this.yearsOfExperience = yearsOfExperience;
    }

    public boolean assignCar(Car car, boolean force) {
        if (car != null && car.getAssignedDriver() != null) {
            if (force) {
                assignedCar.getAssignedDriver().assignedCar = null;
                car.assignDriver(this);
            } else {
                return false;
            }
        }
        if (this.assignedCar != null) {
            this.assignedCar.assignDriver(null);
        }
        this.assignedCar = car;
        if (car != null) {
        car.assignDriver(this);
        }
        return true;
    }

    public Car getAssignedCar() {
        return assignedCar;
    }

    @Override
    public Map<String, String> toMap() {
        return Map.of(name_field, name, licenseType_field, "" + license, yearsOfExperience_field, "" + yearsOfExperience);
    }

    @Override
    public boolean fromMap(Map<String, String> definition) {
        String newName;
        LicenceType newLicense;
        int newYearsOfExperience;
        if (definition.containsKey(name_field)) {
            newName = definition.get(name_field);
        } else {
            return false;
        }
        if (definition.containsKey(licenseType_field)) {
            newLicense = LicenceType.valueOf(definition.get(licenseType_field));
        } else {
            return false;
        }
        if (definition.containsKey(yearsOfExperience_field)) {
            newYearsOfExperience = Integer.parseInt(definition.get(yearsOfExperience_field));
        } else {
            return false;
        }
        this.name = newName;
        this.license = newLicense;
        this.yearsOfExperience = newYearsOfExperience;
        return true;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name + " — license category: " + license + "; " + yearsOfExperience + " years of experience" + (assignedCar != null ? ". Drives " + assignedCar.getName() : "");
    }

    public LicenceType getLicense() {
        return license;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }
}

package CarGarage;

import Helpers.Menu;
import Helpers.MenuHelper;

import java.io.File;
import java.util.HashSet;

public class Garage implements Menu {
    private static Garage instance;
    private final HashSet<Engine> engines;
    private final HashSet<Car> cars;
    private final HashSet<Driver> drivers;

    private final String SAVE_FILE_NAME = "SaveData.txt";

    private Garage() {
        engines = new HashSet<>();
        cars = new HashSet<>();
        drivers = new HashSet<>();
    }

    public Engine findEngineByName(String name) {
        //TODO: implement findEngineByName
        return null;
    }

    public static Garage getInstance() {
        if (instance == null) {
            instance = new Garage();
        }
        return instance;
    }

    @Override
    public void menu() {
        MenuHelper.menuLoop("Select action:",
                new String[] {"Exit", "Show status", "Register new car", "Assign a car to a driver", "Start or stop motors", "Service history"},
                new Runnable[] {this::registerNewCar, this::assignCarToDriver, this::motorsMenu, this::serviceHistory},
                false);
    }

    private void serviceHistory() {

    }

    private void motorsMenu() {
    }

    private void assignCarToDriver() {
    }

    private void registerNewCar() {
    }

    public void initWithDefaults() {
        File f = new File(SAVE_FILE_NAME);
        if (f.exists()) {
            // load from file
        } else {
            // initialize default garage
            engines.add(new Engine("ICE35", "Combustion", 35, FuelType.E95));
            engines.add(new Engine("Truck300", "Combustion", 300, FuelType.E95));
            engines.add(new Engine("F1", "Combustion", 250, FuelType.E98));
            engines.add(new Engine("Electro100", "Electric", 100, FuelType.ELECTRIC));
        }
    }

    public HashSet<Engine> getEngines() {
        return engines;
    }

    public HashSet<Car> getCars() {
        return cars;
    }

    public HashSet<Driver> getDrivers() {
        return drivers;
    }
}

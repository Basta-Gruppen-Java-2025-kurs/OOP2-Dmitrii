package CarGarage;

import Helpers.Menu;
import Helpers.SafeInput;
import Helpers.SaveLoad;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.stream.Collectors;

import static Helpers.MenuHelper.*;

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
        return engines.stream().filter(en -> en.getName().equals(name)).findFirst().orElse(null);
    }

    public static Garage getInstance() {
        if (instance == null) {
            instance = new Garage();
        }
        return instance;
    }

    @Override
    public void menu() {
        menuLoop("Select action:",
                new String[] {"Exit", "Show status", "Register new car", "Assign a car to a driver", "Start or stop motors", "Service history"},
                new Runnable[] {this::showStatus, this::registerNewCar, this::assignCarToDriver, this::motorsMenu, this::serviceHistory},
                false);
    }

    private void showStatus() {
        System.out.println("Cars in the garage: " + cars.size());
        System.out.println("Drivers in the garage: " + drivers.size());
        System.out.println("Engines on: " + cars.stream().filter(Car::isOn).count());
        System.out.println("Available engines:");
        for (Engine engine : engines) {
            System.out.println("* " + engine);
        }
        // list all cars
        System.out.println("List of cars:");
        for (Car car : cars) {
            System.out.println("* " + car);
        }
        // list all drivers
        System.out.println("\nList of drivers:");
        for (Driver driver : drivers) {
            System.out.println("* " + driver);
        }
        // service history?
    }

    private void serviceHistory() {

    }

    private void motorsMenu() {
        menuLoop("Select an action:", new String[] {"Back", "List engines", "Add engine", "Remove engine"}, new Runnable[] {this::listEngines, this::addEngine, this::removeEngine}, false);
    }

    private long countCarsUsingEngine(Engine engine) {
        return cars.stream().filter(c -> c.getEngine().equals(engine)).count();
    }

    private void listEngines() {
        System.out.println("Engines used in the garage:");
        for(Engine engine : engines) {
            System.out.println("* " + engine + " (installed in " + countCarsUsingEngine(engine) + " cars)");
        }
    }

    private void addEngine() {
        Scanner sc = new Scanner(System.in);
        System.out.println("New engine name:");
        String newName = sc.nextLine().trim();
        System.out.println("Engine type:");
        String newType = sc.nextLine().trim();
        SafeInput si = new SafeInput(sc);
        double newHorsePower = si.nextDouble("Horse power:");
        listMenuLoop("Select fuel type:", "Cancel", "No fuel types found.", Arrays.asList(FuelType.values()),
                newFuelType -> engines.add(new Engine(newName, newType, newHorsePower, newFuelType)), true);
    }

    private void removeEngine() {
        ArrayList<Engine> removableEngines = engines.stream().filter(en -> (countCarsUsingEngine(en) < 1)).collect(Collectors.toCollection(ArrayList::new));
        listMenuLoop("Select engine to remove:", "Cancel", "No engines found.", removableEngines, engines::remove, true);
    }

    private void assignCarSubmenu(Driver driver, Car car) {
        if (driver.assignCar(car, false)) {
            System.out.println("Driver assigned to car.");
        } else {
            if (yesNoQuestion("Apparently, the car is already assigned. Reassign to this driver anyway?")) {
                System.out.println(driver.assignCar(car, true) ? "Car reassigned." : "Failed to reassign car.");
            }
        }
    }

    private void assignCarToDriver() {
        listMenuLoop("Select driver:", "Cancel", "No drivers found.", drivers.stream().toList(), dr -> {
            if (dr.getAssignedCar() != null) {
                if (!yesNoQuestion("This driver is already assigned to a car. Reassign to another car?")) {
                    return;
                }
            }
            listMenuLoop("Select car:", "Cancel", "No cars found.", cars.stream().toList(), car -> assignCarSubmenu(dr, car), true);
        }, true);
    }

    private void registerNewCar() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter new car's model (empty to cancel):");
        String newModel = sc.nextLine().trim();
        if (!newModel.isEmpty()) {
            listMenuLoop("Select engine:", "Cancel", "No engines found.", engines.stream().toList(), newEngine -> {
                System.out.println("Enter new car's license plate (empty to cancel):");
                String newLicensePlate = sc.nextLine().trim();
                if (!newLicensePlate.isEmpty()) {
                    cars.add(new Car(newModel, newEngine, newLicensePlate));
                }
            }, true);
        }
    }

    public void initWithDefaults() {
        File f = new File(SAVE_FILE_NAME);
        if (f.exists()) {
            // load from file
            try {
                SaveLoad loader = new SaveLoad(new Scanner(f));
                loader.registerClass("Engine", Engine.class);
                loader.registerClass("Car", Engine.class);
                loader.registerClass("Driver", Engine.class);
                loader.registerAction("Assign", definition -> {

                });
                loader.load();
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
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

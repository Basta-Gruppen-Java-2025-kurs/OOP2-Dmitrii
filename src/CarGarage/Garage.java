package CarGarage;

import Helpers.Menu;
import Helpers.SafeInput;
import Helpers.SaveLoad;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

import static Helpers.MenuHelper.*;

public class Garage implements Menu {
    private static Garage instance;
    private final HashSet<Engine> engines;
    private final HashSet<Car> cars;
    private final HashSet<Driver> drivers;
    private final ServiceHistory serviceHistory;
    private Date currentDate;

    private final String SAVE_FILE_NAME = "SaveData.txt";

    private Garage() {
        engines = new HashSet<>();
        cars = new HashSet<>();
        drivers = new HashSet<>();
        serviceHistory = new ServiceHistory();
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
        //TODO:
        // - change motor menu name
        // - add menu to start and stop motors - maybe inside car menu
        // - separate car menu (add, remove, list, paint, service, assign driver?)
        // - separate driver menu (list, hire, fire, assign car)
        // - maybe calendar? advance day, set date, set time
        // - add fuel menu (fuel prices, fuel usage between dates, filter by car, engine type, driver)
        // - save and load menu
        menuLoop("Select action:",
                new String[] {"Exit", "Show status", "Car menu", "Driver menu", "Engines menu", "Service history", "Save", "Load"},
                new Runnable[] {this::showStatus, this::carMenu, this::driversMenu, this::motorsMenu, serviceHistory::menu, this::save, this::load},
                false);
    }

    private void save() {

    }

    private void load() {

    }

    private void carMenu() {
        // list, new car, remove car, service car, car-specific menu
        menuLoop("Car menu. Select action:",
                new String[]{"Back", "List cars", "Add new car", "Remove car", "Select car"},
                new Runnable[] {this::listCars, this::registerNewCar, this::removeCar, () -> listMenuLoop("Select car:", "Back", "No cars found.", cars.stream().toList(), Car::menu, true)},
                false);
    }

    private void removeCar() {
        listMenuLoop("Select car to remove:", "Cancel", "No cars found.", cars.stream().toList(), c -> {
            if(c.getAssignedDriver() != null) {
                if (!yesNoQuestion("This car has an assigned driver. Remove anyway?")) {
                    return;
                }
                c.getAssignedDriver().assignCar(null, true);
            }
            System.out.println(cars.remove(c) ? "Car removed." : "Failed to remove car.");
        }, true);
    }

    private void listCars() {
        if (cars.isEmpty()) {
            System.out.println("No cars in the garage.");
            return;
        }
        System.out.println("List of cars:");
        for(Car car: cars) {
            System.out.println("* " + car);
        }
    }

    private void listDrivers() {
        if (drivers.isEmpty()) {
            System.out.println("No drivers in the garage.");
            return;
        }
        System.out.println("List of drivers:");
        for (Driver driver : drivers) {
            System.out.println("* " + driver);
        }
    }

    private void showStatus() {
        System.out.println("Cars in the garage: " + cars.size());
        System.out.println("Drivers in the garage: " + drivers.size());
        System.out.println("Engines on: " + cars.stream().filter(Car::isOn).count());
        listEngines();
        listCars();
        listDrivers();
        System.out.println(serviceHistory);
    }

    private void driversMenu() {
        menuLoop("Select an action:",
                new String[] {"Back", "List drivers", "Hire new driver", "Fire a driver", "Assign a driver to a car"},
                new Runnable[] {this::listDrivers, this::hireNewDriver, this::fireADriver, this::assignCarToDriver},
                false);
    }

    private void fireADriver() {
        listMenuLoop("Select a driver to fire", "Cancel", "No drivers found.", drivers.stream().toList(), dr -> {
            if (dr.getAssignedCar() != null) {
                if (yesNoQuestion("A car is assigned to this driver. Unassing and fire driver?")) {
                    dr.assignCar(null, true);
                }
            }
            System.out.println(drivers.remove(dr) ? "Driver fired." : "Failed to fire driver.");
            // if failed to fire a driver, they are still unassigned from a car
        }, true);
    }

    private void hireNewDriver() {
        SafeInput si = new SafeInput(new Scanner(System.in));
        String newDriverName = si.nextLine("New driver's name:");
        int newYearsOfExperience = si.nextInt("Years of experience:");
        listMenuLoop("Select driver license type:", "Cancel", "No license types found.",
                Arrays.asList(LicenceType.values()), newLicenseType ->
                drivers.add(new Driver(newDriverName, newLicenseType, newYearsOfExperience)), true);
    }

    private void motorsMenu() {
        menuLoop("Select an action:",
                new String[] {"Back", "List engines", "Add engine", "Remove engine"},
                new Runnable[] {this::listEngines, this::addEngine, this::removeEngine},
                false);
    }

    private long countCarsUsingEngine(Engine engine) {
        return cars.stream().filter(c -> c.getEngine().equals(engine)).count();
    }

    private void listEngines() {
        if (engines.isEmpty()) {
            System.out.println("No engines are available.");
            return;
        }
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
                    listMenuLoop("Select driver license requirement:", "Cancel", "No license types found.", Arrays.asList(LicenceType.values()),
                            newLicenseRequirement -> cars.add(new Car(newModel, newEngine, newLicensePlate,newLicenseRequirement)), true);
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
                loader.registerClass("Car", Car.class);
                loader.registerClass("Driver", Driver.class);
                loader.registerClass("Service", ServiceHistory.class);
                loader.registerAction("Assign", definition -> {
                    //TODO: define assigning drivers to cars
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

package CarGarage;

import Helpers.Mappable;
import Helpers.Menu;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class ServiceHistory implements Mappable, Menu {
    @Override
    public void menu() {
        //TODO: implement service menu
        System.out.println("Service menu");
        // log, add record, find cars that need service
    }

    public record ServiceRecord (String licensePlate, Date serviceDate) {}
    final ArrayList<ServiceRecord> history = new ArrayList<>();

    @Override
    public Map<String, String> toMap() {
        return Map.of();
    }

    @Override
    public boolean fromMap(Map<String, String> definition) {
        return false;
    }

    @Override
    public String toString() {
        return "";
    }
}

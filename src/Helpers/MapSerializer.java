package Helpers;

import java.util.Map;
import java.util.Scanner;

public class MapSerializer {
    private Scanner sc;

    MapSerializer(Scanner sc) {
        this.sc = sc;
    }

    public <T extends Mappable> boolean serialize(T object) {
        //TODO: implement serialize
        return false;
    }

    public <T extends Mappable> void deserialize(T object, Map<String, String> definition) {
        object.fromMap(definition);
    }
}

package Helpers;

import java.util.HashMap;
import java.util.Scanner;
import java.util.function.Consumer;

public class SaveLoad {
    private final Scanner sc;
    private final HashMap<String, Class<? extends Mappable>>classes = new HashMap<>();
    private final HashMap<String, Consumer<String>>actions = new HashMap<>();

    SaveLoad(Scanner sc) {
        this.sc = sc;
    }

    public void registerClass(String header, Class<? extends Mappable> type) {
        classes.put(header, type);
    }

    public void registerAction(String header, Consumer<String> action) {
        actions.put(header, action);
    }

    public void load() {
        if (sc == null) {
            throw new RuntimeException("No scanner found");
        }
        while(sc.hasNextLine()) {
            String nextLine = sc.nextLine();
        }
        //TODO: implement loading of a file
        // in a loop:
        // read next line
        // see if it's in classes or actions, otherwise signal error
        // if classes, read properties until next header and store in a map
        //  create new object of that class
        //  call fromMap on the object
        // if actions, read next line and call a registered action consumer with that line as parameter
    }

    public boolean saveObject(String type, Mappable object) {
        //TODO: implement saving
        return false;
    }

    public boolean loadObject(Mappable object) {
        //TODO: implement loading
        return false;
    }
}

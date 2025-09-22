package Helpers;

import java.util.Map;

public interface Mappable {
    Map<String, String> toMap();
    boolean fromMap(Map<String, String> definition);
}

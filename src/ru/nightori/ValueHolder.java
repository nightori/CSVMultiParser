package ru.nightori;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ValueHolder {
    private static final ConcurrentHashMap<String, Set<String>> values = new ConcurrentHashMap<>();

    public static Map<String, Set<String>> get() {
        return values;
    }

    public static void append(Map<String, Set<String>> map) {
        // go through the map that's being appended
        for (String key : map.keySet()) {
            // create an empty set if there's no such key in main map
            values.putIfAbsent(key, new HashSet<>());
            // combine added map's values with main map's values
            values.get(key).addAll(map.get(key));
        }
    }
}

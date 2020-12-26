package ru.nightori;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public abstract class ValueHolder {
    private static final ConcurrentHashMap<String, Set<String>> values = new ConcurrentHashMap<>();

    public static void append(Map<String, Set<String>> map) {
        // go through the map that's being appended
        for (String key : map.keySet()) {
            // create an empty set if there's no such key in main map
            values.putIfAbsent(key, new HashSet<>());
            // combine added map's values with main map's values
            values.get(key).addAll(map.get(key));
        }
    }

    public static void export(String path) {
        try {
            prepareDirectory(path);
            for (String header : values.keySet()) {
                try (PrintWriter writer = new PrintWriter(path + header)) {
                    // for each value associated with the header
                    for (String value : values.get(header)) {
                        writer.print(value + ";");
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // if exists - clear, if not - create
    public static void prepareDirectory(String path) throws IOException {
        Path dir = Path.of(path);
        if (Files.exists(dir)) {
            // recursively delete everything in the directory
            Files.walk(dir)
                    // don't touch the directory itself
                    .skip(1)
                    // process files before directories
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);
        }
        else {
            Files.createDirectory(dir);
        }
    }
}

package ru.nightori;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class FileReadTask implements Runnable {

    private final String filename;

    public FileReadTask(String filename) {
        this.filename = filename;
    }

    @Override
    public void run() {
        try (FileReader fileReader = new FileReader(filename)) {
            BufferedReader reader = new BufferedReader(fileReader);

            // get all headers by splitting the first line
            String[] headers = reader.readLine().split(";");

            // create a map with a set for each header
            Map<String, Set<String>> map = new HashMap<>();
            for (String header : headers) map.put(header, new HashSet<>());

            // read the file line by line
            while (reader.ready()) {
                String line = reader.readLine();
                if (line.isBlank()) continue;

                // get values by splitting the line
                String[] values = line.split(";");

                // put each value in the corresponding set
                for (int i = 0; i < values.length; i++) {
                    map.get(headers[i]).add(values[i]);
                }
            }

            // append the resulting map to the global map
            ValueHolder.append(map);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

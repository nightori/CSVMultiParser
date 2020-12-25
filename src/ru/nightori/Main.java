package ru.nightori;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws InterruptedException {
        // args must not be empty
        if (args.length == 0) {
            System.out.println("Please provide input files as command line arguments");
            return;
        }

        // determine pool size based on the number of cores
        int poolSize = Runtime.getRuntime().availableProcessors();

        // create a fixed pool thread
        ExecutorService pool = Executors.newFixedThreadPool(poolSize);

        // create and submit a FileReadTask for each input file
        for (String filename : args) {
            pool.submit(new FileReadTask(filename));
        }

        // close the pool and await shutdown
        pool.shutdown();
        if (pool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS)) {
            // when everything is done, get the result
            Map<String, Set<String>> map = ValueHolder.get();

            // print the result
            for (String key : map.keySet()) {
                System.out.println(key + ":");
                for (String value : map.get(key)) {
                    System.out.print(value + ";");
                }
                System.out.println();
            }
        }
    }
}

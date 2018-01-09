package main;

import task.Buyer;
import task.Storage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Starter {
    public static void main(String[] args) {
        if (parseArgs(args)) {
            int threadsNumber = Integer.parseInt(args[0]);
            ExecutorService exec = Executors.newFixedThreadPool(threadsNumber);
            CyclicBarrier barrier = new CyclicBarrier(threadsNumber);
            Storage storage = new Storage();
            List<Buyer> buyers = new ArrayList<>();
            for (int i = 0; i < threadsNumber; i++) {
                buyers.add(new Buyer("buyer" + (i + 1), storage, barrier));
            }

            List<Future<String>> results = null;
            try {
                results = exec.invokeAll(buyers);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            exec.shutdown();
            for (Future<String> result : results) {
                try {
                    System.out.println(result.get());
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
            int sum = 0;
            for (Buyer b : buyers) {
                sum += b.getBalance();
            }
            System.out.println("Total: " + sum);
        }
    }

    private static boolean parseArgs(String[] args) {
        if (args.length != 1) {
            printHelp();
            return false;
        }
        try {
            Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            printHelp();
            return false;
        }
        return true;
    }

    private static void printHelp() {
        System.out.println("Usage: <program name> <number of buyers (integer)>");
    }
}

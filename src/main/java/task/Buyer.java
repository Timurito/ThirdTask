package task;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;

public class Buyer implements Callable<String> {
    private Storage storage;
    private String name;
    private CyclicBarrier barrier;
    private int balance;
    private int numberOfPurchases;

    public Buyer(String name, Storage storage, CyclicBarrier barrier) {
        this.name = name;
        this.storage = storage;
        this.barrier = barrier;
    }

    public int getBalance() {
        return balance;
    }

    public int getNumberOfPurchases() {
        return numberOfPurchases;
    }

    public String getName() {
        return name;
    }

    @Override
    public String call() {
        while (true) {
            int curPurchase;
            synchronized (storage) {
                curPurchase = storage.takeFromStock((int) (Math.random() * 10 + 1));
            }
            if (curPurchase != 0) {
                balance += curPurchase;
                numberOfPurchases++;
                try {
                    barrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
//                    e.printStackTrace();
                    // there will be BrokenBarrierException, but we don't need to handle it, I think
                }
            } else {
                barrier.reset();
                break;
            }
        }
        return getName() + " " + getNumberOfPurchases() + " " + getBalance();
    }
}

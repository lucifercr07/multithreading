package org.example.ThreadingEssentials.SynchronizedExample;

import java.util.Arrays;

public class CounterClient {
    public static void main(String[] args) {
        Counter counter = new Counter(100_000_000);
        Thread t1 = new Thread(() -> {
            while (true) {
                getCount(counter);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }, "1st Thread");
        Thread t2 = new Thread(() -> getCount(counter), "2nd Thread");
        t1.start();
        t2.start();
        System.out.println("Main thread finished!!");
    }

    private static void getCount(Counter counter) {
        if (counter.getCount() < Integer.MAX_VALUE) {
            System.out.println(counter.incrementAndGet());
        } else {
            System.err.println("Counter overflow " + Thread.currentThread().getName());
        }
    }
}

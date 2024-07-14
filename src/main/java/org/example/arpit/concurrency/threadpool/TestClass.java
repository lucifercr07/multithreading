package org.example.arpit.concurrency.threadpool;

import static java.lang.Thread.sleep;

public class TestClass {
    public static void main(String[] args) {
        ThreadPool th = new CustomThreadPool(10);
        for (int i = 0; i < 10; i++) {
            th.submit(() -> {
                try {
                    sleep(10000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                System.out.println("Hello world!! from " + Thread.currentThread().getName());
            });
        }

        th.shutdown();
        // Thread shouldn't accept any job after shutdown already executed
        for (int i = 0; i < 10; i++) {
            th.submit(() -> {
                try {
                    sleep(10000L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                System.out.println("Hello world!! from " + Thread.currentThread().getName());
            });
        }
        th.shutdownNow();
    }
}

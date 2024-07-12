package org.example.arpit.concurrency;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class CountPrimeTill100Million {
    private static final int PRIME_NUM_END_COUNT = 100_000_000;
    private static final int THREAD_NUMS = 10;
    private static AtomicInteger primeCounter = new AtomicInteger(0);

    public static void main(String[] args) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        System.out.println("Number of primes till 100 million: " + singleThreadedCounting(1, PRIME_NUM_END_COUNT));
//        multiThreadedCounting();
        long endTime = System.currentTimeMillis();
        System.out.println("Number of primes till 100 million: " + primeCounter);
        System.out.println("Time taken for run: " + (endTime - startTime) / 1000 + " seconds.");
    }

    private static void multiThreadedCounting() throws InterruptedException {
        ExecutorService ex = Executors.newFixedThreadPool(THREAD_NUMS);
        int low = 0;
        int high = 10_000_000;
        while (high <= PRIME_NUM_END_COUNT + 1) {
            final int start = low;
            final int end = high;
            ex.submit(() -> {
               primeCounter.addAndGet(singleThreadedCounting(start, end));
            });
            low = high;
            high = high + high;
            // System.out.println("Job submitted for range start: " + start + " end: " + end);
            String s = "";
            var x = s.intern();
        }

        ex.shutdown();
        if (ex.awaitTermination(1000L, TimeUnit.SECONDS)) {
            ex.shutdownNow();
        }
    }

    private static int singleThreadedCounting(final int start, final int end) {
        System.out.println("I am thread: " + Thread.currentThread().getName() + " executing for range: " + start + " " + end);
        int count = 0;
        for (int i = start; i <= end; ++i) {
            if (isPrime(i))
                count++;
        }

        return count;
    }

    private static boolean isPrime(final int num) {
        // Check if num is less than 2
        if (num < 2) {
            return false;
        }

        // Check from 2 to sqrt(num)
        for (int i = 2; i <= Math.sqrt(num); i++) {
            if (num % i == 0) {
                return false;
            }
        }

        return true;
    }
}

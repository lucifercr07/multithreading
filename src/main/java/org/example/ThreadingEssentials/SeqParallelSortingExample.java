package org.example.ThreadingEssentials;

import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

public class SeqParallelSortingExample {
    public static void main(String[] args) {
        int[] numbers = ThreadLocalRandom.current().ints(100_000_000).toArray();
        int[] numbersSeq = numbers.clone();
        int[] numbersPar = numbers.clone();

        // testSorting(numbersSeq, numbersPar);
        Thread sequentialSortingThread = new Thread(() -> sortNumbers("seq", numbersSeq, Arrays::sort));
        Thread parallelSortingThread = new Thread(() -> sortNumbers("par", numbersPar, Arrays::parallelSort));

        sequentialSortingThread.start();
        parallelSortingThread.start();
        System.out.println("Main thread finished.");
    }

    private static void testSorting(int[] numbersSeq, int[] numbersPar) {
        sortNumbers("seq", numbersSeq, Arrays::sort);
        sortNumbers("par", numbersPar, Arrays::parallelSort);
    }

    private static void sortNumbers(String sortType, int[] numbers, Consumer<int[]> sorter) {
        long startTime = System.currentTimeMillis();
        sorter.accept(numbers);
        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;
        System.out.println(sortType + ": elapsed time: " + elapsedTime + " ms");
    }
}

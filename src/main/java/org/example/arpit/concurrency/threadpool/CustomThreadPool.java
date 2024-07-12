package org.example.arpit.concurrency.threadpool;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class CustomThreadPool implements ThreadPool {

    private Deque<Runnable> tasks = new ArrayDeque<>();
    private List<Worker> workers;

    public CustomThreadPool(int size) {
        workers = new ArrayList<>(size);
    }

    @Override
    public void submit(Runnable task) {
    }

    @Override
    public void shutdown() {
    }

    @Override
    public void shutdownNow() {
    }

    private Runnable take() {
        return tasks.peekFirst();
    }

    private class Worker extends Thread {
        Worker(String name) {
            super(name);
        }

        public void run() {
            while (true) {
                var task = take();
                if (task != null) {
                    task.run();
                }
            }
        }
    }
}

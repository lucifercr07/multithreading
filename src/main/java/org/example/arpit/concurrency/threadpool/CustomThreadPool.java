package org.example.arpit.concurrency.threadpool;

import java.util.ArrayDeque;
import java.util.Deque;

public class CustomThreadPool implements ThreadPool {

    private final Deque<Runnable> tasks = new ArrayDeque<>();
    private final Runnable POISON_PILL = () -> {};

    public CustomThreadPool(int size) {
        if (size < 1) {
            throw new IllegalArgumentException("No thread pool size < 1");
        }

        for (int i = 0; i < size; i++) {
            new Worker("Thread " + i).start();
        }
    }

    @Override
    public void submit(Runnable task) {
        java.util.Objects.requireNonNull(task, "task==null");
        synchronized (tasks) {
            if (tasks.peekLast() == POISON_PILL) {
                System.err.println("ThreadPool already shutting down!!");
                return;
            }

            tasks.add(task);
            tasks.notifyAll();
        }
    }

    @Override
    public void shutdown() {
        synchronized (tasks) {
            submit(POISON_PILL);
        }
    }

    @Override
    public void shutdownNow() {
        synchronized (tasks) {
            System.out.println("Killing thread pool with " + this.currentTasksPending() + " tasks pending.");
            tasks.addFirst(POISON_PILL);
            tasks.notifyAll();
        }
    }

    private long currentTasksPending() {
        return tasks.stream().filter(t -> !t.equals(POISON_PILL)).count();
    }

    private Runnable take() throws InterruptedException {
        synchronized (tasks) {
            if (tasks.isEmpty()) {
                tasks.wait();
            }

            var task = tasks.removeFirst();
            // If task is already POISON_PILL i.e. queue needs to shutdown
            // so keep this in task queue so all threads can consume and die!!!
            if (task.equals(POISON_PILL)) {
                tasks.add(POISON_PILL);
                return POISON_PILL;
            } else {
                return task;
            }
        }
    }

    private class Worker extends Thread {
        Worker(String name) {
            super(name);
        }

        public void run() {
            while (true) {
                try {
                    var task = take();
                    if (POISON_PILL.equals(task)) {
                        System.out.println("Exiting thread " + Thread.currentThread().getName());
                        return;
                    }

                    task.run();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}

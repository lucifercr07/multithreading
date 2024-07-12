package org.example.arpit.concurrency.threadpool;

public interface ThreadPool {
    void submit(Runnable task);
    void shutdown();
    void shutdownNow();
}

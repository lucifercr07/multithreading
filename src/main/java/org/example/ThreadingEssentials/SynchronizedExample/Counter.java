package org.example.ThreadingEssentials.SynchronizedExample;

public class Counter {
    private int count;

    public Counter(int count) {
        if (count == Integer.MAX_VALUE) throw new IllegalArgumentException();
        this.count = count;
    }

    public synchronized int getAndIncrement() {
        return count++;
    }

    public synchronized int incrementAndGet() {
        return ++count;
    }

    public synchronized int getCount() {
        return count;
    }
}

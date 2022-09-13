package com.example.server.ConcurrencyTest;

public class SynchronizedCounter {
    private int c = 0;

    public synchronized void increment() {
        c++;
    }

    public synchronized void decrement() {
        c--;
    }

    public synchronized int value() {
        return c;
    }

    public synchronized void reset() {
        c = 0;
    }

}

package com.example.server.businessLayer.Market.ResourcesObjects;

public class SynchronizedCounter {
    private int c = 1;

    public synchronized int increment() {
        c++;
        return (c - 1);
    }

    public synchronized void decrement() {
        c--;
    }

    public synchronized int value() {
        return c;
    }

    public synchronized void reset() {
        c = 1;
    }

    public synchronized void setValue(int value)
    {
        c = value;
    }
}

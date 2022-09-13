package com.example.server.ConcurrencyTest;

public class SynchronizedBoolean {
    private boolean b = true;

    public synchronized boolean value() {
        return b;
    }

    public synchronized void makeTrue() {
        b = true;
    }

    public synchronized void makeFalse() {
        b = false;
    }
}

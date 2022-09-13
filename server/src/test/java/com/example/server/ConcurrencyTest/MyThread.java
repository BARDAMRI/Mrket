package com.example.server.ConcurrencyTest;

import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;

public class MyThread extends Thread {
    MyRunnable runnable;
    MarketException ex;

    public void setRunnable(MyRunnable runnable) {
        this.runnable = runnable;
    }

    public MarketException getEx() {
        return ex;
    }

    @Override
    public void run(){
        try {
            runnable.run ();
        } catch (MarketException e) {
            ex = e;
        }
    }
}

package com.example.server.serviceLayer.Notifications;

public abstract class Notification {

    protected String message ;

    public Notification(String mess) {
        this.message=mess;
    }

    public Notification() {
        this.message="";
    }
    public String getMessage(){return message;}
}

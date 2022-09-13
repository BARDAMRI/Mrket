package com.example.server.businessLayer.Publisher;

import com.example.server.serviceLayer.Notifications.Notification;

import java.util.List;

public abstract class Publisher {


    public abstract boolean add( String sessionId);
    public abstract List<Notification> remove(String sessionId);
    public abstract boolean addMessgae(String sessionId, Notification notification);
}

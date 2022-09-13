package com.example.server.serviceLayer.Requests;

public class RequestVisitorName {
    private String name;

    public RequestVisitorName(){};
    public RequestVisitorName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

package com.example.server.serviceLayer.Requests;

public class IsSystemManagerRequest {
    private String name;

    public  IsSystemManagerRequest(){}

    public IsSystemManagerRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

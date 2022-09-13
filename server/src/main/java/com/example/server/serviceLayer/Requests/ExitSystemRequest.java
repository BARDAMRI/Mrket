package com.example.server.serviceLayer.Requests;

public class ExitSystemRequest {
    private String visitorName;

    public  ExitSystemRequest(){};
    public ExitSystemRequest(String visitorName) {
        this.visitorName = visitorName;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }
}

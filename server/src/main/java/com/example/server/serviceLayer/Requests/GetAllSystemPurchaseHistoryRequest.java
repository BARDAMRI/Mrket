package com.example.server.serviceLayer.Requests;

public class GetAllSystemPurchaseHistoryRequest {
    private String systemManagerName;

    public GetAllSystemPurchaseHistoryRequest(String systemManagerName) {
        this.systemManagerName = systemManagerName;
    }
    public GetAllSystemPurchaseHistoryRequest(){}


    public String getSystemManagerName() {
        return systemManagerName;
    }

    public void setSystemManagerName(String systemManagerName) {
        this.systemManagerName = systemManagerName;
    }
}

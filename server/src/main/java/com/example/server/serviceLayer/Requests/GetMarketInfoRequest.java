package com.example.server.serviceLayer.Requests;

public class GetMarketInfoRequest {
    String sysManager;
    public GetMarketInfoRequest(){};
    public GetMarketInfoRequest(String sysManager) {
        this.sysManager = sysManager;
    }

    public String getSysManager() {
        return sysManager;
    }

    public void setSysManager(String sysManager) {
        this.sysManager = sysManager;
    }

}

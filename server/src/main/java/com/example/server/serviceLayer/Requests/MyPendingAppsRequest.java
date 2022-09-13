package com.example.server.serviceLayer.Requests;

public class MyPendingAppsRequest {
    private String shopName;
    private String ownerName;

    public MyPendingAppsRequest(){}

    public MyPendingAppsRequest(String shopName, String ownerName) {
        this.shopName = shopName;
        this.ownerName = ownerName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}

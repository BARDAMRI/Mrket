package com.example.server.serviceLayer.Requests;

public class TwoStringRequest {
    private String name;
    private String shopName;

    public TwoStringRequest() {
    }

    public TwoStringRequest(String first, String second) {
        this.name = first;
        this.shopName = second;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}

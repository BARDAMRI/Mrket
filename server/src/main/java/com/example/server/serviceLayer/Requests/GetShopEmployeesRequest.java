package com.example.server.serviceLayer.Requests;

public class GetShopEmployeesRequest {

    private String shopManagerName;
    private String shopName;

    public GetShopEmployeesRequest() {
    }

    public GetShopEmployeesRequest(String shopManagerName, String shopName) {
        this.shopManagerName = shopManagerName;
        this.shopName = shopName;
    }

    public String getShopManagerName() {
        return shopManagerName;
    }

    public void setShopManagerName(String shopManagerName) {
        this.shopManagerName = shopManagerName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}

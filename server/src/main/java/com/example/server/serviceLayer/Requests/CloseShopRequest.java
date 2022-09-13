package com.example.server.serviceLayer.Requests;

public class CloseShopRequest {

    private String shopOwnerName;
    private String shopName;

    public CloseShopRequest() {
    }

    public CloseShopRequest(String shopOwnerName, String shopName) {
        this.shopOwnerName = shopOwnerName;
        this.shopName = shopName;
    }

    public String getShopOwnerName() {
        return shopOwnerName;
    }

    public void setShopOwnerName(String shopOwnerName) {
        this.shopOwnerName = shopOwnerName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}

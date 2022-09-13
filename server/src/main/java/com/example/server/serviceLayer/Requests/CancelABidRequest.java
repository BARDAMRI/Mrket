package com.example.server.serviceLayer.Requests;

public class CancelABidRequest {
    String buyer;
    String shopName;
    int itemId;

    public CancelABidRequest(String buyer, String shopName, int itemId) {
        this.buyer = buyer;
        this.shopName = shopName;
        this.itemId = itemId;
    }

    public CancelABidRequest() {
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
}

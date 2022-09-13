package com.example.server.serviceLayer.Requests;

public class RejectABidRequest {
    String opposed;
    String buyer;
    String shopName;
    int itemId;

    public RejectABidRequest(String opposed, String buyer, String shopName, int itemId) {
        this.opposed = opposed;
        this.buyer = buyer;
        this.shopName = shopName;
        this.itemId = itemId;
    }

    public RejectABidRequest() {
    }

    public String getOpposed() {
        return opposed;
    }

    public void setOpposed(String opposed) {
        this.opposed = opposed;
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

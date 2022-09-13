package com.example.server.serviceLayer.Requests;

public class ApproveABidRequest {
    String approves;
    String shopName;
    String askedBy;
    Integer itemId;

    public ApproveABidRequest(String approves, String shopName, String askedBy, Integer itemId) {
        this.approves = approves;
        this.shopName = shopName;
        this.askedBy = askedBy;
        this.itemId = itemId;
    }

    public ApproveABidRequest() {
    }

    public String getApproves() {
        return approves;
    }

    public void setApproves(String approves) {
        this.approves = approves;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getAskedBy() {
        return askedBy;
    }

    public void setAskedBy(String askedBy) {
        this.askedBy = askedBy;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }
}

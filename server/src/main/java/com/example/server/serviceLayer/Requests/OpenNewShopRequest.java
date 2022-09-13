package com.example.server.serviceLayer.Requests;

public class OpenNewShopRequest {

    private String memberName;
    private String shopName;

    public OpenNewShopRequest(String visitorName, String shopName) {
        this.memberName = visitorName;
        this.shopName = shopName;
    }

    public OpenNewShopRequest() {
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}

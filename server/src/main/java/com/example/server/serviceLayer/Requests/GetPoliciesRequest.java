package com.example.server.serviceLayer.Requests;

public class GetPoliciesRequest {
    String visitorName;
    String shopName;

    public GetPoliciesRequest(String visitorName, String shopName) {
        this.visitorName = visitorName;
        this.shopName = shopName;
    }

    public GetPoliciesRequest(){}

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}

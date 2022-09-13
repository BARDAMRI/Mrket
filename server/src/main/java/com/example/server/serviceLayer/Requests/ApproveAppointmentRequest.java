package com.example.server.serviceLayer.Requests;

public class ApproveAppointmentRequest {
    private String shopName;
    private String appointedName;
    private String ownerName;

    public ApproveAppointmentRequest(){}

    public ApproveAppointmentRequest(String shopName, String appointedName, String ownerName) {
        this.shopName = shopName;
        this.appointedName = appointedName;
        this.ownerName = ownerName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getAppointedName() {
        return appointedName;
    }

    public void setAppointedName(String appointedName) {
        this.appointedName = appointedName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }
}

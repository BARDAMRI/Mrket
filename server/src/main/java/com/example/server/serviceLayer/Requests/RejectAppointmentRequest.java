package com.example.server.serviceLayer.Requests;

public class RejectAppointmentRequest {
    private String shopOwnerName;
    private String appointedShopManager;
    private String shopName;
    public RejectAppointmentRequest() {
    }

    public RejectAppointmentRequest(String shopOwnerName, String appointedShopManager, String shopName) {
        this.shopOwnerName = shopOwnerName;
        this.appointedShopManager = appointedShopManager;
        this.shopName = shopName;
    }

    public String getShopOwnerName() {
        return shopOwnerName;
    }

    public void setShopOwnerName(String shopOwnerName) {
        this.shopOwnerName = shopOwnerName;
    }

    public String getAppointedShopManager() {
        return appointedShopManager;
    }

    public void setAppointedShopManager(String appointedShopManager) {
        this.appointedShopManager = appointedShopManager;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}

package com.example.server.serviceLayer.Requests;

public class AppointmentShopOwnerRequest {

    private String shopOwnerName;
    private String appointedShopOwner;
    private String shopName;


    public AppointmentShopOwnerRequest(String shopOwnerName, String appointedShopOwner, String shopName) {
        this.shopOwnerName = shopOwnerName;
        this.appointedShopOwner = appointedShopOwner;
        this.shopName = shopName;
    }

    public AppointmentShopOwnerRequest() {
    }

    public String getShopOwnerName() {
        return shopOwnerName;
    }

    public void setShopOwnerName(String shopOwnerName) {
        this.shopOwnerName = shopOwnerName;
    }

    public String getAppointedShopOwner() {
        return appointedShopOwner;
    }

    public void setAppointedShopOwner(String appointedShopOwner) {
        this.appointedShopOwner = appointedShopOwner;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}

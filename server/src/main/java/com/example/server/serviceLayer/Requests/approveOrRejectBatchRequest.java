package com.example.server.serviceLayer.Requests;

import java.util.List;

public class approveOrRejectBatchRequest {
    private String shopName;
    private String ownerName;
    private List<String> appointedNames;
    private boolean approve;

    public approveOrRejectBatchRequest(String shopName, String ownerName, List<String> appointedNames, boolean approve) {
        this.shopName = shopName;
        this.ownerName = ownerName;
        this.appointedNames = appointedNames;
        this.approve = approve;
    }
    public approveOrRejectBatchRequest(){}

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public List<String> getAppointedNames() {
        return appointedNames;
    }

    public void setAppointedNames(List<String> appointedNames) {
        this.appointedNames = appointedNames;
    }

    public boolean isApprove() {
        return approve;
    }

    public void setApprove(boolean approve) {
        this.approve = approve;
    }
}

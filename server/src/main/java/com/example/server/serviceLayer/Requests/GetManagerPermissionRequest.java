package com.example.server.serviceLayer.Requests;

public class GetManagerPermissionRequest {

    private String shopOwnerName;
    private String managerName;
    private String relatedShop;

    public GetManagerPermissionRequest() {
    }

    public GetManagerPermissionRequest(String shopOwnerName, String managerName, String relatedShop) {
        this.shopOwnerName = shopOwnerName;
        this.managerName = managerName;
        this.relatedShop = relatedShop;
    }

    public String getShopOwnerName() {
        return shopOwnerName;
    }

    public void setShopOwnerName(String shopOwnerName) {
        this.shopOwnerName = shopOwnerName;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getRelatedShop() {
        return relatedShop;
    }

    public void setRelatedShop(String relatedShop) {
        this.relatedShop = relatedShop;
    }
}

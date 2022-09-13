package com.example.server.serviceLayer.Requests;

import com.example.server.serviceLayer.FacadeObjects.ItemFacade;

public class ChangeShopItemInfoRequest {

    private String shopOwnerName;
    private String updatedInfo;
    private ItemFacade oldItem;
    private String shopName;

    public ChangeShopItemInfoRequest() {
    }

    public ChangeShopItemInfoRequest(String shopOwnerName, String updatedInfo, ItemFacade oldItem, String shopName) {
        this.shopOwnerName = shopOwnerName;
        this.updatedInfo = updatedInfo;
        this.oldItem = oldItem;
        this.shopName = shopName;
    }

    public String getShopOwnerName() {
        return shopOwnerName;
    }

    public void setShopOwnerName(String shopOwnerName) {
        this.shopOwnerName = shopOwnerName;
    }

    public String getUpdatedInfo() {
        return updatedInfo;
    }

    public void setUpdatedInfo(String updatedInfo) {
        this.updatedInfo = updatedInfo;
    }

    public ItemFacade getOldItem() {
        return oldItem;
    }

    public void setOldItem(ItemFacade oldItem) {
        this.oldItem = oldItem;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}

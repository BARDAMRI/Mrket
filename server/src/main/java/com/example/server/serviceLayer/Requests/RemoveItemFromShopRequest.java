package com.example.server.serviceLayer.Requests;

import com.example.server.serviceLayer.FacadeObjects.ItemFacade;

public class RemoveItemFromShopRequest {
    private String shopOwnerName;
    private ItemFacade item;
    private String shopName;

    public RemoveItemFromShopRequest(String shopOwnerName, ItemFacade item, String shopName) {
        this.shopOwnerName = shopOwnerName;
        this.item = item;
        this.shopName = shopName;
    }

    public RemoveItemFromShopRequest() {
    }

    public String getShopOwnerName() {
        return shopOwnerName;
    }

    public void setShopOwnerName(String shopOwnerName) {
        this.shopOwnerName = shopOwnerName;
    }

    public ItemFacade getItem() {
        return item;
    }

    public void setItem(ItemFacade item) {
        this.item = item;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}

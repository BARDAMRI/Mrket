package com.example.server.serviceLayer.Requests;

import com.example.server.serviceLayer.FacadeObjects.ItemFacade;

public class EditItemFromShoppingCartRequest {

    private Double amount;
    private ItemFacade itemFacade;
    private String shopName;
    private String visitorName;

    public EditItemFromShoppingCartRequest() {
    }

    public EditItemFromShoppingCartRequest(double amount, ItemFacade itemFacade, String shopName, String visitorName) {
        this.amount = amount;
        this.itemFacade = itemFacade;
        this.shopName = shopName;
        this.visitorName = visitorName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public ItemFacade getItemFacade() {
        return itemFacade;
    }

    public void setItemFacade(ItemFacade itemFacade) {
        this.itemFacade = itemFacade;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }
}

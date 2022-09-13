package com.example.server.serviceLayer.FacadeObjects;

import com.example.server.businessLayer.Market.Bid;

import java.util.HashMap;

public class BidFacade {
    private String buyerName;
    private Integer itemId;
    private double amount;
    private double price;
    private boolean approved;
    private HashMap<String, Boolean> shopOwnersStatus;

    public BidFacade(String buyerName, Integer itemId, double amount, double price, boolean approved, HashMap<String, Boolean> shopOwnersStatus) {
        this.buyerName = buyerName;
        this.itemId = itemId;
        this.amount = amount;
        this.price = price;
        this.approved = approved;
        this.shopOwnersStatus = shopOwnersStatus;
    }

    public BidFacade() {
    }

    public BidFacade(Bid bid) {
        buyerName = bid.getBuyerName ();
        itemId = bid.getItemId ();
        amount = bid.getAmount ();
        price = bid.getPrice ();
        approved = bid.getApproved();
        shopOwnersStatus = bid.getShopOwnersStatus ();
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public HashMap<String, Boolean> getShopOwnersStatus() {
        return shopOwnersStatus;
    }

    public void setShopOwnersStatus(HashMap<String, Boolean> shopOwnersStatus) {
        this.shopOwnersStatus = shopOwnersStatus;
    }
}

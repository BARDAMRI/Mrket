package com.example.server.serviceLayer.Requests;

public class SuggestNewOfferToBidRequest {
    private String shopName;
    private String suggester;
    private String askedBy;
    private int itemId;
    private double newPrice;

    public SuggestNewOfferToBidRequest(String shopName, String suggester, String askedBy, int itemId, double newPrice) {
        this.shopName = shopName;
        this.suggester = suggester;
        this.askedBy = askedBy;
        this.itemId = itemId;
        this.newPrice = newPrice;
    }

    public SuggestNewOfferToBidRequest() {
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getSuggester() {
        return suggester;
    }

    public String getAskedBy() {
        return askedBy;
    }

    public void setAskedBy(String askedBy) {
        this.askedBy = askedBy;
    }

    public void setSuggester(String suggester) {
        this.suggester = suggester;
    }


    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public double getNewPrice() {
        return newPrice;
    }

    public void setNewPrice(double newPrice) {
        this.newPrice = newPrice;
    }
}

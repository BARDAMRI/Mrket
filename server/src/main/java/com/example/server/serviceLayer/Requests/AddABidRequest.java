package com.example.server.serviceLayer.Requests;

import com.example.server.businessLayer.Market.Bid;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.DiscountPolicy;

public class AddABidRequest {
    private String visitorName;
    private String shopName;
    private Integer itemId;
    private Double price;
    private Double amount;

    public AddABidRequest(String visitorName, String shopName, Integer itemId, Double price, Double amount) {
        this.visitorName = visitorName;
        this.shopName = shopName;
        this.itemId = itemId;
        this.price = price;
        this.amount = amount;
    }

    public AddABidRequest() {
    }
    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}

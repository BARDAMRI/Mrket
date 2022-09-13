package com.example.server.serviceLayer.Requests;

import com.example.server.businessLayer.Market.Item;

import java.util.List;

public class AddItemToShopRequest {

    private String shopOwnerName;
    private String name;
    private double price;
    private Item.Category category;
    private String info;
    private List<String> keywords;
    private double amount;
    private String shopName;

    public AddItemToShopRequest() {
    }

    public AddItemToShopRequest(String shopOwnerName, String name, double price, Item.Category category, String info, List<String> keywords, double amount, String shopName) {
        this.shopOwnerName = shopOwnerName;
        this.name = name;
        this.price = price;
        this.category = category;
        this.info = info;
        this.keywords = keywords;
        this.amount = amount;
        this.shopName = shopName;
    }

    public String getShopOwnerName() {
        return shopOwnerName;
    }

    public void setShopOwnerName(String shopOwnerName) {
        this.shopOwnerName = shopOwnerName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Item.Category getCategory() {
        return category;
    }

    public void setCategory(Item.Category category) {
        this.category = category;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }
}

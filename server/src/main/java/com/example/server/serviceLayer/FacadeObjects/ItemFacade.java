package com.example.server.serviceLayer.FacadeObjects;

import com.example.server.businessLayer.Market.Item;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;

import java.util.List;

public class ItemFacade implements FacadeObject<Item>{
    private String info;
    private java.lang.Integer id;
    private String name;
    private Double price;
    private Item.Category category;

    private List<String> keywords;

    private int rank;

    private int rankers;

    public ItemFacade(){}

    public ItemFacade(java.lang.Integer ID, String name, double price,
                      Item.Category category, List<String> keywords,
                      String info) {
        this.id = ID;
        this.name = name;
        this.price = price;
        this.category = category;
        this.keywords = keywords;
        this.info = info;
        rank=1;
        rankers=0;

    }

    public ItemFacade(Item item) {
        this.id = item.getID();
        this.name = item.getName();
        this.price = item.getPrice();
        this.category = item.getCategory();
        this.keywords = item.getKeywords();
        this.info = item.getInfo();
        rank= item.getRank();
        rankers= item.getRankers();

    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setId(java.lang.Integer id) {
        this.id = id;
    }

    public java.lang.Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public Item.Category getCategory() {
        return category;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCategory(Item.Category category) {
        this.category = category;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public Item toBusinessObject() throws MarketException {
        Item item = new Item(this.id,this.name,this.price,this.info,this.category,this.keywords);
        return item;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getRankers() {
        return rankers;
    }

    public void setRankers(int rankers) {
        this.rankers = rankers;
    }
}
package com.example.server.serviceLayer.FacadeObjects;

import com.example.server.businessLayer.Market.Bid;
import com.example.server.businessLayer.Market.Item;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.businessLayer.Market.ShoppingBasket;

import java.util.HashMap;
import java.util.Map;

public class ShoppingBasketFacade implements FacadeObject<ShoppingBasket> {
    public Map<java.lang.Integer, ItemFacade> getItemMap() {
        return itemMap;
    }

    public void setItemMap(Map<java.lang.Integer, ItemFacade> itemMap) {
        this.itemMap = itemMap;
    }

    Map<java.lang.Integer,Double> items;//<Item,quantity>
    Map<java.lang.Integer,ItemFacade> itemMap;
    private HashMap<Integer, BidFacade> bids;

    double price;

    public ShoppingBasketFacade(Map<Integer, Double> items, Map<Integer, ItemFacade> itemMap, HashMap<Integer, BidFacade> bids, double price) {
        this.items = items;
        this.itemMap = itemMap;
        this.bids = bids;
        this.price = price;
    }

    public ShoppingBasketFacade(ShoppingBasket shoppingBasket) {
        items = new HashMap<>();
        for (Map.Entry<java.lang.Integer,Double> item: shoppingBasket.getItems().entrySet()){
            items.put(item.getKey(), item.getValue());
        }
        itemMap = new HashMap<>();
        for(Map.Entry<java.lang.Integer, Item> item : shoppingBasket.getItemMap().entrySet()){
            itemMap.put(item.getKey(), new ItemFacade((item.getValue())));
        }
        bids = new HashMap<>();
        for(Map.Entry<Integer, Bid> bid : shoppingBasket.getBids().entrySet()){
            bids.put(bid.getKey(), new BidFacade((bid.getValue())));
        }
    }

    public Map<java.lang.Integer, Double> getItems() {
        return items;
    }

    public void setItems(Map<java.lang.Integer, Double> items) {
        this.items = items;
    }


    public HashMap<Integer, BidFacade> getBids() {
        return bids;
    }

    public void setBids(HashMap<Integer, BidFacade> bids) {
        this.bids = bids;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public ShoppingBasket toBusinessObject() throws MarketException {
        Map<java.lang.Integer,ItemFacade> facadeItems = this.itemMap;
        Map<java.lang.Integer, Item> BLItems = new HashMap<>();
        for (Map.Entry<java.lang.Integer, ItemFacade> entry: facadeItems.entrySet()){
            BLItems.put(entry.getKey(),entry.getValue().toBusinessObject());
        }
        ShoppingBasket basket = new ShoppingBasket(items, BLItems, this.price);
        return basket;

    }
}

package com.example.server.businessLayer.Market;

import com.example.server.businessLayer.Market.ResourcesObjects.DebugLog;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ShoppingBasket implements IHistory {
    private Map<java.lang.Integer, Double> items;//<Item,quantity>
    private Map<java.lang.Integer, Item> itemMap;
    private double price;

    private HashMap<Integer, Bid> bids;

    public ShoppingBasket() {
        items = new ConcurrentHashMap<>();
        itemMap = new ConcurrentHashMap<>();
        price = 0;
        bids = new LinkedHashMap<> (  );
    }
    public ShoppingBasket(Map<java.lang.Integer,Double> items , Map<java.lang.Integer, Item> itemMap , double price){
        this.items = items;
        this.itemMap = itemMap;
        this.price = price;
        bids = new LinkedHashMap<> (  );
    }

    @Override
    public boolean equals(Object object){
        if(object instanceof ShoppingBasket){
            ShoppingBasket shoppingBasketToCompare = (ShoppingBasket) object;
            if(shoppingBasketToCompare.price != this.price)
                return false;
            for( Map.Entry<Integer, Bid> bid: this.bids.entrySet ()){
                if(shoppingBasketToCompare.bids.get ( bid.getKey () ) == null)
                    return false;
            }
            for( Map.Entry<Integer, Bid> bid: shoppingBasketToCompare.bids.entrySet ()){
                if(this.bids.get ( bid.getKey () ) == null)
                    return false;
            }
            for( Map.Entry<Integer, Double> item: this.items.entrySet ()){
                Double amount = shoppingBasketToCompare.items.get ( item.getKey () );
                if(amount == null || amount != item.getValue ())
                    return false;
            }
            for( Map.Entry<Integer, Double> item: shoppingBasketToCompare.items.entrySet ()){
                Double amount = this.items.get ( item.getKey () );
                if(amount == null || amount != item.getValue ())
                    return false;
            }
        }
        return false;
    }

    @Override
    public StringBuilder getReview() {
        StringBuilder review = new StringBuilder();
        for (Map.Entry<java.lang.Integer, Double> itemToAmount : items.entrySet()) {
            Item item = itemMap.get(itemToAmount.getKey());
            Double amount = itemToAmount.getValue();
            if (amount > 0) {
                review.append(String.format("%s, amount: %f\n", item.getReview(), amount));
            }
        }
        review.append ( String.format ( "total price: %f", price ));
        return review;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return calculatePrice();
    }

    private double calculatePrice() {
        double price = 0;
        for (Map.Entry<java.lang.Integer,Double> currItem:items.entrySet())
        {
            price = price + currItem.getValue()*itemMap.get(currItem.getKey()).getPrice();
        }
        if(bids != null && bids.size () > 0) {
            for ( Bid bid : bids.values ( ) ) {
                if (bid.getApproved ( ))
                    price += bid.getPrice ( ) * bid.getAmount ( );
            }
        }
        DecimalFormat format = new DecimalFormat("#.###");
        price = Double.parseDouble(format.format(price));
        setPrice(price);
        return price;
    }

    public boolean isEmpty() {
        if (this.items == null || this.items.isEmpty()) {
            return true;
        } else {
            for (Map.Entry<java.lang.Integer, Double> itemDoubleEntry : items.entrySet()) {
                if (itemDoubleEntry.getValue() > 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public Map<java.lang.Integer, Double> getItems() {
        return items;
    }

    public void addItem(Item item, double amount) throws MarketException {
        if (amount<0)
            throw new MarketException("Cant add negative amount of item to basket.");
        if(itemMap.get(item.getID()) == null)
            itemMap.put(item.getID(), item);
        else
            amount += items.get(item.getID());
        items.put(item.getID(),amount);
    }

    public void removeItem(Item item) {
        items.remove(item.getID());
    }


    public void updateQuantity(double amount, Item item) throws MarketException {
        if(!items.containsKey(item.getID())){
            throw new MarketException("Item is not in cart. cannot update amount");
        }
        if (amount<0)
        {
            DebugLog.getInstance().Log("Visitor tried to update negative amount for item.");
            throw new MarketException("Cant put negative amount for item");
        }
        items.replace(item.getID(), amount);
    }

    public Map<java.lang.Integer, Item> getItemMap() {
        return itemMap;
    }

    public void setItemMap(Map<java.lang.Integer, Item> itemMap) {
        this.itemMap = itemMap;
    }

    public void updatePrice(Shop shop) throws MarketException {
        setPrice (shop.calculateDiscount ( this ));
    }

    public double getCurrentPrice(){
        return price;
    }

    public void addABid(Bid bid) throws MarketException {
        if(bid==null)
        {
            DebugLog.getInstance().Log("Bid cant be null");
            throw new MarketException("Bid cant be null");
        }
        bids.put ( bid.getItemId (), bid );
    }

    public HashMap<Integer, Bid> getBids() {
        return bids;
    }

    public void setBids(HashMap<Integer, Bid> bids) {
        this.bids = bids;
    }

    public void removeBid(Integer itemId) {
        bids.remove ( itemId );
        calculatePrice ();
    }
}

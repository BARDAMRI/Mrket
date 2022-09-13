package com.example.server.serviceLayer.FacadeObjects;


import com.example.server.businessLayer.Market.Appointment.Appointment;
import com.example.server.businessLayer.Market.Bid;
import com.example.server.businessLayer.Market.Item;
import com.example.server.businessLayer.Market.Shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopFacade implements FacadeObject<Shop> {

    private String shopName;
    private Map<java.lang.Integer, ItemFacade> itemMap;             //<ItemID,main.businessLayer.Item>
    private Map<String, AppointmentFacade> employees;     //<name, appointment>
    private Map<java.lang.Integer, Double> itemsCurrentAmount;
    private List<BidFacade> bidsInShop;
    private boolean closed;

    public ShopFacade(String shopName, Map<java.lang.Integer, Item> itemMap, Map<String,
            Appointment> employees, Map<java.lang.Integer, Double> itemsCurrentAmount, List<Bid> bids, boolean closed) {
        this.shopName = shopName;
        updateItemMap ( itemMap );
        updateEmployees ( employees );
        updateItemsAmount ( itemsCurrentAmount );
        updateBidsInShop( bids );
        this.closed = closed;
    }


    public ShopFacade(Shop fromShop) {
        this.shopName = fromShop.getShopName();
        updateItemMap (fromShop.getItemMap());
        updateEmployees (fromShop.getEmployees());
        updateItemsAmount ( fromShop.getItemsCurrentAmountMap () );
        updateBidsInShop ( fromShop.getBids () );
        this.closed = fromShop.isClosed();
    }

    private void updateItemMap(Map<java.lang.Integer, Item> items){
        this.itemMap = new HashMap<> ();
        for( Map.Entry<java.lang.Integer, Item> entry : items.entrySet ()) {
            this.itemMap.put (entry.getKey (), new ItemFacade (entry.getValue ()) );
        }
    }

    private void updateEmployees(Map<String, Appointment> appointmentMap){
        this.employees = new HashMap<> ();
        for( Map.Entry<String, Appointment> entry : appointmentMap.entrySet ()) {
            this.employees.put (entry.getKey (), new ShopOwnerAppointmentFacade (  ).toFacade (entry.getValue ()) );
        }
    }

    private void updateItemsAmount(Map<java.lang.Integer, Double> itemsAmount){
        itemsCurrentAmount = new HashMap<>();
        for (Map.Entry<java.lang.Integer, Double> entry: itemsAmount.entrySet()){
            itemsCurrentAmount.put(entry.getKey(), entry.getValue());
        }
    }

    private void updateBidsInShop(List<Bid> bids) {
        bidsInShop = new ArrayList<> (  );
        for(Bid bid: bids)
            bidsInShop.add ( new BidFacade ( bid ) );
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public Map<java.lang.Integer, ItemFacade> getItemMap() {
        return itemMap;
    }

    public void setItemMap(Map<java.lang.Integer, ItemFacade> itemMap) {
        this.itemMap = itemMap;
    }

    public Map<String, AppointmentFacade> getEmployees() {
        return employees;
    }

    public void setEmployees(Map<String, AppointmentFacade> employees) {
        this.employees = employees;
    }

    public void setItemsCurrentAmount(Map<java.lang.Integer, Double> itemsCurrentAmount) {
        this.itemsCurrentAmount = itemsCurrentAmount;
    }

    public List<BidFacade> getBidsInShop() {
        return bidsInShop;
    }

    public void setBidsInShop(List<BidFacade> bidsInShop) {
        this.bidsInShop = bidsInShop;
    }

    public Map<java.lang.Integer, Double> getItemsCurrentAmount() {
        return itemsCurrentAmount;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    @Override
    public Shop toBusinessObject() {
        return null;
    }
}

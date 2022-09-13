package com.example.server.businessLayer.Market;


import com.example.server.businessLayer.Market.ResourcesObjects.DebugLog;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ClosedShopsHistory {
    private Map<String, Shop> closedShops;
    private StringBuilder overallHistory;
    private static ClosedShopsHistory instance;

    private ClosedShopsHistory(){
        this.closedShops = new ConcurrentHashMap<> ();
        this.overallHistory = new StringBuilder();
    }

    public synchronized static ClosedShopsHistory getInstance(){
        if (instance == null){
            instance =  new ClosedShopsHistory();
        }
        return instance;
    }

    public void closeShop(Shop closedShop) throws MarketException {
        if (closedShop == null){
            DebugLog.getInstance ().Log ( "tried to close a null shop!" );
            throw new MarketException("tried to close a null shop!");
        }
        if(closedShop.isClosed ())
        {
            DebugLog.getInstance ().Log ( String.format ( "shop %s is already closed", closedShop.getShopName () ) );
            throw new MarketException ( String.format ( "shop %s is already closed", closedShop.getShopName () ));
        }
        closedShops.put (closedShop.getShopName (), closedShop);
    }

    public void addPurchaseHistory(String purchaseReview, Shop shop) throws MarketException {
        if(shop == null)
            throw new MarketException("cannot add a purchase history of non defined shop (null)");
        if (purchaseReview != null && purchaseReview != ""){
            overallHistory.append("\n").append(purchaseReview);
        }
    }

    public Map<String, Shop> getClosedShops() {
        return closedShops;
    }

    public void setClosedShops(Map<String, Shop> closedShops) {
        this.closedShops = closedShops;
    }

    public StringBuilder getOverallHistory() {
        return overallHistory;
    }

    public void setOverallHistory(StringBuilder overallHistory) {
        this.overallHistory = overallHistory;
    }

    public boolean isClosed(String shopName) {
        return closedShops.containsKey ( shopName );
    }

    public void reset() {
        closedShops = new HashMap<>();
        overallHistory = new StringBuilder();
    }

    public void reopenShop(String shopName) {
        this.closedShops.remove(shopName);
    }
}

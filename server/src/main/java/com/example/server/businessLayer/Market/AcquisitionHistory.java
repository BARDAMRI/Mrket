package com.example.server.businessLayer.Market;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class ItemAcquisitionHistory {
    String shopName;
    String itemName;
    double amount;
    double totalPriceForItem;

    public ItemAcquisitionHistory(String shopName, String itemName, double amount, double totalPriceForItem) {
        this.shopName = shopName;
        this.itemName = itemName;
        this.amount = amount;
        this.totalPriceForItem = totalPriceForItem;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setTotalPriceForItem(double totalPriceForItem) {
        this.totalPriceForItem = totalPriceForItem;
    }

    public String getShopName() {
        return shopName;
    }

    public String getItemName() {
        return itemName;
    }

    public double getAmount() {
        return amount;
    }

    public double getTotalPriceForItem() {
        return totalPriceForItem;
    }

    @Override
    public String toString() {
        return "You bought : "+amount + " "+ itemName+" in the shop : "+ shopName+". Total price for this item:"+totalPriceForItem+"\n";
    }
}
//--------------------------------------------------------------------------------------------------------------------

public class AcquisitionHistory {
    private String name;
    private double totalPriceBeforeDiscount;
    private double discount;
    private double totalPriceAfterDiscount;
    private List<ItemAcquisitionHistory> itemAcquisitionHistories;

    public AcquisitionHistory(ShoppingCart cart , String name, double totalPriceAfterDiscount, double totalPriceBeforeDiscount)
    {
        itemAcquisitionHistories = new ArrayList<>();
        this.name = name;
        this.totalPriceBeforeDiscount = totalPriceBeforeDiscount;
        this.totalPriceAfterDiscount = totalPriceAfterDiscount;
        discount = totalPriceBeforeDiscount - totalPriceAfterDiscount;
        for (Map.Entry<Shop,ShoppingBasket> entry:cart.getCart().entrySet())
        {
            String curShop = entry.getKey().getShopName();
            for (Map.Entry<java.lang.Integer,Double> bask: entry.getValue().getItems().entrySet())
            {
                Item curItem = entry.getKey().getItemMap().get(bask.getKey());
                double curPrice = curItem.getPrice()*bask.getValue();
                ItemAcquisitionHistory acq = new ItemAcquisitionHistory(curShop,curItem.getName(), bask.getValue(),curPrice);
                itemAcquisitionHistories.add(acq);
            }
        }

    }

    public List<ItemAcquisitionHistory> getItemAcquisitions() {
        return itemAcquisitionHistories;
    }

    public void setItemAcquisitions(List<ItemAcquisitionHistory> itemAcquisitionHistories) {
        this.itemAcquisitionHistories = itemAcquisitionHistories;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        for (ItemAcquisitionHistory acq : itemAcquisitionHistories)
            str.append(acq.toString());
        return str.toString();

    }
}

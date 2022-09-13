package com.example.server.businessLayer.Market.Policies.DiscountPolicy.DiscountState;

import com.example.server.businessLayer.Market.Item;
import com.example.server.businessLayer.Market.Market;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.businessLayer.Market.ShoppingBasket;
import com.example.server.serviceLayer.FacadeObjects.PolicyFacade.*;

import java.util.Map;

public class CategoryLevelState extends DiscountLevelState {
    private Item.Category category;

    public CategoryLevelState(Item.Category category) {
        this.category = category;
    }

    @Override
    public double calculateDiscount(ShoppingBasket shoppingBasket, double percentageOfDiscount) throws MarketException {
        Map<java.lang.Integer, Double> items = shoppingBasket.getItems();
        double generalDiscount = 0;
        double discount = 0;
        double price = shoppingBasket.getPrice ();
        for (Map.Entry<java.lang.Integer, Double> entry : items.entrySet()) {
            Item item = Market.getInstance().getItemByID(entry.getKey());
            double amount = entry.getValue();
            if (item.getCategory().equals(this.category))
                discount = item.getPrice() * amount * (percentageOfDiscount / 100);
            generalDiscount += discount;
        }
        return price - generalDiscount;
    }
    @Override
    public boolean equals(Object object){
        if(object instanceof CategoryLevelState){
            CategoryLevelState toCompare = (CategoryLevelState) object;
            return this.category.equals(toCompare.category);
        }
        return false;
    }

    @Override
    public boolean isCategory(){
        return true;
    }

    @Override
    public DiscountLevelStateFacade visitToFacade(CategoryLevelStateFacade levelStateFacade) {
        return levelStateFacade.toFacade ( this );
    }

    @Override
    public DiscountLevelStateFacade visitToFacade(ItemLevelStateFacade levelStateFacade) {
        return null;
    }

    @Override
    public DiscountLevelStateFacade visitToFacade(ShopLevelStateFacade levelStateFacade) {
        return null;
    }

    @Override
    public DiscountLevelStateFacade visitToFacade(AndCompositeDiscountLevelStateFacade levelStateFacade) {
        return null;
    }

    @Override
    public DiscountLevelStateFacade visitToFacade(MaxXorCompositeDiscountLevelStateFacade levelStateFacade) {
        return null;
    }

    public Item.Category getCategory() {
        return category;
    }

    public void setCategory(Item.Category category) {
        this.category = category;
    }
}

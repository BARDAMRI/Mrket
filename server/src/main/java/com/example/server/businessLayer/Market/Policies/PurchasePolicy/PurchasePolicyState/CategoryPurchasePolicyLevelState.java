package com.example.server.businessLayer.Market.Policies.PurchasePolicy.PurchasePolicyState;

import com.example.server.businessLayer.Market.Item;
import com.example.server.businessLayer.Market.Market;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.businessLayer.Market.ShoppingBasket;
import com.example.server.serviceLayer.FacadeObjects.PolicyFacade.*;

import java.util.ArrayList;
import java.util.List;

public class CategoryPurchasePolicyLevelState extends PurchasePolicyLevelState {
    Item.Category category;

    public CategoryPurchasePolicyLevelState(Item.Category category) {
        this.category = category;
    }

    @Override
    public boolean isPolicyHeld(ShoppingBasket shoppingBasket, double amount, boolean greater) throws MarketException {
        double curAmount = getAmount ( shoppingBasket ).get ( 0 );
        return curAmount == 0 || ((greater && curAmount >= amount) || (!greater && curAmount <= amount));
    }

    @Override
    public List<Double> getAmount(ShoppingBasket shoppingBasket) throws MarketException {
        List<Double> amount = new ArrayList<> (  );
        double curAmount = 0;
        for(Integer itemId : shoppingBasket.getItems().keySet ()) {
            if (Market.getInstance().getItemByID(itemId).getCategory().equals(category)) {
                curAmount += shoppingBasket.getItems().get(itemId);
            }
        }
        amount.add ( curAmount );
        return amount;
    }

    @Override
    public boolean equals(Object object) {
        if(object instanceof CategoryPurchasePolicyLevelState){
            CategoryPurchasePolicyLevelState categoryPurchasePolicyLevelState = (CategoryPurchasePolicyLevelState) object;
            if(category.equals (categoryPurchasePolicyLevelState.category))
                return true;
        }
        return false;
    }

    @Override
    public boolean isItemLevel() {
        return false;
    }

    @Override
    public boolean isCategoryLevel() {
        return true;
    }

    @Override
    public boolean isShopLevel() {
        return false;
    }

    @Override
    public boolean isOrLevel() {
        return false;
    }

    @Override
    public boolean isXorLevel() {
        return false;
    }

    @Override
    public boolean isAndLevel() {
        return false;
    }

    @Override
    public PurchasePolicyLevelStateFacade visitToFacade(ItemPurchasePolicyLevelStateFacade levelStateFacade) {
        return null;
    }

    @Override
    public PurchasePolicyLevelStateFacade visitToFacade(CategoryPurchasePolicyLevelStateFacade levelStateFacade) {
        return levelStateFacade.toFacade ( this );
    }

    @Override
    public PurchasePolicyLevelStateFacade visitToFacade(ShopPurchasePolicyLevelStateFacade levelStateFacade) {
        return null;
    }

    @Override
    public PurchasePolicyLevelStateFacade visitToFacade(AndCompositePurchasePolicyLevelStateFacade levelStateFacade) {
        return null;
    }

    @Override
    public PurchasePolicyLevelStateFacade visitToFacade(XorCompositePurchasePolicyLevelStateFacade levelStateFacade) {
        return null;
    }

    @Override
    public PurchasePolicyLevelStateFacade visitToFacade(OrCompositePurchasePolicyLevelStateFacade levelStateFacade) {
        return null;
    }

    public Item.Category getCategory() {
        return category;
    }

    public void setCategory(Item.Category category) {
        this.category = category;
    }
}

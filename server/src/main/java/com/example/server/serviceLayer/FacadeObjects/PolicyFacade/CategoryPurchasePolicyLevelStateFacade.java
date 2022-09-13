package com.example.server.serviceLayer.FacadeObjects.PolicyFacade;

import com.example.server.businessLayer.Market.Item;
import com.example.server.businessLayer.Market.Policies.PurchasePolicy.PurchasePolicyState.*;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;

public class CategoryPurchasePolicyLevelStateFacade extends PurchasePolicyLevelStateFacade {
    Item.Category category;

    public CategoryPurchasePolicyLevelStateFacade(Item.Category category) {
        this.category = category;
    }

    public CategoryPurchasePolicyLevelStateFacade(){}

    public Item.Category getCategory() {
        return category;
    }

    public void setCategory(Item.Category category) {
        this.category = category;
    }

    @Override
    public PurchasePolicyLevelState toBusinessObject() throws MarketException {
        return new CategoryPurchasePolicyLevelState ( category );
    }

    @Override
    public PurchasePolicyLevelStateFacade toFacade(ItemPurchasePolicyLevelState levelState) {
        return null;
    }

    @Override
    public PurchasePolicyLevelStateFacade toFacade(CategoryPurchasePolicyLevelState levelState) {
        return new CategoryPurchasePolicyLevelStateFacade ( levelState.getCategory () );
    }

    @Override
    public PurchasePolicyLevelStateFacade toFacade(ShopPurchasePolicyLevelState levelState) {
        return null;
    }

    @Override
    public PurchasePolicyLevelStateFacade toFacade(AndCompositePurchasePolicyLevelState levelState) {
        return null;
    }

    @Override
    public PurchasePolicyLevelStateFacade toFacade(XorCompositePurchasePolicyLevelState levelState) {
        return null;
    }

    @Override
    public PurchasePolicyLevelStateFacade toFacade(OrCompositePurchasePolicyLevelState levelState) {
        return null;
    }

    @Override
    public PurchasePolicyLevelStateFacade toFacade(PurchasePolicyLevelState levelState) {
        return levelState.visitToFacade ( this );
    }
}

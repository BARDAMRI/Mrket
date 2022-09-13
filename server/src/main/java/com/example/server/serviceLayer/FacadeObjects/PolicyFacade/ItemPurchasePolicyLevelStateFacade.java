package com.example.server.serviceLayer.FacadeObjects.PolicyFacade;

import com.example.server.businessLayer.Market.Item;
import com.example.server.businessLayer.Market.Policies.PurchasePolicy.PurchasePolicyState.*;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;

public class ItemPurchasePolicyLevelStateFacade extends PurchasePolicyLevelStateFacade {
    int itemID;

    public ItemPurchasePolicyLevelStateFacade(int itemID) {
        this.itemID = itemID;
    }

    public ItemPurchasePolicyLevelStateFacade(){}

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    @Override
    public PurchasePolicyLevelState toBusinessObject() throws MarketException {
        return new ItemPurchasePolicyLevelState ( itemID );
    }

    @Override
    public PurchasePolicyLevelStateFacade toFacade(ItemPurchasePolicyLevelState levelState) {
        return new ItemPurchasePolicyLevelStateFacade ( levelState.getItemId () );
    }

    @Override
    public PurchasePolicyLevelStateFacade toFacade(CategoryPurchasePolicyLevelState levelState) {
        return null;
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

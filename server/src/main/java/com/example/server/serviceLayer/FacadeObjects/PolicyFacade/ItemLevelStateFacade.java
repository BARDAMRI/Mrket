package com.example.server.serviceLayer.FacadeObjects.PolicyFacade;

import com.example.server.businessLayer.Market.Policies.DiscountPolicy.DiscountState.*;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.serviceLayer.FacadeObjects.PolicyFacade.DiscountLevelStateFacade;

public class ItemLevelStateFacade extends DiscountLevelStateFacade {
    int itemID;

    public ItemLevelStateFacade(int itemID) {
        this.itemID = itemID;
    }

    public ItemLevelStateFacade(){}

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    @Override
    public DiscountLevelState toBusinessObject() throws MarketException {
        return new ItemLevelState ( itemID );
    }

    @Override
    public DiscountLevelStateFacade toFacade(ItemLevelState itemLevelState) {
        return new ItemLevelStateFacade ( itemLevelState.getItemID () );
    }

    @Override
    public DiscountLevelStateFacade toFacade(CategoryLevelState categoryLevelState) {
        return null;
    }

    @Override
    public DiscountLevelStateFacade toFacade(ShopLevelState shopLevelState) {
        return null;
    }

    @Override
    public DiscountLevelStateFacade toFacade(AndCompositeDiscountLevelState itemLevelState) {
        return null;
    }

    @Override
    public DiscountLevelStateFacade toFacade(MaxXorCompositeDiscountLevelState shopLevelState) {
        return null;
    }

    @Override
    public DiscountLevelStateFacade toFacade(DiscountLevelState discountLevelState) {
        return discountLevelState.visitToFacade ( this );
    }
}

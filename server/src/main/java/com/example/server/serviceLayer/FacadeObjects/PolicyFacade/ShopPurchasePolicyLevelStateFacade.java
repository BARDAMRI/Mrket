package com.example.server.serviceLayer.FacadeObjects.PolicyFacade;

import com.example.server.businessLayer.Market.Policies.PurchasePolicy.PurchasePolicyState.*;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;

public class ShopPurchasePolicyLevelStateFacade extends PurchasePolicyLevelStateFacade {
    @Override
    public PurchasePolicyLevelState toBusinessObject() throws MarketException {
        return new ShopPurchasePolicyLevelState ();
    }

    @Override
    public PurchasePolicyLevelStateFacade toFacade(ItemPurchasePolicyLevelState levelState) {
        return null;
    }

    @Override
    public PurchasePolicyLevelStateFacade toFacade(CategoryPurchasePolicyLevelState levelState) {
        return null;
    }

    @Override
    public PurchasePolicyLevelStateFacade toFacade(ShopPurchasePolicyLevelState levelState) {
        return new ShopPurchasePolicyLevelStateFacade ();
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

package com.example.server.businessLayer.Market.Policies.PurchasePolicy;

import com.example.server.businessLayer.Market.Policies.PurchasePolicy.PurchasePolicyState.PurchasePolicyLevelState;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.businessLayer.Market.ShoppingBasket;
import com.example.server.businessLayer.Market.Users.Visitor;
import com.example.server.serviceLayer.FacadeObjects.PolicyFacade.*;

public abstract class PurchasePolicyType {
    PurchasePolicyLevelState purchasePolicyLevelState;

    public PurchasePolicyType(PurchasePolicyLevelState purchasePolicyLevelState) {
        this.purchasePolicyLevelState = purchasePolicyLevelState;
    }

    public PurchasePolicyLevelState getPurchasePolicyLevelState() {
        return purchasePolicyLevelState;
    }

    public void setPurchasePolicyLevelState(PurchasePolicyLevelState purchasePolicyLevelState) {
        this.purchasePolicyLevelState = purchasePolicyLevelState;
    }

    public abstract boolean equals(Object object);

    public abstract boolean isPolicyHeld(ShoppingBasket shoppingBasket) throws MarketException;

    public abstract boolean isAtLeast();

    public abstract boolean isAtMost();

    public abstract boolean isOrComposite();

    public abstract PurchasePolicyTypeFacade visitToFacade(AtLeastPurchasePolicyTypeFacade policyTypeFacade);
    public abstract  PurchasePolicyTypeFacade visitToFacade(AtMostPurchasePolicyTypeFacade policyTypeFacade);
    public abstract PurchasePolicyTypeFacade visitToFacade(OrCompositePurchasePolicyTypeFacade policyTypeFacade);
}

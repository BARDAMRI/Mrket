package com.example.server.serviceLayer.FacadeObjects.PolicyFacade;

import com.example.server.businessLayer.Market.Policies.PurchasePolicy.PurchasePolicyState.PurchasePolicyLevelState;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;

import java.util.List;

public abstract class CompositePurchasePolicyLevelStateFacade extends PurchasePolicyLevelStateFacade {
    protected List<PurchasePolicyLevelStateFacade> purchasePolicyLevelStateFacades;

    public CompositePurchasePolicyLevelStateFacade(List<PurchasePolicyLevelStateFacade> purchasePolicyLevelStateFacades) {
        this.purchasePolicyLevelStateFacades = purchasePolicyLevelStateFacades;
    }

    public CompositePurchasePolicyLevelStateFacade(){}

    public List<PurchasePolicyLevelStateFacade> getPurchasePolicyLevelStateFacades() {
        return purchasePolicyLevelStateFacades;
    }

    public void setPurchasePolicyLevelStateFacades(List<PurchasePolicyLevelStateFacade> purchasePolicyLevelStateFacades) {
        this.purchasePolicyLevelStateFacades = purchasePolicyLevelStateFacades;
    }
}

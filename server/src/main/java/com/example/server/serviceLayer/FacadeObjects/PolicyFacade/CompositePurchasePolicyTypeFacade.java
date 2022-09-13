package com.example.server.serviceLayer.FacadeObjects.PolicyFacade;

import java.util.List;

public abstract class CompositePurchasePolicyTypeFacade extends PurchasePolicyTypeFacade {
    protected List<PurchasePolicyTypeFacade> purchasePolicyTypeFacades;

    public CompositePurchasePolicyTypeFacade(List<PurchasePolicyTypeFacade> purchasePolicyTypeFacades) {
        this.purchasePolicyTypeFacades = purchasePolicyTypeFacades;
    }

    public CompositePurchasePolicyTypeFacade(){}

    public List<PurchasePolicyTypeFacade> getPurchasePolicyTypeFacades() {
        return purchasePolicyTypeFacades;
    }

    public void setPurchasePolicyTypeFacades(List<PurchasePolicyTypeFacade> purchasePolicyTypeFacades) {
        this.purchasePolicyTypeFacades = purchasePolicyTypeFacades;
    }
}

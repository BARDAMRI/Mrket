package com.example.server.serviceLayer.FacadeObjects.PolicyFacade;

import com.example.server.businessLayer.Market.Policies.PurchasePolicy.AtLeastPurchasePolicyType;
import com.example.server.businessLayer.Market.Policies.PurchasePolicy.AtMostPurchasePolicyType;
import com.example.server.businessLayer.Market.Policies.PurchasePolicy.OrCompositePurchasePolicyType;
import com.example.server.businessLayer.Market.Policies.PurchasePolicy.PurchasePolicyType;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;

public class AtLeastPurchasePolicyTypeFacade extends PurchasePolicyTypeFacade {
    protected double amount;
    public AtLeastPurchasePolicyTypeFacade(PurchasePolicyLevelStateFacade purchasePolicyLevelStateFacade, double amount) {
        super ( purchasePolicyLevelStateFacade );
        this.amount = amount;
    }

    public AtLeastPurchasePolicyTypeFacade() {
    }

    @Override
    public PurchasePolicyTypeFacade toFacade(OrCompositePurchasePolicyType purchasePolicyType) {
        return null;
    }

    @Override
    public PurchasePolicyTypeFacade toFacade(AtLeastPurchasePolicyType purchasePolicyType) {
        PurchasePolicyLevelStateFacade purchasePolicyLevel = getPurchasePolicyLevel (purchasePolicyType.getPurchasePolicyLevelState ());
        return new AtLeastPurchasePolicyTypeFacade ( purchasePolicyLevel, purchasePolicyType.getAmount () );
    }

    @Override
    public PurchasePolicyTypeFacade toFacade(AtMostPurchasePolicyType purchasePolicyType) {
        return null;
    }

    @Override
    public PurchasePolicyTypeFacade toFacade(PurchasePolicyType purchasePolicyType) {
        return purchasePolicyType.visitToFacade ( this );
    }

    @Override
    public PurchasePolicyType toBusinessObject() throws MarketException {
        return new AtLeastPurchasePolicyType ( purchasePolicyLevelStateFacade.toBusinessObject (), amount );
    }
}

package com.example.server.serviceLayer.FacadeObjects.PolicyFacade;

import com.example.server.businessLayer.Market.Policies.PurchasePolicy.AtLeastPurchasePolicyType;
import com.example.server.businessLayer.Market.Policies.PurchasePolicy.AtMostPurchasePolicyType;
import com.example.server.businessLayer.Market.Policies.PurchasePolicy.OrCompositePurchasePolicyType;
import com.example.server.businessLayer.Market.Policies.PurchasePolicy.PurchasePolicyType;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;

    public class AtMostPurchasePolicyTypeFacade extends PurchasePolicyTypeFacade {

    protected double amount;

    public AtMostPurchasePolicyTypeFacade(PurchasePolicyLevelStateFacade purchasePolicyLevelStateFacade, double amount) {
        super ( purchasePolicyLevelStateFacade );
        this.amount = amount;
    }

    public AtMostPurchasePolicyTypeFacade() {
    }

    @Override
    public PurchasePolicyTypeFacade toFacade(OrCompositePurchasePolicyType purchasePolicyType) {
        return null;
    }

    @Override
    public PurchasePolicyTypeFacade toFacade(AtLeastPurchasePolicyType purchasePolicyType) {
        return null;
    }

    @Override
    public PurchasePolicyTypeFacade toFacade(AtMostPurchasePolicyType purchasePolicyType) {
        PurchasePolicyLevelStateFacade purchasePolicyLevelStateFacade = getPurchasePolicyLevel ( purchasePolicyType.getPurchasePolicyLevelState () );
        return new AtMostPurchasePolicyTypeFacade ( purchasePolicyLevelStateFacade, purchasePolicyType.getAmount ( ) );
    }

    @Override
    public PurchasePolicyTypeFacade toFacade(PurchasePolicyType purchasePolicyType) {
        return purchasePolicyType.visitToFacade ( this );
    }

    @Override
    public PurchasePolicyType toBusinessObject() throws MarketException {
        return new AtMostPurchasePolicyType ( purchasePolicyLevelStateFacade.toBusinessObject (), amount );
    }
}

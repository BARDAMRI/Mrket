package com.example.server.serviceLayer.FacadeObjects.PolicyFacade;

import com.example.server.businessLayer.Market.Policies.PurchasePolicy.PurchasePolicyState.*;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;

import java.util.ArrayList;
import java.util.List;

public class AndCompositePurchasePolicyLevelStateFacade extends CompositePurchasePolicyLevelStateFacade{
    public AndCompositePurchasePolicyLevelStateFacade(List<PurchasePolicyLevelStateFacade> purchasePolicyLevelStateFacades) {
        super ( purchasePolicyLevelStateFacades );
    }

    public AndCompositePurchasePolicyLevelStateFacade() {
    }

    @Override
    public PurchasePolicyLevelState toBusinessObject() throws MarketException {
        List<PurchasePolicyLevelState> purchasePolicyLevelStates = new ArrayList<> (  );
        for(PurchasePolicyLevelStateFacade purchasePolicyLevelStateFacade : purchasePolicyLevelStateFacades)
            purchasePolicyLevelStates.add ( purchasePolicyLevelStateFacade.toBusinessObject () );
        return new AndCompositePurchasePolicyLevelState ( purchasePolicyLevelStates);
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
        return null;
    }

    @Override
    public PurchasePolicyLevelStateFacade toFacade(AndCompositePurchasePolicyLevelState levelState) {
        List<PurchasePolicyLevelStateFacade> purchasePolicyLevelStateFacades = new ArrayList<> (  );
        List<PurchasePolicyLevelState> purchasePolicyLevelStates = levelState.getPurchasePolicyLevelStates ();
        for(PurchasePolicyLevelState purchasePolicyLevelState : purchasePolicyLevelStates){
            purchasePolicyLevelStateFacades.add ( getPurchasePolicyLevel(purchasePolicyLevelState) );
        }
        return new AndCompositePurchasePolicyLevelStateFacade (purchasePolicyLevelStateFacades);
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

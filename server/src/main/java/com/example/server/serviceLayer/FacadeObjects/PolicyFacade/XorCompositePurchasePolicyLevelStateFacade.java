package com.example.server.serviceLayer.FacadeObjects.PolicyFacade;

import com.example.server.businessLayer.Market.Policies.PurchasePolicy.PurchasePolicyState.*;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;

import java.util.ArrayList;
import java.util.List;

public class XorCompositePurchasePolicyLevelStateFacade extends CompositePurchasePolicyLevelStateFacade{
    public XorCompositePurchasePolicyLevelStateFacade(List<PurchasePolicyLevelStateFacade> purchasePolicyLevelStateFacades) {
        super ( purchasePolicyLevelStateFacades );
    }

    public XorCompositePurchasePolicyLevelStateFacade() {
    }

    @Override
    public PurchasePolicyLevelState toBusinessObject() throws MarketException {
        List<PurchasePolicyLevelState> purchasePolicyLevelStates = new ArrayList<> (  );
        for(PurchasePolicyLevelStateFacade purchasePolicyLevelStateFacade : purchasePolicyLevelStateFacades)
            purchasePolicyLevelStates.add ( purchasePolicyLevelStateFacade.toBusinessObject () );
        return new XorCompositePurchasePolicyLevelState ( purchasePolicyLevelStates);
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
        return null;
    }

    @Override
    public PurchasePolicyLevelStateFacade toFacade(XorCompositePurchasePolicyLevelState levelState) {

        List<PurchasePolicyLevelStateFacade> purchasePolicyLevelStateFacades = new ArrayList<> (  );
        List<PurchasePolicyLevelState> purchasePolicyLevelStates = levelState.getPurchasePolicyLevelStates ();
        for(PurchasePolicyLevelState purchasePolicyLevelState : purchasePolicyLevelStates){
            purchasePolicyLevelStateFacades.add ( getPurchasePolicyLevel(purchasePolicyLevelState) );
        }
        return new XorCompositePurchasePolicyLevelStateFacade (purchasePolicyLevelStateFacades);
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

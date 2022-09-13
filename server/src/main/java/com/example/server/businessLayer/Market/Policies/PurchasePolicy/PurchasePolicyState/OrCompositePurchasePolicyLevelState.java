package com.example.server.businessLayer.Market.Policies.PurchasePolicy.PurchasePolicyState;

import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.businessLayer.Market.ShoppingBasket;
import com.example.server.serviceLayer.FacadeObjects.PolicyFacade.*;

import java.util.List;

public class OrCompositePurchasePolicyLevelState extends CompositePurchasePolicyLevelState{

    public OrCompositePurchasePolicyLevelState(List<PurchasePolicyLevelState> purchasePolicyLevelStates) {
        super ( purchasePolicyLevelStates );
    }

    @Override
    public boolean isPolicyHeld(ShoppingBasket shoppingBasket, double amount, boolean greater) throws MarketException {
        double curAmount = 0;
        for ( PurchasePolicyLevelState purchasePolicyLevelState : purchasePolicyLevelStates ) {
            List<Double> amounts = purchasePolicyLevelState.getAmount ( shoppingBasket );
            for(Double cur : amounts)
                curAmount += cur;
        }
        return curAmount == 0 || (((greater && curAmount > amount) || curAmount <= amount));
    }

    @Override
    public boolean equals(Object object) {
        if(object instanceof OrCompositePurchasePolicyLevelState){
            OrCompositePurchasePolicyLevelState orCompositePurchasePolicyLevelState = (OrCompositePurchasePolicyLevelState) object;
            for( PurchasePolicyLevelState purchasePolicyLevelState : orCompositePurchasePolicyLevelState.purchasePolicyLevelStates){
                if (!this.purchasePolicyLevelStates.contains ( purchasePolicyLevelState ))
                    return false;
            }
            for( PurchasePolicyLevelState purchasePolicyLevelState : this.purchasePolicyLevelStates){
                if (!orCompositePurchasePolicyLevelState.purchasePolicyLevelStates.contains ( purchasePolicyLevelState ))
                    return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean isItemLevel() {
        return false;
    }

    @Override
    public boolean isCategoryLevel() {
        return false;
    }

    @Override
    public boolean isShopLevel() {
        return false;
    }

    @Override
    public boolean isOrLevel() {
        return true;
    }

    @Override
    public boolean isXorLevel() {
        return false;
    }

    @Override
    public boolean isAndLevel() {
        return false;
    }

    @Override
    public PurchasePolicyLevelStateFacade visitToFacade(ItemPurchasePolicyLevelStateFacade levelStateFacade) {
        return null;
    }

    @Override
    public PurchasePolicyLevelStateFacade visitToFacade(CategoryPurchasePolicyLevelStateFacade levelStateFacade) {
        return null;
    }

    @Override
    public PurchasePolicyLevelStateFacade visitToFacade(ShopPurchasePolicyLevelStateFacade levelStateFacade) {
        return null;
    }

    @Override
    public PurchasePolicyLevelStateFacade visitToFacade(AndCompositePurchasePolicyLevelStateFacade levelStateFacade) {
        return null;
    }

    @Override
    public PurchasePolicyLevelStateFacade visitToFacade(XorCompositePurchasePolicyLevelStateFacade levelStateFacade) {
        return null;
    }

    @Override
    public PurchasePolicyLevelStateFacade visitToFacade(OrCompositePurchasePolicyLevelStateFacade levelStateFacade) {
        return null;
    }
}

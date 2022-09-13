package com.example.server.businessLayer.Market.Policies.PurchasePolicy.PurchasePolicyState;

import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.businessLayer.Market.ShoppingBasket;
import com.example.server.serviceLayer.FacadeObjects.PolicyFacade.*;

import java.util.List;

public class XorCompositePurchasePolicyLevelState extends CompositePurchasePolicyLevelState{

    public XorCompositePurchasePolicyLevelState(List<PurchasePolicyLevelState> purchasePolicyLevelStates) {
        super ( purchasePolicyLevelStates );
    }

    @Override
    public boolean isPolicyHeld(ShoppingBasket shoppingBasket, double amount, boolean greater) throws MarketException {
        for ( PurchasePolicyLevelState purchasePolicyLevelState : purchasePolicyLevelStates ) {
            List<Double> amounts = purchasePolicyLevelState.getAmount ( shoppingBasket );
            int numOfZeroes = 0;
            for(Double cur : amounts) {
                if ((greater && cur > amount) || cur <= amount)
                    return true;
                if (cur == 0)
                    numOfZeroes++;
            }
            if(numOfZeroes == amounts.size ())
                return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object object) {
        if(object instanceof XorCompositePurchasePolicyLevelState){
            XorCompositePurchasePolicyLevelState xorCompositePurchasePolicyLevelState = (XorCompositePurchasePolicyLevelState) object;
            for( PurchasePolicyLevelState purchasePolicyLevelState : xorCompositePurchasePolicyLevelState.purchasePolicyLevelStates){
                if (!this.purchasePolicyLevelStates.contains ( purchasePolicyLevelState ))
                    return false;
            }
            for( PurchasePolicyLevelState purchasePolicyLevelState : this.purchasePolicyLevelStates){
                if (!xorCompositePurchasePolicyLevelState.purchasePolicyLevelStates.contains ( purchasePolicyLevelState ))
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
        return false;
    }

    @Override
    public boolean isXorLevel() {
        return true;
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

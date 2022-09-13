package com.example.server.businessLayer.Market.Policies.PurchasePolicy.PurchasePolicyState;

import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.businessLayer.Market.ShoppingBasket;
import org.springframework.aop.target.LazyInitTargetSource;

import java.util.ArrayList;
import java.util.List;

public abstract class CompositePurchasePolicyLevelState extends PurchasePolicyLevelState {
    List<PurchasePolicyLevelState> purchasePolicyLevelStates;

    public CompositePurchasePolicyLevelState(List<PurchasePolicyLevelState> purchasePolicyLevelStates) {
        this.purchasePolicyLevelStates = purchasePolicyLevelStates;
    }

    public List<Double> getAmount(ShoppingBasket shoppingBasket) throws MarketException {
        List<Double> amounts = new ArrayList<> (  );
        for(PurchasePolicyLevelState purchasePolicyLevelState : purchasePolicyLevelStates){
            amounts.addAll (purchasePolicyLevelState.getAmount ( shoppingBasket ));
        }
        return amounts;
    }

    public List<PurchasePolicyLevelState> getPurchasePolicyLevelStates() {
        return purchasePolicyLevelStates;
    }

    public void setPurchasePolicyLevelStates(List<PurchasePolicyLevelState> purchasePolicyLevelStates) {
        this.purchasePolicyLevelStates = purchasePolicyLevelStates;
    }
}


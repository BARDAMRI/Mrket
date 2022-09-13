package com.example.server.businessLayer.Market.Policies.PurchasePolicy;

import com.example.server.businessLayer.Market.Policies.PurchasePolicy.PurchasePolicyState.PurchasePolicyLevelState;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.businessLayer.Market.ShoppingBasket;
import com.example.server.businessLayer.Market.Users.Visitor;
import com.example.server.serviceLayer.FacadeObjects.PolicyFacade.AtLeastPurchasePolicyTypeFacade;
import com.example.server.serviceLayer.FacadeObjects.PolicyFacade.AtMostPurchasePolicyTypeFacade;
import com.example.server.serviceLayer.FacadeObjects.PolicyFacade.OrCompositePurchasePolicyTypeFacade;
import com.example.server.serviceLayer.FacadeObjects.PolicyFacade.PurchasePolicyTypeFacade;

public class AtLeastPurchasePolicyType extends PurchasePolicyType {
    private double amount;

    public AtLeastPurchasePolicyType(PurchasePolicyLevelState purchasePolicyLevelState, double amount) {
        super ( purchasePolicyLevelState );
        this.amount = amount;
    }

    @Override
    public boolean equals(Object object) {
        if(object instanceof AtLeastPurchasePolicyType){
            AtLeastPurchasePolicyType atLeastPurchasePolicyType = (AtLeastPurchasePolicyType) object;
            return atLeastPurchasePolicyType.amount == this.amount;
        }
        return false;
    }

    @Override
    public boolean isPolicyHeld(ShoppingBasket shoppingBasket) throws MarketException {
        return purchasePolicyLevelState.isPolicyHeld ( shoppingBasket, amount, true);
    }

    @Override
    public boolean isAtLeast() {
        return true;
    }

    @Override
    public boolean isAtMost() {
        return false;
    }

    @Override
    public boolean isOrComposite() {
        return false;
    }

    @Override
    public PurchasePolicyTypeFacade visitToFacade(AtLeastPurchasePolicyTypeFacade policyTypeFacade) {
        return policyTypeFacade.toFacade ( this );
    }

    @Override
    public PurchasePolicyTypeFacade visitToFacade(AtMostPurchasePolicyTypeFacade policyTypeFacade) {
        return null;
    }

    @Override
    public PurchasePolicyTypeFacade visitToFacade(OrCompositePurchasePolicyTypeFacade policyTypeFacade) {
        return null;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}

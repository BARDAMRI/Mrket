package com.example.server.businessLayer.Market.Policies.PurchasePolicy;

import com.example.server.businessLayer.Market.Policies.DiscountPolicy.CompositeDiscount.CompositeDiscount;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.DiscountType;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.businessLayer.Market.ShoppingBasket;
import com.example.server.businessLayer.Market.Users.Visitor;

import java.util.ArrayList;
import java.util.List;

public class PurchasePolicy {

    public PurchasePolicy() {
        this.validPurchasePolicies = new ArrayList<>();
    }

    private List<PurchasePolicyType> validPurchasePolicies;
    public List<PurchasePolicyType> getValidPurchasePolicies() {
        return validPurchasePolicies;
    }

    public void setValidPurchasePolicies(List<PurchasePolicyType> validPurchasePolicies) {
        this.validPurchasePolicies = validPurchasePolicies;
    }

    public void addPurchasePolicy(PurchasePolicyType purchasePolicyType){
        throw new UnsupportedOperationException (  );
    }

    public boolean isPoliciesHeld(ShoppingBasket shoppingBasket) throws MarketException {
        for(PurchasePolicyType purchasePolicyType : validPurchasePolicies){
            if (!purchasePolicyType.isPolicyHeld (shoppingBasket ))
                return false;
        }
        return true;
    }

    public void addNewPurchasePolicy(PurchasePolicyType purchasePolicyType) throws MarketException {
        if(purchasePolicyType == null)
            throw new MarketException ( "purchase policy is null" );
        if(validPurchasePolicies.contains ( purchasePolicyType ))
            throw new MarketException ( "policy is already exist in the shop" );
        if(purchasePolicyType instanceof CompositePurchasePolicyType){
            purePolicies ( purchasePolicyType);
        }
        validPurchasePolicies.add ( purchasePolicyType );
    }

    private void purePolicies(PurchasePolicyType purchasePolicyType) {
        if(purchasePolicyType instanceof CompositePurchasePolicyType) {
            CompositePurchasePolicyType compositePurchasePolicyType = (CompositePurchasePolicyType) purchasePolicyType;
            for ( PurchasePolicyType purchasePolicyType1 : compositePurchasePolicyType.getPolicies ( ) ) {
                if (validPurchasePolicies.contains ( purchasePolicyType1 )) {
                    validPurchasePolicies.remove ( purchasePolicyType1 );
                } else if (purchasePolicyType1 instanceof CompositePurchasePolicyType) {
                    purePolicies (purchasePolicyType1);
                }
            }
        } else if(validPurchasePolicies.contains ( purchasePolicyType ))
            validPurchasePolicies.remove ( purchasePolicyType );
    }

    public void removePurchasePolicy(PurchasePolicyType purchasePolicyType) {
        if(validPurchasePolicies.contains ( purchasePolicyType ))
            validPurchasePolicies.remove ( purchasePolicyType );
    }
}

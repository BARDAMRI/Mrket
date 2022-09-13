package com.example.server.businessLayer.Market.Policies.PurchasePolicy;

import com.example.server.businessLayer.Market.Policies.PurchasePolicy.PurchasePolicyState.PurchasePolicyLevelState;

import java.util.List;

public abstract class CompositePurchasePolicyType extends PurchasePolicyType {
    List<PurchasePolicyType> policies;

    public CompositePurchasePolicyType(PurchasePolicyLevelState purchasePolicyLevelState, List<PurchasePolicyType> policies) {
        super ( purchasePolicyLevelState );
        this.policies = policies;
    }


    public List<PurchasePolicyType> getPolicies() {
        return policies;
    }

    public void setPolicies(List<PurchasePolicyType> policies) {
        this.policies = policies;
    }
}

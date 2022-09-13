package com.example.server.serviceLayer.FacadeObjects.PolicyFacade;

import com.example.server.businessLayer.Market.Policies.DiscountPolicy.DiscountState.*;
import com.example.server.businessLayer.Market.Policies.PurchasePolicy.PurchasePolicyState.OrCompositePurchasePolicyLevelState;
import com.example.server.businessLayer.Market.Policies.PurchasePolicy.PurchasePolicyState.XorCompositePurchasePolicyLevelState;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;

import java.util.ArrayList;
import java.util.List;

public class MaxXorCompositeDiscountLevelStateFacade extends CompositeDiscountLevelStateFacade{
    public MaxXorCompositeDiscountLevelStateFacade(List<DiscountLevelStateFacade> discountLevelStateFacades) {
        super ( discountLevelStateFacades );
    }

    public MaxXorCompositeDiscountLevelStateFacade(){}

    @Override
    public DiscountLevelState toBusinessObject() throws MarketException {
        List<DiscountLevelState> discountLevelStates = new ArrayList<> (  );
        for(DiscountLevelStateFacade discountLevelStateFacade : discountLevelStateFacades)
            discountLevelStates.add ( discountLevelStateFacade.toBusinessObject () );
        return new MaxXorCompositeDiscountLevelState ( discountLevelStates );
    }

    @Override
    public DiscountLevelStateFacade toFacade(ItemLevelState itemLevelState) {
        return null;
    }

    @Override
    public DiscountLevelStateFacade toFacade(CategoryLevelState categoryLevelState) {
        return null;
    }

    @Override
    public DiscountLevelStateFacade toFacade(ShopLevelState shopLevelState) {
        return null;
    }

    @Override
    public DiscountLevelStateFacade toFacade(AndCompositeDiscountLevelState andCompositeDiscountLevelState) {
        return null;
    }

    @Override
    public DiscountLevelStateFacade toFacade(MaxXorCompositeDiscountLevelState maxXorCompositeDiscountLevelState) {
        List<DiscountLevelStateFacade> discountLevelStateFacades = new ArrayList<> (  );
        List<DiscountLevelState> discountLevelStates = maxXorCompositeDiscountLevelState.getDiscountLevelStates ();
        for(DiscountLevelState discountLevelState : discountLevelStates)
            discountLevelStateFacades.add ( getDiscountLevelStateFacade ( discountLevelState ) );
        return new MaxXorCompositeDiscountLevelStateFacade ( discountLevelStateFacades );
    }

    @Override
    public DiscountLevelStateFacade toFacade(DiscountLevelState discountLevelState) {
        return discountLevelState.visitToFacade ( this );
    }
}

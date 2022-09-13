package com.example.server.serviceLayer.FacadeObjects.PolicyFacade;

import com.example.server.businessLayer.Market.Policies.DiscountPolicy.DiscountState.*;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.serviceLayer.FacadeObjects.PolicyFacade.DiscountLevelStateFacade;

public class ShopLevelStateFacade extends DiscountLevelStateFacade {
    @Override
    public DiscountLevelState toBusinessObject() throws MarketException {
        return new ShopLevelState ();
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
        return new ShopLevelStateFacade ();
    }

    @Override
    public DiscountLevelStateFacade toFacade(AndCompositeDiscountLevelState itemLevelState) {
        return null;
    }

    @Override
    public DiscountLevelStateFacade toFacade(MaxXorCompositeDiscountLevelState shopLevelState) {
        return null;
    }

    @Override
    public DiscountLevelStateFacade toFacade(DiscountLevelState discountLevelState) {
        return discountLevelState.visitToFacade ( this );
    }
}

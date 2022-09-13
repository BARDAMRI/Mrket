package com.example.server.serviceLayer.FacadeObjects.PolicyFacade;

import com.example.server.businessLayer.Market.Policies.DiscountPolicy.DiscountState.DiscountLevelState;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;

import java.util.List;

public abstract class CompositeDiscountLevelStateFacade extends DiscountLevelStateFacade {
    List<DiscountLevelStateFacade> discountLevelStateFacades;

    public CompositeDiscountLevelStateFacade(List<DiscountLevelStateFacade> discountLevelStateFacades) {
        this.discountLevelStateFacades = discountLevelStateFacades;
    }

    public CompositeDiscountLevelStateFacade(){}

    public List<DiscountLevelStateFacade> getDiscountLevelStateFacades() {
        return discountLevelStateFacades;
    }

    public void setDiscountLevelStateFacades(List<DiscountLevelStateFacade> discountLevelStateFacades) {
        this.discountLevelStateFacades = discountLevelStateFacades;
    }
}

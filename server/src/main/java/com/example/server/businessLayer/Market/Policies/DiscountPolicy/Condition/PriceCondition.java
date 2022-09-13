package com.example.server.businessLayer.Market.Policies.DiscountPolicy.Condition;

import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.businessLayer.Market.ShoppingBasket;
import com.example.server.serviceLayer.FacadeObjects.PolicyFacade.*;

public class PriceCondition extends Condition {
    private double priceNeeded;

    public PriceCondition(double priceNeeded) {
        this.priceNeeded = priceNeeded;
    }

    /**
     *
     * @param shoppingBasket
     * @return price of the shoppingBasket >= priceNeeded
     */
    @Override
    public boolean isDiscountHeld(ShoppingBasket shoppingBasket) throws MarketException {
        return shoppingBasket.getPrice() >= priceNeeded;
    }

    @Override
    public boolean equals(Object object) {
        if(object instanceof PriceCondition){
            PriceCondition toCompare = (PriceCondition) object;
            return this.priceNeeded == toCompare.priceNeeded;
        }
        return false;
    }

    @Override
    public boolean isPrice(){
        return true;
    }

    @Override
    public ConditionFacade visitToFacade(AmountOfItemConditionFacade conditionFacade) {
        return null;
    }

    @Override
    public ConditionFacade visitToFacade(PriceConditionFacade conditionFacade) {
        return conditionFacade.toFacade ( this );
    }

    @Override
    public ConditionFacade visitToFacade(AndCompositeConditionFacade conditionFacade) {
        return null;
    }

    @Override
    public ConditionFacade visitToFacade(OrCompositeConditionFacade conditionFacade) {
        return null;
    }


    public double getPriceNeeded() {
        return priceNeeded;
    }

    public void setPriceNeeded(double priceNeeded) {
        this.priceNeeded = priceNeeded;
    }
}

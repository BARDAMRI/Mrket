package com.example.server.businessLayer.Market.Policies.DiscountPolicy.Condition;

import com.example.server.businessLayer.Market.Policies.DiscountPolicy.DiscountState.CategoryLevelState;
import com.example.server.businessLayer.Market.ShoppingBasket;
import com.example.server.serviceLayer.FacadeObjects.PolicyFacade.*;

import java.util.Map;

public class AmountOfItemCondition extends Condition {
    private double amountNeeded;
    private Integer itemNeeded;

    public AmountOfItemCondition(double amountNeeded, Integer itemNeeded) {
        this.amountNeeded = amountNeeded;
        this.itemNeeded = itemNeeded;
    }

    /**
     *
     * @param shoppingBasket
     * @return there is at least amountNeeded of itemNeeded in the shoppingBasket
     */
    @Override
    public boolean isDiscountHeld(ShoppingBasket shoppingBasket) {
        Map<Integer,Double> map = shoppingBasket.getItems();
        return (map.containsKey(itemNeeded)&&map.get(itemNeeded)>=amountNeeded);
    }
    @Override
    public boolean equals(Object object){
        if(object instanceof AmountOfItemCondition){
            AmountOfItemCondition toCompare = (AmountOfItemCondition) object;
            return this.amountNeeded == toCompare.amountNeeded && this.itemNeeded == toCompare.itemNeeded;
        }
        return false;
    }

    @Override
    public boolean isAmountOfItem(){
        return true;
    }

    @Override
    public ConditionFacade visitToFacade(AmountOfItemConditionFacade conditionFacade) {
        return conditionFacade.toFacade ( this );
    }

    @Override
    public ConditionFacade visitToFacade(PriceConditionFacade conditionFacade) {
        return null;
    }

    @Override
    public ConditionFacade visitToFacade(AndCompositeConditionFacade conditionFacade) {
        return null;
    }

    @Override
    public ConditionFacade visitToFacade(OrCompositeConditionFacade conditionFacade) {
        return null;
    }

    public double getAmountNeeded() {
        return amountNeeded;
    }

    public void setAmountNeeded(double amountNeeded) {
        this.amountNeeded = amountNeeded;
    }

    public Integer getItemNeeded() {
        return itemNeeded;
    }

    public void setItemNeeded(Integer itemNeeded) {
        this.itemNeeded = itemNeeded;
    }
}

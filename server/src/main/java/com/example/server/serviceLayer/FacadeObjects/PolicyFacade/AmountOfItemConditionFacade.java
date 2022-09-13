package com.example.server.serviceLayer.FacadeObjects.PolicyFacade;

import com.example.server.businessLayer.Market.Policies.DiscountPolicy.Condition.AmountOfItemCondition;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.Condition.CompositionCondition.AndCompositeCondition;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.Condition.CompositionCondition.OrCompositeCondition;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.Condition.Condition;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.Condition.PriceCondition;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;

public class AmountOfItemConditionFacade extends ConditionFacade {
    private double amount;
    int itemID;

    public AmountOfItemConditionFacade(double amount, int itemID) {
        this.amount = amount;
        this.itemID = itemID;
    }

    public AmountOfItemConditionFacade() {
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    @Override
    public Condition toBusinessObject() throws MarketException {
        return new AmountOfItemCondition (amount, itemID);
    }

    @Override
    public ConditionFacade toFacade(PriceCondition conditionFacade) {
        return null;
    }

    @Override
    public ConditionFacade toFacade(AmountOfItemCondition conditionFacade) {
        return new AmountOfItemConditionFacade ( conditionFacade.getAmountNeeded (), conditionFacade.getItemNeeded () );
    }

    @Override
    public ConditionFacade toFacade(AndCompositeCondition condition) {
        return null;
    }

    @Override
    public ConditionFacade toFacade(OrCompositeCondition condition) {
        return null;
    }

    @Override
    public ConditionFacade toFacade(Condition condition) {
        return condition.visitToFacade ( this );
    }
}

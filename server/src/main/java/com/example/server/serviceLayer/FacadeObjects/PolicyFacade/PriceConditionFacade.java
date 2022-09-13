package com.example.server.serviceLayer.FacadeObjects.PolicyFacade;

import com.example.server.businessLayer.Market.Policies.DiscountPolicy.Condition.AmountOfItemCondition;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.Condition.CompositionCondition.AndCompositeCondition;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.Condition.CompositionCondition.OrCompositeCondition;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.Condition.Condition;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.Condition.PriceCondition;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.serviceLayer.FacadeObjects.PolicyFacade.ConditionFacade;

public class PriceConditionFacade extends ConditionFacade {
    private double price;

    public PriceConditionFacade(double price) {
        this.price = price;
    }

    public PriceConditionFacade(){}

    public double getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public Condition toBusinessObject() throws MarketException {
        return new PriceCondition ( price );
    }

    @Override
    public ConditionFacade toFacade(PriceCondition condition) {
        return new PriceConditionFacade ( condition.getPriceNeeded () );
    }

    @Override
    public ConditionFacade toFacade(AmountOfItemCondition condition) {
        return null;
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

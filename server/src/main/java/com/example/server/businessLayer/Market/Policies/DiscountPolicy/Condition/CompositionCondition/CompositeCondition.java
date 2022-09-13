package com.example.server.businessLayer.Market.Policies.DiscountPolicy.Condition.CompositionCondition;

import com.example.server.businessLayer.Market.Policies.DiscountPolicy.Condition.Condition;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.businessLayer.Market.ShoppingBasket;

import java.util.List;

public abstract class CompositeCondition extends Condition {

    List<Condition> conditions;

    public CompositeCondition(List<Condition> conditions) {
        this.conditions = conditions;
    }

    @Override
    public abstract boolean isDiscountHeld(ShoppingBasket shoppingBasket) throws MarketException;

    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }
}

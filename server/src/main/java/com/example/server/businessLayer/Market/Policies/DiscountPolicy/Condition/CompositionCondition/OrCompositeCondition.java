package com.example.server.businessLayer.Market.Policies.DiscountPolicy.Condition.CompositionCondition;

import com.example.server.businessLayer.Market.Policies.DiscountPolicy.Condition.Condition;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.businessLayer.Market.ShoppingBasket;
import com.example.server.serviceLayer.FacadeObjects.PolicyFacade.*;

import java.util.List;

public class OrCompositeCondition extends CompositeCondition{

    public OrCompositeCondition(List<Condition> conditions) {
        super ( conditions );
    }

    @Override
    public boolean isDiscountHeld(ShoppingBasket shoppingBasket) throws MarketException {
        for ( Condition condition: conditions )
            if(condition.isDiscountHeld ( shoppingBasket ))
                return true;
        return false;
    }

    @Override
    public boolean equals(Object object) {
        if(object instanceof OrCompositeCondition){
            OrCompositeCondition toCompare = (OrCompositeCondition) object;
            for( Condition condition: this.conditions){
                if (!toCompare.conditions.contains ( condition ))
                    return false;
            }
            for( Condition condition: toCompare.conditions){
                if ( !this.conditions.contains ( condition ))
                    return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean isOr(){
        return true;
    }

    @Override
    public ConditionFacade visitToFacade(AmountOfItemConditionFacade conditionFacade) {
        return null;
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
        return conditionFacade.toFacade ( this );
    }
}

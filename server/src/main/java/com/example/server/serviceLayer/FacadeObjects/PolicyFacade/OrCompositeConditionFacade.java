package com.example.server.serviceLayer.FacadeObjects.PolicyFacade;

import com.example.server.businessLayer.Market.Policies.DiscountPolicy.Condition.AmountOfItemCondition;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.Condition.CompositionCondition.AndCompositeCondition;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.Condition.CompositionCondition.OrCompositeCondition;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.Condition.Condition;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.Condition.PriceCondition;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;

import java.util.ArrayList;
import java.util.List;

public class OrCompositeConditionFacade extends CompositeConditionFacade{

    public OrCompositeConditionFacade(List<ConditionFacade> conditionFacadeList) {
        super ( conditionFacadeList );
    }

    public OrCompositeConditionFacade(){}
    @Override
    public Condition toBusinessObject() throws MarketException {
        List<Condition> conditions = new ArrayList<> (  );
        for(ConditionFacade conditionFacade: conditionFacadeList)
            conditions.add ( conditionFacade.toBusinessObject () );
        return new OrCompositeCondition ( conditions );
    }

    @Override
    public ConditionFacade toFacade(PriceCondition condition) {
        return null;
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
        List<Condition> conditions = condition.getConditions ();
        List<ConditionFacade> conditionFacades = new ArrayList<> (  );
        for(Condition cur : conditions)
            conditionFacades.add ( getConditionFacade ( cur ) );
        return new OrCompositeConditionFacade ( conditionFacades );
    }

    @Override
    public ConditionFacade toFacade(Condition condition) {
        return condition.visitToFacade ( this );
    }
}

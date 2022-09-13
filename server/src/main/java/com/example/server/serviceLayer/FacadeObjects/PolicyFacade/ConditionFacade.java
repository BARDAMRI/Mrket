package com.example.server.serviceLayer.FacadeObjects.PolicyFacade;

import com.example.server.businessLayer.Market.Policies.DiscountPolicy.Condition.AmountOfItemCondition;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.Condition.CompositionCondition.AndCompositeCondition;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.Condition.CompositionCondition.OrCompositeCondition;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.Condition.Condition;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.Condition.PriceCondition;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.serviceLayer.FacadeObjects.FacadeObject;

public abstract class ConditionFacade implements FacadeObject<Condition> {
    public abstract ConditionFacade toFacade(PriceCondition condition);

    public abstract ConditionFacade toFacade(AmountOfItemCondition condition);

    public abstract ConditionFacade toFacade(AndCompositeCondition condition);

    public abstract ConditionFacade toFacade(OrCompositeCondition condition);

    public abstract ConditionFacade toFacade(Condition condition);

    protected ConditionFacade getConditionFacade(Condition condition){
        ConditionFacade conditionFacade;
        if(condition.isAnd ()){
            conditionFacade = new AndCompositeConditionFacade (  );
        } else if(condition.isOr ()){
            conditionFacade = new OrCompositeConditionFacade (  );
        }else if(condition.isAmountOfItem ()){
            conditionFacade = new AmountOfItemConditionFacade (  );
        }else{
            conditionFacade = new PriceConditionFacade (  );
        }
        conditionFacade = conditionFacade.toFacade ( condition );
        return conditionFacade;
    }

}

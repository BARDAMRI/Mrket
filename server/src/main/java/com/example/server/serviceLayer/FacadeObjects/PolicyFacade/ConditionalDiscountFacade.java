package com.example.server.serviceLayer.FacadeObjects.PolicyFacade;

import com.example.server.businessLayer.Market.Policies.DiscountPolicy.CompositeDiscount.MaxCompositeDiscount;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.Condition.Condition;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.ConditionalDiscount;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.DiscountState.DiscountLevelState;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.DiscountType;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.SimpleDiscount;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;

public class ConditionalDiscountFacade extends DiscountTypeFacade {
    ConditionFacade conditionFacade;

    public ConditionalDiscountFacade(double percentageOfDiscount, DiscountLevelStateFacade discountLevelState, ConditionFacade conditionFacade) {
        super (percentageOfDiscount, discountLevelState );
        this.conditionFacade = conditionFacade;
    }

    public ConditionalDiscountFacade(){}

    @Override
    public DiscountTypeFacade toFacade(SimpleDiscount discount) {
        return null;
    }

    @Override
    public DiscountTypeFacade toFacade(ConditionalDiscount discount) {
        DiscountLevelState discountLevelState = discount.getDiscountLevelState ();
        DiscountLevelStateFacade discountLevelStateFacade = getDiscountLevelStateFacade ( discountLevelState );
        Condition condition = discount.getCondition ();
        ConditionFacade conditionFacade = getConditionFacade ( condition );
        return new ConditionalDiscountFacade ( discount.getPercentageOfDiscount (), discountLevelStateFacade, conditionFacade );
    }

    @Override
    public DiscountTypeFacade toFacade(MaxCompositeDiscount discount) {
        return null;
    }

    @Override
    public DiscountTypeFacade toFacade(DiscountType discount) {
        return null;
    }

    public ConditionFacade getConditionFacade() {
        return conditionFacade;
    }

    public void setConditionFacade(ConditionFacade conditionFacade) {
        this.conditionFacade = conditionFacade;
    }

    @Override
    public DiscountType toBusinessObject() throws MarketException {
        return new ConditionalDiscount (percentageOfDiscount, discountLevelState.toBusinessObject (),conditionFacade.toBusinessObject () );
    }
}

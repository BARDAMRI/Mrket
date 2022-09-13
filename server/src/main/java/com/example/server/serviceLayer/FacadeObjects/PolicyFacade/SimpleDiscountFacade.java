package com.example.server.serviceLayer.FacadeObjects.PolicyFacade;

import com.example.server.businessLayer.Market.Policies.DiscountPolicy.CompositeDiscount.MaxCompositeDiscount;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.ConditionalDiscount;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.DiscountState.DiscountLevelState;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.DiscountType;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.SimpleDiscount;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;

public class SimpleDiscountFacade extends DiscountTypeFacade {
    public SimpleDiscountFacade(double percentageOfDiscount, DiscountLevelStateFacade discountLevelState) {
        super (percentageOfDiscount, discountLevelState );
    }

    public SimpleDiscountFacade(){}

    @Override
    public DiscountTypeFacade toFacade(SimpleDiscount discount) {
        DiscountLevelState discountLevelState = discount.getDiscountLevelState ();
        DiscountLevelStateFacade discountLevelStateFacade = getDiscountLevelStateFacade ( discountLevelState );
        return new SimpleDiscountFacade ( discount.getPercentageOfDiscount (), discountLevelStateFacade );
    }

    @Override
    public DiscountTypeFacade toFacade(ConditionalDiscount discount) {
        return null;
    }

    @Override
    public DiscountTypeFacade toFacade(MaxCompositeDiscount discount) {
        return null;
    }

    @Override
    public DiscountTypeFacade toFacade(DiscountType discount) {
        return discount.visitToFacade ( this );
    }

    @Override
    public DiscountType toBusinessObject() throws MarketException {
        return new SimpleDiscount ( percentageOfDiscount, discountLevelState.toBusinessObject () );
    }
}

package com.example.server.serviceLayer.FacadeObjects.PolicyFacade;

import com.example.server.businessLayer.Market.Policies.DiscountPolicy.CompositeDiscount.MaxCompositeDiscount;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.ConditionalDiscount;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.DiscountType;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.SimpleDiscount;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;

import java.util.ArrayList;
import java.util.List;

public class MaxCompositeDiscountTypeFacade extends CompositeDiscountTypeFacade {
    public MaxCompositeDiscountTypeFacade(List<DiscountTypeFacade> discountTypes) {
        super (0, new ShopLevelStateFacade (), discountTypes );
    }

    public MaxCompositeDiscountTypeFacade(){}

    @Override
    public DiscountTypeFacade toFacade(SimpleDiscount discount) {
        return null;
    }

    @Override
    public DiscountTypeFacade toFacade(ConditionalDiscount discount) {
        return null;
    }

    @Override
    public DiscountTypeFacade toFacade(MaxCompositeDiscount discount) {
        List<DiscountTypeFacade> discountTypeFacades = new ArrayList<> (  );
        List<DiscountType> discountTypes = discount.getDiscountTypes ();
        for(DiscountType discountType : discountTypes)
            discountTypeFacades.add ( getDiscountType ( discountType ) );
        return new MaxCompositeDiscountTypeFacade (discountTypeFacades );
    }

    @Override
    public DiscountTypeFacade toFacade(DiscountType discount) {
        return null;
    }

    @Override
    public DiscountType toBusinessObject() throws MarketException {
        List<DiscountType> discountTypeList = new ArrayList<> (  );
        for(DiscountTypeFacade discountTypeFacade: discountTypes)
            discountTypeList.add ( discountTypeFacade.toBusinessObject () );
        return new MaxCompositeDiscount ( discountTypeList );
    }
}

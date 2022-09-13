package com.example.server.serviceLayer.FacadeObjects.PolicyFacade;

import java.util.List;

public abstract class CompositeDiscountTypeFacade extends DiscountTypeFacade{
    protected List<DiscountTypeFacade> discountTypes;

    public CompositeDiscountTypeFacade(int percentageOfDiscount, DiscountLevelStateFacade discountLevelState, List<DiscountTypeFacade> discountTypes) {
        super (percentageOfDiscount, discountLevelState );
        this.discountTypes = discountTypes;
    }

    public CompositeDiscountTypeFacade(){}

    public List<DiscountTypeFacade> getDiscountTypes() {
        return discountTypes;
    }

    public void setDiscountTypes(List<DiscountTypeFacade> discountTypes) {
        this.discountTypes = discountTypes;
    }
}

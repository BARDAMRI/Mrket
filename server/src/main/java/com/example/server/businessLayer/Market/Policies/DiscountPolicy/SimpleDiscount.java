package com.example.server.businessLayer.Market.Policies.DiscountPolicy;

import com.example.server.businessLayer.Market.Policies.DiscountPolicy.DiscountState.DiscountLevelState;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.businessLayer.Market.ShoppingBasket;
import com.example.server.serviceLayer.FacadeObjects.PolicyFacade.ConditionalDiscountFacade;
import com.example.server.serviceLayer.FacadeObjects.PolicyFacade.DiscountTypeFacade;
import com.example.server.serviceLayer.FacadeObjects.PolicyFacade.MaxCompositeDiscountTypeFacade;
import com.example.server.serviceLayer.FacadeObjects.PolicyFacade.SimpleDiscountFacade;

public class SimpleDiscount extends DiscountType{

    public SimpleDiscount(double percentageOfDiscount, DiscountLevelState discountLevelState) throws MarketException {
        super(percentageOfDiscount, discountLevelState);
    }

    public SimpleDiscount() {
    }

    @Override
    public boolean isDiscountHeld(ShoppingBasket shoppingBasket) {
        return true;
    }

    public boolean equals(Object object){
        if(object instanceof SimpleDiscount){
            SimpleDiscount toCompare = (SimpleDiscount) object;
            boolean out =  this.discountLevelState.equals(toCompare.discountLevelState) && this.percentageOfDiscount == toCompare.percentageOfDiscount;
            return out;
        }
        return false;
    }

    @Override
    public boolean isSimple(){
        return true;
    }

    @Override
    public DiscountTypeFacade visitToFacade(SimpleDiscountFacade discountFacade) {
        return discountFacade.toFacade ( this );
    }

    @Override
    public DiscountTypeFacade visitToFacade(ConditionalDiscountFacade discountFacade) {
        return null;
    }

    @Override
    public DiscountTypeFacade visitToFacade(MaxCompositeDiscountTypeFacade discountFacade) {
        return null;
    }
}

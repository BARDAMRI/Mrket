package com.example.server.businessLayer.Market.Policies.DiscountPolicy.DiscountState;

import com.example.server.businessLayer.Market.ShoppingBasket;
import com.example.server.serviceLayer.FacadeObjects.PolicyFacade.*;

public class ShopLevelState extends DiscountLevelState {
    @Override
    public double calculateDiscount(ShoppingBasket shoppingBasket, double percentageOfDiscount) {
        return shoppingBasket.getPrice()*(100-percentageOfDiscount)/100;
    }

    @Override
    public boolean equals(Object object){
        if(object instanceof ShopLevelState) return true;
        return false;
    }

    @Override
    public boolean isShop(){
        return true;
    }

    @Override
    public DiscountLevelStateFacade visitToFacade(CategoryLevelStateFacade levelStateFacade) {
        return null;
    }

    @Override
    public DiscountLevelStateFacade visitToFacade(ItemLevelStateFacade levelStateFacade) {
        return null;
    }

    @Override
    public DiscountLevelStateFacade visitToFacade(ShopLevelStateFacade levelStateFacade) {
        return levelStateFacade.toFacade ( this );
    }

    @Override
    public DiscountLevelStateFacade visitToFacade(AndCompositeDiscountLevelStateFacade levelStateFacade) {
        return null;
    }

    @Override
    public DiscountLevelStateFacade visitToFacade(MaxXorCompositeDiscountLevelStateFacade levelStateFacade) {
        return null;
    }
}

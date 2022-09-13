package com.example.server.businessLayer.Market.Policies.DiscountPolicy.DiscountState;

import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.businessLayer.Market.ShoppingBasket;
import com.example.server.serviceLayer.FacadeObjects.PolicyFacade.*;

public abstract class DiscountLevelState {

    public abstract double calculateDiscount(ShoppingBasket shoppingBasket, double percentageOfDiscount) throws MarketException;
    public abstract boolean equals(Object object);

    public boolean isItem(){
        return false;
    }

    public boolean isCategory(){
        return false;
    }

    public boolean isShop(){
        return false;
    }

    public boolean isAnd(){
        return false;
    }

    public boolean isMaxXor(){
        return false;
    }

    public abstract DiscountLevelStateFacade visitToFacade(CategoryLevelStateFacade levelStateFacade);
    public abstract  DiscountLevelStateFacade visitToFacade(ItemLevelStateFacade levelStateFacade);
    public abstract DiscountLevelStateFacade visitToFacade(ShopLevelStateFacade levelStateFacade);
    public abstract DiscountLevelStateFacade visitToFacade(AndCompositeDiscountLevelStateFacade levelStateFacade);
    public abstract DiscountLevelStateFacade visitToFacade(MaxXorCompositeDiscountLevelStateFacade levelStateFacade);
}

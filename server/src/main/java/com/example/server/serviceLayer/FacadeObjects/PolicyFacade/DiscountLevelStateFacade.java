package com.example.server.serviceLayer.FacadeObjects.PolicyFacade;

import com.example.server.businessLayer.Market.Policies.DiscountPolicy.Condition.AmountOfItemCondition;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.Condition.Condition;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.Condition.PriceCondition;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.DiscountState.*;
import com.example.server.businessLayer.Market.Policies.PurchasePolicy.PurchasePolicyState.OrCompositePurchasePolicyLevelState;
import com.example.server.businessLayer.Market.Policies.PurchasePolicy.PurchasePolicyState.XorCompositePurchasePolicyLevelState;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.serviceLayer.FacadeObjects.FacadeObject;

public abstract class DiscountLevelStateFacade implements FacadeObject {

    @Override
    public abstract DiscountLevelState toBusinessObject() throws MarketException;

    public abstract DiscountLevelStateFacade toFacade(ItemLevelState itemLevelState);

    public abstract DiscountLevelStateFacade toFacade(CategoryLevelState categoryLevelState);

    public abstract DiscountLevelStateFacade toFacade(ShopLevelState shopLevelState);

    public abstract DiscountLevelStateFacade toFacade(AndCompositeDiscountLevelState itemLevelState);

    public abstract DiscountLevelStateFacade toFacade(MaxXorCompositeDiscountLevelState shopLevelState);

    public abstract DiscountLevelStateFacade toFacade(DiscountLevelState discountLevelState);

    protected DiscountLevelStateFacade getDiscountLevelStateFacade(DiscountLevelState discountLevelState){
        DiscountLevelStateFacade discountLevelStateFacade;
        if(discountLevelState.isAnd ()){
            discountLevelStateFacade = new AndCompositeDiscountLevelStateFacade (  );
        } else if(discountLevelState.isMaxXor ()){
            discountLevelStateFacade = new MaxXorCompositeDiscountLevelStateFacade (  );
        }else if(discountLevelState.isItem ()){
            discountLevelStateFacade = new ItemLevelStateFacade (  );
        }else if(discountLevelState.isCategory ()){
            discountLevelStateFacade = new CategoryLevelStateFacade (  );
        }else{
            discountLevelStateFacade = new ShopLevelStateFacade ();
        }
        discountLevelStateFacade = discountLevelStateFacade.toFacade ( discountLevelState );
        return discountLevelStateFacade;
    }
}

package com.example.server.serviceLayer.FacadeObjects.PolicyFacade;

import com.example.server.businessLayer.Market.Item;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.DiscountState.*;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;

public class CategoryLevelStateFacade extends DiscountLevelStateFacade {
    Item.Category category;

    public CategoryLevelStateFacade(Item.Category category) {
        this.category = category;
    }

    public CategoryLevelStateFacade(){}

    public Item.Category getCategory() {
        return category;
    }

    public void setCategory(Item.Category category) {
        this.category = category;
    }

    @Override
    public DiscountLevelState toBusinessObject() throws MarketException {
        return new CategoryLevelState ( category );
    }

    @Override
    public DiscountLevelStateFacade toFacade(ItemLevelState itemLevelState) {
        return null;
    }

    @Override
    public DiscountLevelStateFacade toFacade(CategoryLevelState categoryLevelState) {
        return new CategoryLevelStateFacade ( categoryLevelState.getCategory () );
    }

    @Override
    public DiscountLevelStateFacade toFacade(ShopLevelState shopLevelState) {
        return null;
    }

    @Override
    public DiscountLevelStateFacade toFacade(AndCompositeDiscountLevelState itemLevelState) {
        return null;
    }

    @Override
    public DiscountLevelStateFacade toFacade(MaxXorCompositeDiscountLevelState shopLevelState) {
        return null;
    }

    @Override
    public DiscountLevelStateFacade toFacade(DiscountLevelState discountLevelState) {
        return discountLevelState.visitToFacade ( this );
    }
}

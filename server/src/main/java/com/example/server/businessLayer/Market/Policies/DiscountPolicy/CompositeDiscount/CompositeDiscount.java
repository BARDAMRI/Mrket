package com.example.server.businessLayer.Market.Policies.DiscountPolicy.CompositeDiscount;

import com.example.server.businessLayer.Market.Policies.DiscountPolicy.DiscountType;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.businessLayer.Market.ShoppingBasket;

import java.util.ArrayList;
import java.util.List;

public abstract class CompositeDiscount extends DiscountType{
    protected List<DiscountType> discountTypes;

    public CompositeDiscount(List<DiscountType> discountTypes) {
        this.discountTypes = discountTypes;
    }

    public double calculateDiscount(ShoppingBasket shoppingBasket) throws MarketException {
        List<Double> discounts = new ArrayList<> (  );
        Double price = shoppingBasket.getPrice();
        for(DiscountType discountType : discountTypes){
            Double curPrice = discountType.calculateDiscount(shoppingBasket);
            discounts.add ( price - curPrice );
        }
        return calculateAllDiscount(price, discounts);
    }

    protected abstract Double calculateAllDiscount(double price, List<Double> discounts) throws MarketException;

    @Override
    public boolean isDiscountHeld(ShoppingBasket shoppingBasket) throws MarketException {
        for(DiscountType discountType : discountTypes)
            if(discountType.isDiscountHeld ( shoppingBasket ))
                return true;
        return false;
    }

    public boolean containsDiscount(DiscountType discountType){
        if(this.equals ( discountType ))
            return true;
        if(discountType instanceof CompositeDiscount){
            CompositeDiscount toCheck = (CompositeDiscount) discountType;
            for ( DiscountType curr : ((CompositeDiscount) discountType).discountTypes ) {
                if(curr.equals ( toCheck ))
                    return true;
                if(curr instanceof CompositeDiscount && ((CompositeDiscount) curr).containsDiscount ( toCheck ))
                    return true;
            }
        }
        return false;
    }

    public void removeDiscount(DiscountType discountType){
        if(discountType instanceof CompositeDiscount){
            CompositeDiscount toCheck = (CompositeDiscount) discountType;
            for ( DiscountType curr : ((CompositeDiscount) discountType).discountTypes ) {
                if(curr.equals ( toCheck )) {
                    discountTypes.remove ( curr );
                    return;
                }
                if(curr instanceof CompositeDiscount)
                    ((CompositeDiscount) curr).removeDiscount ( toCheck );
            }
        }
    }

    public List<DiscountType> getDiscountTypes() {
        return discountTypes;
    }

    public void setDiscountTypes(List<DiscountType> discountTypes) {
        this.discountTypes = discountTypes;
    }
}

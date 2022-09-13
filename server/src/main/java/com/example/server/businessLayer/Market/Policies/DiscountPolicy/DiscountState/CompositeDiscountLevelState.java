package com.example.server.businessLayer.Market.Policies.DiscountPolicy.DiscountState;

import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.businessLayer.Market.ShoppingBasket;

import java.util.ArrayList;
import java.util.List;

public abstract class CompositeDiscountLevelState extends DiscountLevelState {

    protected List<DiscountLevelState> discountLevelStates;

    public CompositeDiscountLevelState(List<DiscountLevelState> discountLevelStates) {
        this.discountLevelStates = discountLevelStates;
    }
    @Override
    public double calculateDiscount(ShoppingBasket shoppingBasket, double percentageOfDiscount) throws MarketException {
        double price = shoppingBasket.getPrice ();
        List<Double> discounts = new ArrayList<> (  );
        for(DiscountLevelState discountLevelState: discountLevelStates){
            double priceAfterDiscount = discountLevelState.calculateDiscount ( shoppingBasket, percentageOfDiscount );
            discounts.add (price - priceAfterDiscount);
        }
        return calculateAllDiscount ( price, discounts );
    }

    protected abstract Double calculateAllDiscount(double price, List<Double> discounts) throws MarketException;

    public abstract boolean equals(Object object);

    public List<DiscountLevelState> getDiscountLevelStates() {
        return discountLevelStates;
    }

    public void setDiscountLevelStates(List<DiscountLevelState> discountLevelStates) {
        this.discountLevelStates = discountLevelStates;
    }
}

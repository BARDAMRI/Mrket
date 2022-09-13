package com.example.server.businessLayer.Market.Policies.DiscountPolicy;

import com.example.server.businessLayer.Market.Policies.DiscountPolicy.CompositeDiscount.CompositeDiscount;
import com.example.server.businessLayer.Market.ResourcesObjects.DebugLog;
import com.example.server.businessLayer.Market.ResourcesObjects.EventLog;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.businessLayer.Market.ShoppingBasket;

import java.util.ArrayList;
import java.util.List;

public class DiscountPolicy {
    private List<DiscountType> validDiscounts;

    public DiscountPolicy() {
        this.validDiscounts = new ArrayList<>();
    }

    public double calculateDiscount(ShoppingBasket shoppingBasket) throws MarketException {
        double price = shoppingBasket.getPrice();
        double priceAfterDiscount = price;
        for (DiscountType discountType : validDiscounts) {
            double curPrice = discountType.calculateDiscount(shoppingBasket);
            priceAfterDiscount -= (price - curPrice);//x-= y => x= x-y
        }
        return Math.max(0,priceAfterDiscount);
    }

    public void addNewDiscount(DiscountType discountType) throws MarketException {
        if (discountType == null) {
            DebugLog.getInstance().Log("Tried to add an invalid (null) discount.");
            throw new MarketException("discount is null");
        }
        if (validDiscounts.contains(discountType)) {
            DebugLog.getInstance().Log("Tried to add a discount that already exist");
            throw new MarketException("discount is already exist in the shop");
        }
        if (discountType instanceof CompositeDiscount) {
            pureDiscounts(discountType);
        }
        validDiscounts.add(discountType);
    }

    private void pureDiscounts(DiscountType discount) {
        if (discount instanceof CompositeDiscount) {
            for (DiscountType discountType : ((CompositeDiscount) discount).getDiscountTypes()) {
                if (validDiscounts.contains(discountType)) {
                    validDiscounts.remove(discountType);
                } else if (discountType instanceof CompositeDiscount) {
                    pureDiscounts(discountType);
                }
            }
        } else if (validDiscounts.contains(discount))
            validDiscounts.remove(discount);
    }

    public void removeDiscount(DiscountType discountType) {
        if (validDiscounts.contains(discountType))
            validDiscounts.remove(discountType);
    }

    public List<DiscountType> getValidDiscounts() {
        return validDiscounts;
    }

    public void setValidDiscounts(List<DiscountType> validDiscounts) {
        this.validDiscounts = validDiscounts;
    }
}

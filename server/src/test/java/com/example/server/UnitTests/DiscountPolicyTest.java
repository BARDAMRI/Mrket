package com.example.server.UnitTests;

import com.example.server.businessLayer.Market.Policies.DiscountPolicy.ConditionalDiscount;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.DiscountPolicy;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.SimpleDiscount;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.businessLayer.Market.ShoppingBasket;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

public class DiscountPolicyTest {
    DiscountPolicy discountPolicy;
    @Mock
    SimpleDiscount simpleDiscount;
    @Mock
    ConditionalDiscount conditionalDiscount;
    @Mock
    ShoppingBasket shoppingBasket;

    @BeforeEach
    public void reset(){
        conditionalDiscount = Mockito.mock(ConditionalDiscount.class);
        simpleDiscount = Mockito.mock(SimpleDiscount.class);
        shoppingBasket=Mockito.mock(ShoppingBasket.class);
        discountPolicy = new DiscountPolicy();
    }
    @Test
    @DisplayName("Add discount - good test - simple discount")
    public void addSimpleDiscountTest(){
        try {
            discountPolicy.addNewDiscount(simpleDiscount);
            Assertions.assertEquals(1,discountPolicy.getValidDiscounts().size());
        } catch (MarketException e) {
            assert false;
        }
    }
    @Test
    @DisplayName("Add discount - good test - conditional discount")
    public void addConditionalDiscountTest(){
        try {
            discountPolicy.addNewDiscount(conditionalDiscount);
            Assertions.assertEquals(1,discountPolicy.getValidDiscounts().size());
        } catch (MarketException e) {
            assert false;
        }
    }
    @Test
    @DisplayName("Add both discount types - good test")
    public void addBothDiscountTypes(){
        try {
            discountPolicy.addNewDiscount(simpleDiscount);
            discountPolicy.addNewDiscount(conditionalDiscount);
            Assertions.assertEquals(2, discountPolicy.getValidDiscounts().size());
        } catch (MarketException e) {
            assert false;
        }
    }
    @Test
    @DisplayName("Add discount - fail test - null discount")
    public void addNullDiscount(){
        try {
            discountPolicy.addNewDiscount(null);
            assert false;
        } catch (MarketException e) {
            assert true;
        }
    }
    @Test
    @DisplayName("Add the same discount twice")
    public void addDiscountTwice(){
        try {
            discountPolicy.addNewDiscount(simpleDiscount);
        } catch (MarketException e) {
            assert false;
        }
        try {
            discountPolicy.addNewDiscount(simpleDiscount);
            assert false;
        } catch (MarketException e) {
            assert true;
        }
    }
    @Test
    @DisplayName("Remove Discount - good test")
    public void removeDiscountTest(){
        try {
            discountPolicy.addNewDiscount(simpleDiscount);
        } catch (MarketException e) {
            assert false;
        }
        discountPolicy.removeDiscount(simpleDiscount);
        Assertions.assertEquals(0, discountPolicy.getValidDiscounts().size());
    }
    @Test
    @DisplayName("Remove discount - good test - nothing changed after removing non existing discount")
    public void removeNonExitingDiscount(){
        try {
            discountPolicy.addNewDiscount(simpleDiscount);
        } catch (MarketException e) {
            assert false;
        }
        discountPolicy.removeDiscount(conditionalDiscount);
        Assertions.assertEquals(1, discountPolicy.getValidDiscounts().size());
    }
    @Test
    @DisplayName("Calculate Discount - Nothing changed")
    public void CalculateDiscountUnchangedPrice(){
        try {
            discountPolicy.addNewDiscount(simpleDiscount);
            discountPolicy.addNewDiscount(conditionalDiscount);
        } catch (MarketException e) {
            assert false;
        }
        Mockito.when(shoppingBasket.getPrice()).thenReturn(100.0);
        try {
            Mockito.when(simpleDiscount.calculateDiscount(shoppingBasket)).thenReturn(100.0);
            Mockito.when(conditionalDiscount.calculateDiscount(shoppingBasket)).thenReturn(100.0);
        } catch (MarketException e) {
            assert false;
        }
        try {
            Assertions.assertEquals(100.0, discountPolicy.calculateDiscount(shoppingBasket));
        } catch (MarketException e) {
            assert false;
        }
    }
    @Test
    @DisplayName("Calculate Discount - price zero")
    public void CalculateDiscountPriceZero(){
        try {
            discountPolicy.addNewDiscount(simpleDiscount);
            discountPolicy.addNewDiscount(conditionalDiscount);
        } catch (MarketException e) {
            assert false;
        }
        Mockito.when(shoppingBasket.getPrice()).thenReturn(100.0);
        try {
            Mockito.when(simpleDiscount.calculateDiscount(shoppingBasket)).thenReturn(0.0);
            Mockito.when(conditionalDiscount.calculateDiscount(shoppingBasket)).thenReturn(0.0);
        } catch (MarketException e) {
            assert false;
        }
        try {
            Assertions.assertEquals(0.0, discountPolicy.calculateDiscount(shoppingBasket));
        } catch (MarketException e) {
            assert false;
        }
    }
    @Test
    @DisplayName("Calculate Discount - Price changed")
    public void CalculateDiscountChangedPrice(){
        Mockito.when(shoppingBasket.getPrice()).thenReturn(100.0);

        try {
            discountPolicy.addNewDiscount(simpleDiscount);
            discountPolicy.addNewDiscount(conditionalDiscount);
        } catch (MarketException e) {
            assert false;
        }
        try {
            Mockito.when(simpleDiscount.calculateDiscount(shoppingBasket)).thenReturn(90.0);
            Mockito.when(conditionalDiscount.calculateDiscount(shoppingBasket)).thenReturn(85.0);
        } catch (MarketException e) {
            assert false;
        }
        try {
            Assertions.assertEquals(75.0, discountPolicy.calculateDiscount(shoppingBasket));
        } catch (MarketException e) {
            assert false;
        }
    }

}

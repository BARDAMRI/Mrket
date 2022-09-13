package com.example.server.UnitTests;

import com.example.server.businessLayer.Market.Policies.PurchasePolicy.AtLeastPurchasePolicyType;
import com.example.server.businessLayer.Market.Policies.PurchasePolicy.AtMostPurchasePolicyType;
import com.example.server.businessLayer.Market.Policies.PurchasePolicy.OrCompositePurchasePolicyType;
import com.example.server.businessLayer.Market.Policies.PurchasePolicy.PurchasePolicy;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.businessLayer.Market.ShoppingBasket;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

public class PurchasePolicyTest {
    PurchasePolicy policy;
    @Mock
    AtLeastPurchasePolicyType atLeast;
    @Mock
    AtMostPurchasePolicyType atMost;
    @Mock
    OrCompositePurchasePolicyType orPurchase;
    @Mock
    ShoppingBasket basket;

    @BeforeEach
    public void reset(){
        basket = Mockito.mock(ShoppingBasket.class);
        atLeast = Mockito.mock(AtLeastPurchasePolicyType.class);
        atMost = Mockito.mock(AtMostPurchasePolicyType.class);
        orPurchase = Mockito.mock(OrCompositePurchasePolicyType.class);
        policy = new PurchasePolicy();
    }
    @Test
    @DisplayName("Add new purchase policy - good test - Type: at Least ")
    public void addPolicyAtLeastType(){
        try {
            policy.addNewPurchasePolicy(atLeast);
        } catch (MarketException e) {
            assert false;
        }
        Assertions.assertEquals(1,policy.getValidPurchasePolicies().size());

    }

    @Test
    @DisplayName("Add new purchase policy - good test - Type: at Most ")
    public void addPolicyAtMostType(){
        try {
            policy.addNewPurchasePolicy(atMost);
        } catch (MarketException e) {
            assert false;
        }
        Assertions.assertEquals(1,policy.getValidPurchasePolicies().size());
    }

    @Test
    @DisplayName("Add new purchase policy - good test - Type: or ")
    public void addPolicyOrType(){

        try {
            policy.addNewPurchasePolicy(orPurchase);
        } catch (MarketException e) {
            assert false;
        }
        Assertions.assertEquals(1,policy.getValidPurchasePolicies().size());
    }

    @Test
    @DisplayName("Add new purchase policy - good test - all types ")
    public void addPolicyAllTypes(){

        try {
            policy.addNewPurchasePolicy(atLeast);
            policy.addNewPurchasePolicy(atMost);
            policy.addNewPurchasePolicy(orPurchase);
        } catch (MarketException e) {
            assert false;
        }
        Assertions.assertEquals(3,policy.getValidPurchasePolicies().size());
    }
    @Test
    @DisplayName("Add purhase policy - fail test - null policy")
    public void addNullPolicyFailTest(){
        try {
            policy.addNewPurchasePolicy(null);
            assert false;
        } catch (MarketException e) {
            assert true;
        }
    }
    @Test
    @DisplayName("Add policy - fail test - add the same policy twice")
    public void addPolicyTwiceFailTest(){
        try {
            policy.addNewPurchasePolicy(atMost);
        }
        catch (MarketException e)
        {
            assert false;
        }
        try {
            policy.addNewPurchasePolicy(atMost);
            assert false;
        }
        catch (MarketException e)
        {
            assert true;
        }
    }
    @Test
    @DisplayName("Is policy held - all held")
    public void isPolicyHeldTestAllHeld(){
        try {
            Mockito.when(atLeast.isPolicyHeld(basket)).thenReturn(true);
            Mockito.when(atMost.isPolicyHeld(basket)).thenReturn(true);
        } catch (MarketException e) {
            assert false;
        }
        try {
            policy.addNewPurchasePolicy(atMost);
            policy.addNewPurchasePolicy(atLeast);
        } catch (MarketException e) {
            assert false;
        }
        try {
            Assertions.assertTrue(policy.isPoliciesHeld(basket));
        } catch (MarketException e) {
            assert false;
        }
    }
    @Test
    @DisplayName("Is policy held - Some held")
    public void isPolicyHeldTestSomeHeld(){
        try {
            Mockito.when(atLeast.isPolicyHeld(basket)).thenReturn(true);
            Mockito.when(atMost.isPolicyHeld(basket)).thenReturn(false);
        } catch (MarketException e) {
            assert false;
        }
        try {
            policy.addNewPurchasePolicy(atMost);
            policy.addNewPurchasePolicy(atLeast);
        } catch (MarketException e) {
            assert false;
        }
        try {
            Assertions.assertFalse(policy.isPoliciesHeld(basket));
        } catch (MarketException e) {
            assert false;
        }
    }
    @Test
    @DisplayName("Is policy held - Nothing  held")
    public void isPolicyHeldTestNothingHeld(){
        try {
            Mockito.when(atLeast.isPolicyHeld(basket)).thenReturn(false);
            Mockito.when(atMost.isPolicyHeld(basket)).thenReturn(false);
        } catch (MarketException e) {
            assert false;
        }
        try {
            policy.addNewPurchasePolicy(atMost);
            policy.addNewPurchasePolicy(atLeast);
        } catch (MarketException e) {
            assert false;
        }
        try {
            Assertions.assertFalse(policy.isPoliciesHeld(basket));
        } catch (MarketException e) {
            assert false;
        }
    }

}

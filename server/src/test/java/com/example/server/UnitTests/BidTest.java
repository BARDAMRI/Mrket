package com.example.server.UnitTests;

import com.example.server.businessLayer.Market.Bid;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BidTest {
    Bid bid;

    @BeforeEach
    public void reset(){
        List<String> shopOwners = new ArrayList<>();
        shopOwners.add("ayala");
        shopOwners.add("ido");
         bid = new Bid("raz",true ,1,5.0,2.0,shopOwners);
    }

    @Test
    @DisplayName("Add approves - good test")
    public void addApprovesGoodTest(){
        bid.addApproves("shaked");
        Assertions.assertEquals(3,bid.getShopOwnersStatus().size());
        Assertions.assertFalse(bid.isApproved());
    }
    @Test
    @DisplayName("Add approves - add owner who exist already")
    public void addApprovesDupNameTest(){
        bid.addApproves("ayala");
        Assertions.assertEquals(2,bid.getShopOwnersStatus().size());
    }
    @Test
    @DisplayName("Remove approves - good test - approved = true")
    public void removeApprovesGoodTestApprovedTrue(){
        try {
            bid.removeApproves("ayala");
        } catch (MarketException e) {
            assert false;
        }
        Assertions.assertEquals(1,bid.getShopOwnersStatus().size());
        try {
            bid.approveBid("ido");
        } catch (MarketException e) {
            assert false;
        }
        Assertions.assertTrue(bid.isApproved());
    }

    @Test
    @DisplayName("Remove approves - good test - approved = false")
    public void removeApprovesApproveFalseTest(){
        Assertions.assertFalse(bid.isApproved());
        try {
            bid.approveBid("ido");
        } catch (MarketException e) {
            assert false;
        }
        Assertions.assertFalse(bid.isApproved());
    }

    @Test
    @DisplayName("Remove approves - good test")
    public void removeApprovesSideBuyer(){
        bid.setSideNeedToApprove(Bid.Side.buyer);
        try {
            bid.approveBid("ido");
        } catch (MarketException e) {
            assert false;
        }
        Assertions.assertFalse(bid.isApproved());
        try {
            bid.approveBid("ayala");
        } catch (MarketException e) {
            assert false;
        }
        Assertions.assertFalse(bid.isApproved());
    }
    @Test
    @DisplayName("Approve bid  - good test - buyer needs to approve")
    public void approveBidBuyerNeedsToApproveTest(){
        bid.setSideNeedToApprove(Bid.Side.buyer);
        try {
            Assertions.assertTrue(bid.approveBid("raz"));
        } catch (MarketException e) {
            assert false;
        }
    }

    @Test
    @DisplayName("Approve bid  - good test - seller needs to approve")
    public void approveBidSellerNeedsToApproveTest(){
        try {
            Assertions.assertFalse(bid.approveBid("ido"));
            Assertions.assertTrue(bid.approveBid("ayala"));
        } catch (MarketException e) {
            assert false;
        }
    }
    @Test
    @DisplayName("Approve bid  - fail test - buyer needs to approve , invalid name")
    public void approveBidBuyerApproveInvalidNameTest(){
        bid.setSideNeedToApprove(Bid.Side.buyer);
        try {
            bid.approveBid("moshe");
            assert false;
        } catch (MarketException e) {
            assert true;
        }
    }
    @Test
    @DisplayName("Approve bid  - fail test - seller needs to approve , buyer name")
    public void approveBidSellerApproveBuyerNameTest(){
        bid.setSideNeedToApprove(Bid.Side.seller);
        try {
            Assertions.assertFalse(bid.approveBid("raz"));
        } catch (MarketException e) {
            assert false;
        }
    }
    @Test
    @DisplayName("Approve bid - both sides need to approve but only seller approve")
    public void approveBidBothSidesFailTest(){
        bid.setSideNeedToApprove(Bid.Side.both);
        try {
            Assertions.assertFalse(bid.approveBid("ido"));
            Assertions.assertFalse(bid.approveBid("ayala"));
        } catch (MarketException e) {
            assert false;
        }
    }
    @Test
    @DisplayName("Approve bid - both sides approve ")
    public void approveBidBothSidesGoodTest(){
        bid.setSideNeedToApprove(Bid.Side.both);
        try {
            bid.approveBid("ido");
            bid.approveBid("ayala");
            Assertions.assertTrue(bid.approveBid("raz"));
        } catch (MarketException e) {
            assert false;
        }
    }
    @Test
    @DisplayName("Reject bid test -fail test -buyer  ")
    public void rejectBidBuyerTest(){
        Assertions.assertThrows(MarketException.class,()->bid.rejectBid("raz"));
    }
    @Test
    @DisplayName("Reject bid test - fail test -seller  ")
    public void rejectBidSellerTest(){
        bid.setSideNeedToApprove(Bid.Side.buyer);
        Assertions.assertThrows(MarketException.class,()->bid.rejectBid("ido"));
    }

    @Test
    @DisplayName("Suggest new offer - good test - seller offers")
    public void sellerSuggestNewOfferGoodTest(){
        try {
            bid.suggestNewOffer("ido",10.0);
            Assertions.assertEquals(Bid.Side.both,bid.getSideNeedToApprove());
            Assertions.assertEquals(10.0,bid.getPrice());
            Assertions.assertFalse(bid.isApproved());
        } catch (MarketException e) {
            assert false;
        }
    }

    @Test
    @DisplayName("Suggest new offer - fail test")
    public void suggestNewOfferFailTest(){
        bid.setSideNeedToApprove(Bid.Side.buyer);
        Assertions.assertThrows(MarketException.class,()->bid.suggestNewOffer("raz",3.0));
    }

    @Test
    @DisplayName("Suggest new offer - fail test")
    public void suggestNewOfferFailTest2(){
        Assertions.assertThrows(MarketException.class,()->bid.suggestNewOffer("raz",3.0));
    }

    @Test
    @DisplayName("Suggest new offer - fail test")
    public void suggestNewOfferFailTest3(){
        Assertions.assertThrows(MarketException.class,()->bid.suggestNewOffer("ido",-3.0));
    }

    @Test
    @DisplayName("Suggest new offer - fail test")
    public void suggestNewOfferFailTest4(){
        Assertions.assertThrows(MarketException.class,()->bid.suggestNewOffer("ido",5.0));
    }

    @Test
    @DisplayName("Suggest new offer - fail test")
    public void suggestNewOfferFailTest5(){
        Assertions.assertThrows(MarketException.class,()->bid.suggestNewOffer("raz",3.0));
    }
    @Test
    @DisplayName("Is approved test - good test")
    public void isApprovedTest(){
        HashMap<String,Boolean> trueShopOwners = new HashMap<>();
        trueShopOwners.put("ido",true);
        trueShopOwners.put("ayala",true);
        bid.setShopOwnersStatus(trueShopOwners);
        Assertions.assertTrue(bid.isApproved());
    }
    @Test
    @DisplayName("Is approved test - both sides needs to approve")
    public void isApprovedBothSidesTest(){
        bid.setSideNeedToApprove(Bid.Side.both);
        HashMap<String,Boolean> trueShopOwners = new HashMap<>();
        trueShopOwners.put("ido",true);
        trueShopOwners.put("ayala",true);
        bid.setShopOwnersStatus(trueShopOwners);
        Assertions.assertTrue(bid.isApproved());
        Assertions.assertEquals(Bid.Side.buyer,bid.getSideNeedToApprove());
    }
    @Test
    @DisplayName("Is approved test - fail test - no one is seller part approved.")
    public void isApprovedFailTest(){
        Assertions.assertFalse(bid.isApproved());
    }
    @Test
    @DisplayName("Is approved test - fail test")
    public void isApprovedSomeApprovedFailTest(){
        HashMap<String,Boolean> booleanHashMap = new HashMap<>();
        booleanHashMap.put("ido",false);
        booleanHashMap.put("ayala",true);
        bid.setShopOwnersStatus(booleanHashMap);
        Assertions.assertFalse(bid.isApproved());
    }



}

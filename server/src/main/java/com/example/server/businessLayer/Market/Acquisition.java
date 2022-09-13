package com.example.server.businessLayer.Market;


import com.example.server.businessLayer.Market.ResourcesObjects.DebugLog;
import com.example.server.businessLayer.Payment.PaymentServiceProxy;
import com.example.server.businessLayer.Publisher.NotificationHandler;
import com.example.server.businessLayer.Supply.Address;
import com.example.server.businessLayer.Payment.PaymentMethod;
import com.example.server.businessLayer.Supply.SupplyServiceProxy;
import com.example.server.businessLayer.Market.ResourcesObjects.ErrorLog;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.businessLayer.Market.Users.Member;
import com.example.server.businessLayer.Market.Users.UserController;
import com.example.server.businessLayer.Market.Users.Visitor;

import java.util.Map;

public class Acquisition {
    private boolean paymentDone;
    private boolean supplyConfirmed;
    ShoppingCart shoppingCartToBuy;
    String buyerName;
    int supplyID;
    int paymentID;
    private Statistics statistics;

    public Acquisition(ShoppingCart shoppingCartToBuy, String buyerName) {
        this.shoppingCartToBuy = shoppingCartToBuy;
        this.buyerName = buyerName;
        paymentDone = false;
        supplyConfirmed = false;
        statistics= Statistics.getInstance();
    }

    @Override
    public boolean equals(Object object){
        if(object instanceof Acquisition){
            Acquisition acquisitionToCompare = (Acquisition) object;
            if(!acquisitionToCompare.buyerName.equals ( this.buyerName ))
                return false;
            if(acquisitionToCompare.supplyID != this.supplyID)
                return false;
            if(acquisitionToCompare.paymentID != this.paymentID)
                return false;
            if(!acquisitionToCompare.shoppingCartToBuy.equals ( this.shoppingCartToBuy ))
                return false;
            if(acquisitionToCompare.supplyConfirmed == this.supplyConfirmed && acquisitionToCompare.paymentDone == this.paymentDone)
                return true;
        }
        return false;
    }


    public void buyShoppingCart(NotificationHandler publisher, double expectedPrice, PaymentMethod paymentMethod, Address address, PaymentServiceProxy paymentHandler, SupplyServiceProxy supplyHandler) throws MarketException, Exception {

        // checks the price is correct
        //todo: check why there is not an exception here.
        isPriceCorrect(publisher, expectedPrice);


        if (address == null) {
            DebugLog.getInstance().Log("Address not supplied.");
            throw new MarketException("Address not supplied.");
        }
        if (!address.isLegal()) {
            DebugLog.getInstance().Log("Address details are illegal.");
            throw new MarketException("Address details are illegal.");
        }
        if (paymentMethod == null) {
            DebugLog.getInstance().Log("Payment method not supplied.");
            throw new MarketException("Payment method not supplied.");
        }
        if (!paymentMethod.isLegal()) {
            DebugLog.getInstance().Log("Payment method details are illegal.");
            throw new MarketException("Payment method details are illegal.");
        }
        supplyID = supplyHandler.supply(address);
        if (supplyID == -1) {
            shoppingCartToBuy.cancelShopSave();
            ErrorLog errorLog = ErrorLog.getInstance();
            errorLog.Log("Supply has been failed.");
            throw new MarketException("supply has been failed. shopping cart did not change");
        }
        supplyConfirmed = true;

        paymentID = paymentHandler.pay(paymentMethod);
        if (paymentID == -1) {
            shoppingCartToBuy.cancelShopSave();
            supplyHandler.cancelSupply(supplyID);
            ErrorLog errorLog = ErrorLog.getInstance();
            errorLog.Log("Payment has been failed.");
            throw new MarketException("payment has been failed. shopping cart did not change and supply was canceled");
        }
        paymentDone = true;
        Visitor visitor = UserController.getInstance().getVisitor(buyerName);
        Member member = visitor.getMember();
        double actualPrice = 0;
        if (member != null) {
            //todo - add discount calculation
            for (Map.Entry<Shop, ShoppingBasket> entry : shoppingCartToBuy.getCart().entrySet()) {
                Shop currShop = entry.getKey();
                if (!currShop.getPurchasePolicy().isPoliciesHeld(entry.getValue())) {
                    throw new MarketException("One of the baskets does not match its shop policy");
                }
                actualPrice = actualPrice + currShop.getDiscountPolicy().calculateDiscount(entry.getValue());
            }
            AcquisitionHistory acq = new AcquisitionHistory(shoppingCartToBuy, member.getName(), actualPrice, expectedPrice);
            member.savePurchase(acq);
        }
        shoppingCartToBuy.clear();
    }

    private void isPriceCorrect(NotificationHandler publisher, double expectedPrice) throws MarketException {
        double actualPrice;
        actualPrice = shoppingCartToBuy.saveFromShops(publisher, buyerName);

        if (Math.abs(actualPrice - expectedPrice) > 0.001) {
            shoppingCartToBuy.cancelShopSave();
            ErrorLog errorLog = ErrorLog.getInstance();
            errorLog.Log("Price for shopping cart has changed.");
            throw new MarketException("Price for shopping cart has changed.");

        }

    }


    public boolean isPaymentDone() {
        return paymentDone;
    }

    public void setPaymentDone(boolean paymentDone) {
        this.paymentDone = paymentDone;
    }

    public boolean isSupplyConfirmed() {
        return supplyConfirmed;
    }

    public void setSupplyConfirmed(boolean supplyConfirmed) {
        this.supplyConfirmed = supplyConfirmed;
    }

    public ShoppingCart getShoppingCartToBuy() {
        return shoppingCartToBuy;
    }

    public void setShoppingCartToBuy(ShoppingCart shoppingCartToBuy) {
        this.shoppingCartToBuy = shoppingCartToBuy;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public int getSupplyID() {
        return supplyID;
    }

    public int getPaymentID() {
        return paymentID;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }
}
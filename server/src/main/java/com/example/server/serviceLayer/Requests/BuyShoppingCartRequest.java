package com.example.server.serviceLayer.Requests;

import com.example.server.businessLayer.Payment.CreditCard;
import com.example.server.businessLayer.Payment.PaymentMethod;
import com.example.server.businessLayer.Supply.Address;

public class BuyShoppingCartRequest {
    private String visitorName;
    private double expectedPrice;
    private CreditCard paymentMethod;
    private Address address;

    public BuyShoppingCartRequest() {
    }

    public BuyShoppingCartRequest(String visitorName, double expectedPrice, CreditCard paymentMethod, Address address) {
        this.visitorName = visitorName;
        this.expectedPrice = expectedPrice;
        this.paymentMethod = paymentMethod;
        this.address = address;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }

    public double getExpectedPrice() {
        return expectedPrice;
    }

    public void setExpectedPrice(double expectedPrice) {
        this.expectedPrice = expectedPrice;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(CreditCard paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}

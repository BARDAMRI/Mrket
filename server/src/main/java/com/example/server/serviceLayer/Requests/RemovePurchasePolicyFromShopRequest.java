package com.example.server.serviceLayer.Requests;

import com.example.server.serviceLayer.FacadeObjects.PolicyFacade.Wrappers.PurchasePolicyTypeWrapper;

public class RemovePurchasePolicyFromShopRequest {

    private PurchasePolicyTypeWrapper purchasePolicyTypeFacade;
    private String shopName;
    private String visitorName;

    public RemovePurchasePolicyFromShopRequest(PurchasePolicyTypeWrapper purchasePolicyTypeFacade, String shopName, String visitorName) {
        this.purchasePolicyTypeFacade = purchasePolicyTypeFacade;
        this.shopName = shopName;
        this.visitorName = visitorName;
    }

    RemovePurchasePolicyFromShopRequest(){}

    public PurchasePolicyTypeWrapper getPolicy() {
        return purchasePolicyTypeFacade;
    }

    public void setPolicy(PurchasePolicyTypeWrapper purchasePolicyTypeFacade) {
        this.purchasePolicyTypeFacade = purchasePolicyTypeFacade;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }
}

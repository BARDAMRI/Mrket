package com.example.server.serviceLayer.Requests;

import com.example.server.serviceLayer.FacadeObjects.PolicyFacade.Wrappers.PurchasePolicyTypeWrapper;

public class AddPurchasePolicyToShopRequest {

    private PurchasePolicyTypeWrapper purchasePolicyTypeFacade;
    private String shopName;
    private String visitorName;

    public AddPurchasePolicyToShopRequest(PurchasePolicyTypeWrapper purchasePolicyTypeFacade, String shopName, String visitorName) {
        this.purchasePolicyTypeFacade = purchasePolicyTypeFacade;
        this.shopName = shopName;
        this.visitorName = visitorName;
    }

    AddPurchasePolicyToShopRequest(){}


    public PurchasePolicyTypeWrapper getPurchasePolicyTypeFacade() {
        return purchasePolicyTypeFacade;
    }

    public void setPurchasePolicyTypeFacade(PurchasePolicyTypeWrapper purchasePolicyTypeFacade) {
        this.purchasePolicyTypeFacade = purchasePolicyTypeFacade;
    }

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

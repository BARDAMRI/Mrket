package com.example.server.serviceLayer.Requests;

import com.example.server.serviceLayer.FacadeObjects.PolicyFacade.ConditionFacade;
import com.example.server.serviceLayer.FacadeObjects.PolicyFacade.DiscountLevelStateFacade;
import com.example.server.serviceLayer.FacadeObjects.PolicyFacade.DiscountTypeFacade;
import com.example.server.serviceLayer.FacadeObjects.PolicyFacade.Wrappers.DiscountTypeWrapper;

public class AddDiscountToShopRequest {

    private DiscountTypeWrapper discount;
    private String shopName;
    private String visitorName;

    public AddDiscountToShopRequest(DiscountTypeWrapper discount, String shopName, String visitorName) {
        this.discount = discount;
        this.shopName = shopName;
        this.visitorName = visitorName;
    }

    AddDiscountToShopRequest(){}

    public DiscountTypeWrapper getDiscount() {
        return discount;
    }

    public void setDiscount(DiscountTypeWrapper discount) {
        this.discount = discount;
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

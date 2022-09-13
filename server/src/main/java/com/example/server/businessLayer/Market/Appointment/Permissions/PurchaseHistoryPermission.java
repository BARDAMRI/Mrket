package com.example.server.businessLayer.Market.Appointment.Permissions;

import com.example.server.businessLayer.Market.Shop;

public class PurchaseHistoryPermission extends IPermission<StringBuilder> {

    public PurchaseHistoryPermission() { this.name = "PurchaseHistoryPermission"; }

    @Override
    public StringBuilder apply(Shop relatedShop) {
        return relatedShop.getReview ();
    }

    @Override
    public boolean isPermission(String permission) {
        return permission == name;
    }
}

package com.example.server.businessLayer.Market.Appointment.Permissions;

import com.example.server.businessLayer.Market.Shop;

public class ApproveBidPermission extends IPermission{

    public ApproveBidPermission(){
        this.name = "ApproveBidPermission";
    }
    @Override
    public Object apply(Shop relatedShop) {
        return null;
    }

    @Override
    public boolean isPermission(String permission) {
        return permission.equals(name);
    }
}

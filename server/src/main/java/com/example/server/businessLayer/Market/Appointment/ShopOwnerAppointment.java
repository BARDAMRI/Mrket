package com.example.server.businessLayer.Market.Appointment;

import com.example.server.businessLayer.Market.Shop;
import com.example.server.businessLayer.Market.Users.Member;
import com.example.server.serviceLayer.FacadeObjects.AppointmentFacade;
import com.example.server.serviceLayer.FacadeObjects.ShopManagerAppointmentFacade;
import com.example.server.serviceLayer.FacadeObjects.ShopOwnerAppointmentFacade;

public class ShopOwnerAppointment extends Appointment {
    private boolean isShopFounder;
    //TODO - check appoint is not null unless it is founder
    public ShopOwnerAppointment(Member appointed, Member appoint, Shop relatedShop,
                                boolean isShopFounder) {
        super(appointed, appoint, relatedShop);
        this.isShopFounder = isShopFounder;

    }

    @Override
    public boolean isManager() {
        return false;
    }

    @Override
    public boolean isOwner() {
        return true;
    }

    @Override
    public AppointmentFacade visitToFacade(ShopOwnerAppointmentFacade shopOwnerAppointmentFacade) {
        return shopOwnerAppointmentFacade.toFacade ( this );
    }

    @Override
    public AppointmentFacade visitToFacade(ShopManagerAppointmentFacade shopManagerAppointmentFacade) {
        return shopManagerAppointmentFacade.toFacade ( this );
    }

    public boolean isShopFounder() {
        return isShopFounder;
    }

}

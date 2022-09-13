package com.example.server.businessLayer.Market.Appointment;

import com.example.server.businessLayer.Market.Appointment.Permissions.IPermission;
import com.example.server.businessLayer.Market.Shop;
import com.example.server.businessLayer.Market.Users.Member;
import com.example.server.serviceLayer.FacadeObjects.AppointmentFacade;
import com.example.server.serviceLayer.FacadeObjects.ShopManagerAppointmentFacade;
import com.example.server.serviceLayer.FacadeObjects.ShopOwnerAppointmentFacade;

import java.util.List;

public class ShopManagerAppointment extends Appointment {


    public ShopManagerAppointment(Member appointed, Member appoint, Shop relatedShop) {
        super(appointed, appoint, relatedShop );
    }

    public ShopManagerAppointment(Member appointed, Member superVisor, Shop relatedShop, List<IPermission> permissions) {
        super(appointed, superVisor, relatedShop, permissions);
    }

    @Override
    public boolean isManager() {
        return true;
    }

    @Override
    public boolean isOwner() {
        return false;
    }

    @Override
    public AppointmentFacade visitToFacade(ShopOwnerAppointmentFacade shopOwnerAppointmentFacade) {
        return shopOwnerAppointmentFacade.toFacade ( this );
    }

    @Override
    public AppointmentFacade visitToFacade(ShopManagerAppointmentFacade shopManagerAppointmentFacade) {
        return shopManagerAppointmentFacade.toFacade ( this );
    }


    public void removePermission(IPermission permission){
        if(permissions.contains ( permission ))
            permissions.remove ( permission );
    }

    public void addPermission(IPermission permission){
        permissions.add ( permission );
    }




}

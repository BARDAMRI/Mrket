package com.example.server.serviceLayer.FacadeObjects;

import com.example.server.businessLayer.Market.Appointment.Appointment;
import com.example.server.businessLayer.Market.Appointment.ShopManagerAppointment;
import com.example.server.businessLayer.Market.Appointment.ShopOwnerAppointment;
import com.example.server.businessLayer.Market.Market;
import com.example.server.businessLayer.Market.Shop;
import com.example.server.businessLayer.Market.Users.Member;
import com.example.server.businessLayer.Market.Users.UserController;


import java.util.ArrayList;
import java.util.List;

public class ShopOwnerAppointmentFacade extends AppointmentFacade {
    private boolean isShopFounder;

    public ShopOwnerAppointmentFacade(){super();
        this.type = "ShopOwnerAppointmentFacade";
    }

    public ShopOwnerAppointmentFacade(Member appointed, Member superVisor,
                                      Shop relatedShop, List<PermissionFacade> permissions, boolean isShopFounder) {
        super(appointed, relatedShop, permissions, "ShopOwnerAppointmentFacade");
        if(!isShopFounder) {
            this.superVisor = superVisor.getName();
        }
        this.isShopFounder = isShopFounder;
    }

    public ShopOwnerAppointmentFacade(ShopOwnerAppointment appointment) {
        super(appointment.getAppointed(),appointment.getRelatedShop(), new ArrayList<>(), "ShopOwnerAppointmentFacade");
        if(!appointment.isShopFounder())
            this.superVisor = appointment.getSuperVisor().getName();
        permissions.addAll(appointment.getPermissions().stream().map(PermissionFacade::new).toList());
        this.isShopFounder = appointment.isShopFounder();
    }

    public boolean isShopFounder() {
        return isShopFounder;
    }

    public AppointmentFacade toFacade(ShopManagerAppointment appointment) {
        return null;
    }

    public AppointmentFacade toFacade(ShopOwnerAppointment appointment) {
        return new ShopOwnerAppointmentFacade ( appointment );
    }

    @Override
    public AppointmentFacade toFacade(Appointment appointment) {
        return appointment.visitToFacade(this);
    }


    @Override
    public Appointment toBusinessObject() {
        Member appointed = UserController.getInstance().getMember(this.appointed);
        Member superVisor = UserController.getInstance().getMember(this.superVisor);
        Shop relatedShop = Market.getInstance().getShopByName(this.relatedShop);
        return new ShopOwnerAppointment(appointed, superVisor, relatedShop, this.isShopFounder);
    }
}
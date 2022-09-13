package com.example.server.serviceLayer.FacadeObjects;

import com.example.server.businessLayer.Market.Appointment.Appointment;
import com.example.server.businessLayer.Market.Appointment.ShopManagerAppointment;
import com.example.server.businessLayer.Market.Appointment.ShopOwnerAppointment;
import com.example.server.businessLayer.Market.Shop;
import com.example.server.businessLayer.Market.Users.Member;

import java.util.List;

public abstract class AppointmentFacade implements FacadeObject<Appointment> {
    protected String appointed;       //  the actual appointed member
    protected String superVisor;      //  member appointedMe
    protected String relatedShop;
    List<PermissionFacade> permissions;
    protected String type;
    public AppointmentFacade(){}

    public AppointmentFacade(Member appointed, Shop relatedShop,
                             List<PermissionFacade> permissions, String type) {
        this.appointed = appointed.getName();
        this.relatedShop = relatedShop.getShopName();
        this.permissions = permissions;
        this.type = type;
    }

    public String getAppointed() {
        return appointed;
    }

    public void setAppointed(String appointed) {
        this.appointed = appointed;
    }

    public String getSuperVisor() {
        return superVisor;
    }

    public void setSuperVisor(String superVisor) {
        this.superVisor = superVisor;
    }

    public String getRelatedShop() {
        return relatedShop;
    }

    public void setRelatedShop(String relatedShop) {
        this.relatedShop = relatedShop;
    }

    public List<PermissionFacade> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionFacade> permissions) {
        this.permissions = permissions;
    }

    public abstract AppointmentFacade toFacade(ShopManagerAppointment appointment);

    public abstract AppointmentFacade toFacade(ShopOwnerAppointment appointment);

    public abstract AppointmentFacade toFacade(Appointment appointment);

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
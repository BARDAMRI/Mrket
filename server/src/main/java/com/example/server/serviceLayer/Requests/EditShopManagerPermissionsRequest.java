package com.example.server.serviceLayer.Requests;

import com.example.server.serviceLayer.FacadeObjects.ShopManagerAppointmentFacade;

public class EditShopManagerPermissionsRequest {

    private String shopOwnerName;
    private String managerName;
    private String relatedShop;
    private ShopManagerAppointmentFacade updatedAppointment;


    public EditShopManagerPermissionsRequest(String shopOwnerName, String managerName, String relatedShop, ShopManagerAppointmentFacade updatedAppointment) {
        this.shopOwnerName = shopOwnerName;
        this.managerName = managerName;
        this.relatedShop = relatedShop;
        this.updatedAppointment = updatedAppointment;
    }

    public EditShopManagerPermissionsRequest() {
    }

    public String getShopOwnerName() {
        return shopOwnerName;
    }

    public void setShopOwnerName(String shopOwnerName) {
        this.shopOwnerName = shopOwnerName;
    }

    public String getManagerName() {
        return managerName;
    }

    public void setManagerName(String managerName) {
        this.managerName = managerName;
    }

    public String getRelatedShop() {
        return relatedShop;
    }

    public void setRelatedShop(String relatedShop) {
        this.relatedShop = relatedShop;
    }

    public ShopManagerAppointmentFacade getUpdatedAppointment() {
        return updatedAppointment;
    }

    public void setUpdatedAppointment(ShopManagerAppointmentFacade updatedAppointment) {
        this.updatedAppointment = updatedAppointment;
    }
}

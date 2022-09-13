package com.example.server.serviceLayer.FacadeObjects;

import com.example.server.businessLayer.Market.AcquisitionHistory;
import com.example.server.businessLayer.Market.Appointment.Appointment;
import com.example.server.businessLayer.Market.Appointment.ShopManagerAppointment;
import com.example.server.businessLayer.Market.Appointment.ShopOwnerAppointment;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.businessLayer.Market.ShoppingCart;
import com.example.server.businessLayer.Market.Users.Member;


import java.util.ArrayList;
import java.util.List;

public class MemberFacade implements FacadeObject<Member> {
    private String name;
    private ShoppingCartFacade myCart;
    private List<AppointmentFacade> appointedByMe;
    private List<AppointmentFacade> myAppointments;
    private List<ShoppingCartFacade> purchaseHistory;

    public MemberFacade(String name, ShoppingCart myCart,
                        List<AppointmentFacade> appointedByMe,
                        List<AppointmentFacade> myAppointments) {
        this.name = name;
        this.myCart = new ShoppingCartFacade (myCart);
        this.appointedByMe = appointedByMe;
        this.myAppointments = myAppointments;
    }

    public MemberFacade(Member member) {
        if(member == null)
            return;
        this.name = member.getName();
        this.myCart = new ShoppingCartFacade (member.getMyCart());
        this.appointedByMe = member.getAppointedByMe().stream().map((appointment ->
                appointment.isManager() ?
                        new ShopManagerAppointmentFacade((ShopManagerAppointment) appointment) :
                        new ShopOwnerAppointmentFacade((ShopOwnerAppointment) appointment))).toList();
        this.myAppointments = member.getMyAppointments().stream().map((appointment ->
                appointment.isManager() ?
                        new ShopManagerAppointmentFacade((ShopManagerAppointment) appointment) :
                        new ShopOwnerAppointmentFacade((ShopOwnerAppointment) appointment))).toList();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ShoppingCartFacade getMyCart() {
        return myCart;
    }

    public void setMyCart(ShoppingCartFacade myCart) {
        this.myCart = myCart;
    }

    public List<AppointmentFacade> getAppointedByMe() {
        return appointedByMe;
    }

    public void setAppointedByMe(List<AppointmentFacade> appointedByMe) {
        this.appointedByMe = appointedByMe;
    }

    public List<AppointmentFacade> getMyAppointments() {
        return myAppointments;
    }

    public void setMyAppointments(List<AppointmentFacade> myAppointments) {
        this.myAppointments = myAppointments;
    }

    public List<ShoppingCartFacade> getPurchaseHistory() {
        return purchaseHistory;
    }

    public void setPurchaseHistory(List<ShoppingCartFacade> purchaseHistory) {
        this.purchaseHistory = purchaseHistory;
    }

    @Override
    public Member toBusinessObject() throws MarketException {
        ShoppingCart shoppingCart = myCart.toBusinessObject ();
        List<Appointment> appointedByMe = new ArrayList<> (  );
        for(AppointmentFacade appointment : this.appointedByMe)
            appointedByMe.add ( appointment.toBusinessObject () );
        List<Appointment> myAppointments = new ArrayList<> (  );
        for(AppointmentFacade appointment : this.myAppointments)
            myAppointments.add ( appointment.toBusinessObject () );
        List<AcquisitionHistory> purchaseHistory = new ArrayList<> (  );
//        for(ShoppingCartFacade shoppingCartFacade : this.purchaseHistory) {
//            AcquisitionHistory acquisitionHistory = new AcquisitionHistory(shoppingCartFacade.toBusinessObject(),name);
//            purchaseHistory.add(acquisitionHistory);
//        }
        return new Member ( name, shoppingCart, appointedByMe,myAppointments, purchaseHistory);
    }
}

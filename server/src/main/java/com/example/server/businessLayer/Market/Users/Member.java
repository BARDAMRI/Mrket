package com.example.server.businessLayer.Market.Users;


import com.example.server.businessLayer.Market.Acquisition;
import com.example.server.businessLayer.Market.ResourcesObjects.DebugLog;
import com.example.server.businessLayer.Market.ResourcesObjects.EventLog;
import com.example.server.businessLayer.Market.AcquisitionHistory;
import com.example.server.businessLayer.Market.Appointment.Appointment;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.businessLayer.Market.IHistory;
import com.example.server.businessLayer.Market.ShoppingCart;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Member implements IHistory {
    private String name;
    private ShoppingCart myCart;
    private List<Appointment> appointedByMe;
    private List<Appointment> myAppointments;
    private List<AcquisitionHistory> purchaseHistory;

    private List<Acquisition> acquisitions;



    public Member(String name) throws MarketException {
        if (name.equals("")) {
            DebugLog.getInstance().Log("Name cannot be an empty String");
            throw new MarketException("Name cannot be an empty String");
        }
        if(name.charAt ( 0 ) == '@') {
            DebugLog.getInstance().Log("cannot create a member with a username starts with @");
            throw new MarketException("cannot create a member with a username starts with @");
        }

        this.name = name;
        myCart = new ShoppingCart();
        appointedByMe = new CopyOnWriteArrayList<>();
        myAppointments = new CopyOnWriteArrayList<>();
        purchaseHistory = new ArrayList<> (  );
        acquisitions = new ArrayList<>();
    }

    public Member(String name, ShoppingCart shoppingCart, List<Appointment> appointmentedByME, List<Appointment> myAppointments, List<AcquisitionHistory> purchaseHistory ){
        this.name = name;
        myCart = shoppingCart;
        this.appointedByMe = appointmentedByME;
        this.myAppointments = myAppointments;
        this.purchaseHistory = purchaseHistory;
        acquisitions = new ArrayList<>();
    }


    public String getName() {
        return name;
    }

    public void setName(String name) throws MarketException {
        if(name.charAt ( 0 ) == '@')
            throw new MarketException ( "cannot create a member with a username starts with @" );
        this.name = name;
    }

    public ShoppingCart getMyCart() {
        return myCart;
    }

    public void setMyCart(ShoppingCart myCart) {
        this.myCart = myCart;
    }

    public List<Appointment> getAppointedByMe() {
        return appointedByMe;
    }

    public void setAppointedByMe(List<Appointment> appointedByMe) {
        this.appointedByMe = appointedByMe;
    }

    public List<Appointment> getMyAppointments() {
        return myAppointments;
    }

    public void setMyAppointments(List<Appointment> myAppointments) {
        this.myAppointments = myAppointments;
    }

    public void addAppointmentByMe(Appointment app){ this.appointedByMe.add(app);}

    public void addAppointmentToMe(Appointment app){ this.myAppointments.add(app);}
    public void addAcquisition(Acquisition acq){this.acquisitions.add(acq);}
    public void removeAcquisition(Acquisition acq){this.acquisitions.remove(acq);}

    public StringBuilder getPurchaseHistoryString() {
        StringBuilder history = new StringBuilder ( String.format ( "%s:\n", name ) );
        int i = 1;
        for(AcquisitionHistory acquisitionHistory : purchaseHistory){
            history.append ( String.format ( "purchase %d:\n%s", i, acquisitionHistory.toString () ));//TODO - Check if shoppingCart.getReview is same as acq.tostring
            i++;
        }
        EventLog eventLog = EventLog.getInstance();
        eventLog.Log("Pulled "+this.getName()+" history");
        return history;
    }

    public List<AcquisitionHistory> getPurchaseHistory() {
        return purchaseHistory;
    }

    public void savePurchase(AcquisitionHistory acquisitionHistory) {
        purchaseHistory.add (acquisitionHistory);
    }

    @Override
    public StringBuilder getReview() {
        StringBuilder history = new StringBuilder ( String.format ( "%s:\n", name ) );
        int i = 1;
        for(AcquisitionHistory acquisitionHistory : purchaseHistory){
            history.append ( String.format ( "purchase %d:\n%s", i, acquisitionHistory.toString () ));//TODO - Check if shoppingCart.getReview is same as acq.tostring
            i++;
        }
        EventLog eventLog = EventLog.getInstance();
        eventLog.Log("Pulled "+this.getName()+" history");
        return history;
    }

    public void setPurchaseHistory(List<AcquisitionHistory> purchaseHistory) {
        this.purchaseHistory = purchaseHistory;
    }

    public List<Acquisition> getAcquisitions() {
        return acquisitions;
    }

    public void setAcquisitions(List<Acquisition> acquisitions) {
        this.acquisitions = acquisitions;
    }
}

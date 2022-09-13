package com.example.server.businessLayer.Market.Appointment;

import com.example.server.businessLayer.Market.ResourcesObjects.DebugLog;
import com.example.server.businessLayer.Market.ResourcesObjects.EventLog;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PendingAppointments {
    private Map<String ,Agreement> agreements;// <AppointedName , agreement>
    private Map<String,ShopOwnerAppointment> appointments; // <AppointedName , appointment>

    public PendingAppointments(Map<String ,Agreement> agreements, Map<String, ShopOwnerAppointment> appointments) {
        this.agreements = agreements;
        this.appointments = appointments;
    }
    public PendingAppointments(){
        this.appointments = new HashMap<>();
        this.agreements = new HashMap<>();
    }

    public void removeAppointment(String appointedName) throws MarketException {
        if (!appointments.containsKey(appointedName))
        {
            DebugLog.getInstance().Log("There is no appointment for "+appointedName+" to remove.");
            throw new MarketException("There is no appointment for "+appointedName+" to remove.");
        }
        appointments.remove(appointedName);
        agreements.remove(appointedName);
        EventLog.getInstance().Log("Appointment for " +appointedName+" has been removed.");
    }

    public void addAppointment(String appointedName, ShopOwnerAppointment appointment, List<String> owners) throws MarketException {
        this.appointments.put(appointedName,appointment);
        Agreement agreement = new Agreement(owners);
        this.agreements.put(appointedName,agreement);
        this.agreements.get(appointedName).setOwnerApproval(appointment.getSuperVisor().getName(),true);
        EventLog.getInstance().Log("Appointment for " +appointedName+" has been added.");
    }

    public Map<String, Agreement> getAgreements() {
        return agreements;
    }

    public void setAgreements(Map<String, Agreement> agreements) {
        this.agreements = agreements;
    }

    public Map<String, ShopOwnerAppointment> getAppointments() {
        return appointments;
    }

    public void setAppointments(Map<String, ShopOwnerAppointment> appointments) {
        this.appointments = appointments;
    }

    public List<String> getMyPendingAppointments(String ownerName) {
        List <String> res = new ArrayList<>();
        for (Map.Entry<String,Agreement> entry:agreements.entrySet())
        {
            if (!entry.getValue().getOwnerStatus(ownerName)){
                res.add(entry.getKey());
            }
        }
        EventLog.getInstance().Log(ownerName+" got his pending appointments.");
        return res;
    }

    public boolean approve(String appointedName, String ownerName) throws MarketException {
        if (!getAppointments().containsKey(appointedName)){
            DebugLog.getInstance().Log("There is no pending appointment for "+ appointedName);
            throw new MarketException("There is no pending appointment for "+ appointedName);
        }
        agreements.get(appointedName).setOwnerApproval(ownerName,true);
        EventLog.getInstance().Log(ownerName+" approved the appointment for:"+appointedName);
        return (agreements.get(appointedName).isAgreed ());
    }

    public List<String> removeOwner(String firedAppointed) { // returns list of appointed Names whos appointment completed because removal of owner
        EventLog.getInstance().Log("Checking if appointments status has been changed after "+ firedAppointed+" got fired. ");
        for (Map.Entry<String,ShopOwnerAppointment> app: this.appointments.entrySet())
        {
            if (app.getValue().getSuperVisor().getName().equals(firedAppointed))
            {
                appointments.remove(app.getKey());
            }
        }
        List<String> completed = new ArrayList<>();
        for (Map.Entry<String,Agreement> entry: this.agreements.entrySet()){
            entry.getValue().removeOwner(firedAppointed);
            if (entry.getValue().isAgreed()){
                completed.add(entry.getKey());
            }
        }
        return completed;
    }
}

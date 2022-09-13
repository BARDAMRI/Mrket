package com.example.server.businessLayer.Market.Appointment;

import com.example.server.businessLayer.Market.ResourcesObjects.DebugLog;
import com.example.server.businessLayer.Market.ResourcesObjects.EventLog;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Agreement {
    private boolean agreed;
    private Map<String,Boolean> ownersAppointmentApproval;

    public Agreement(boolean agreed, Map<String, Boolean> ownersAgreementStatus) {
        this.agreed = agreed;
        this.ownersAppointmentApproval = ownersAgreementStatus;
    }

    public Agreement(List<String> owners){
        this.ownersAppointmentApproval = new HashMap<>();
        for (String owner: owners){
            ownersAppointmentApproval.put(owner,false);
        }
        this.agreed = false;
    }

    public void setOwnerApproval(String ownerName , boolean approve) throws MarketException {
        if (!ownersAppointmentApproval.containsKey(ownerName)) {
            DebugLog.getInstance().Log("You dont have the authority to approve or reject this appointment.");
            throw new MarketException("You dont have the authority to approve or reject this appointment.");
        }
        ownersAppointmentApproval.replace(ownerName,approve);
        updateStatus();
    }

    private void updateStatus() {
        for (Map.Entry<String,Boolean> entry:ownersAppointmentApproval.entrySet())
        {
            if (!entry.getValue()) {
                this.agreed = false;
                return;
            }
        }
        this.agreed =true;


    }

    public boolean isAgreed() {
        return agreed;
    }

    public void setAgreed(boolean agreed) {
        this.agreed = agreed;
    }

    public Map<String, Boolean> getOwnersAppointmentApproval() {
        return ownersAppointmentApproval;
    }

    public void setOwnersAppointmentApproval(Map<String, Boolean> ownersAppointmentApproval) {
        this.ownersAppointmentApproval = ownersAppointmentApproval;
    }

    public boolean getOwnerStatus(String ownerName) {
        return ownersAppointmentApproval.get(ownerName);
    }

    public void removeOwner(String firedAppointed) {
        if (this.ownersAppointmentApproval.containsKey(firedAppointed))
            ownersAppointmentApproval.remove(firedAppointed);
        updateStatus();
    }
}

package com.example.server.UnitTests;

import com.example.server.businessLayer.Market.Appointment.Agreement;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class AgreementTest {
    Agreement agreement;

    @BeforeEach
    public void reset(){
        List<String> owners = new ArrayList<>();
        owners.add("ido");owners.add("ayala");
        agreement = new Agreement(owners);
    }

    @Test
    @DisplayName("Testing default status")
    public void DefaultTest(){
        Assertions.assertFalse(agreement.getOwnerStatus("ayala"));
        Assertions.assertFalse(agreement.getOwnerStatus("ido"));
    }
    @Test
    @DisplayName("Set owner approval - valid case")
    public void setOwnerApprovalGoodTest(){
        try {
            agreement.setOwnerApproval("ido",true);
            Assertions.assertTrue(agreement.getOwnerStatus("ido"));
        }
        catch (MarketException e)
        {
            assert false;
        }
    }
    @Test
    @DisplayName("Set owner approval - no such owner")
    public void setOwnerApprovalFailTest(){
        try {
            agreement.setOwnerApproval("raz",true);
            assert false;
        }
        catch (MarketException e)
        {
            assert true;
        }
    }
    @Test
    @DisplayName("Update status - only one approved")
    public void updateStatusOnlyOneTest(){
        try {
            agreement.setOwnerApproval("ido",true);
            Assertions.assertFalse(agreement.isAgreed());
        }
        catch (MarketException e)
        {
            assert false;
        }
    }
    @Test
    @DisplayName("Update status - Everyone approved")
    public void updateStatusEveryoneTest(){
        try {
            agreement.setOwnerApproval("ido",true);
            agreement.setOwnerApproval("ayala",true);
            Assertions.assertTrue(agreement.isAgreed());
        }
        catch (MarketException e)
        {
            assert false;
        }
    }
    @Test
    @DisplayName("Remove owner test")
    public void removeOwnerTest(){
        try {
            agreement.setOwnerApproval("ido",true);
        } catch (MarketException e) {
            assert false;
        }
        Assertions.assertFalse(agreement.isAgreed());
        agreement.removeOwner("ayala");
        Assertions.assertTrue(agreement.isAgreed());
    }
}

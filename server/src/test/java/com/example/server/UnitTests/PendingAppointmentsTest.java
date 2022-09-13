package com.example.server.UnitTests;

import com.example.server.businessLayer.Market.Appointment.Agreement;
import com.example.server.businessLayer.Market.Appointment.PendingAppointments;
import com.example.server.businessLayer.Market.Appointment.ShopOwnerAppointment;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.businessLayer.Market.Users.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

public class PendingAppointmentsTest {
    PendingAppointments pendingAppointments;
    @Mock
    ShopOwnerAppointment appointment1;
    @Mock
    Agreement agreement1;
    @Mock
    Member appointed1;
    @Mock
    Member supervisor1;

    //----------------------------------------------
    @Mock
    ShopOwnerAppointment appointment2;
    @Mock
    Agreement agreement2;
    @Mock
    Member appointed2;
    @Mock
    Member supervisor2;

    //----------------------------------------------




    @BeforeEach
    public void reset(){
        pendingAppointments = new PendingAppointments();
        agreement1 = Mockito.mock(Agreement.class);
        agreement2 = Mockito.mock(Agreement.class);
        appointment1= Mockito.mock(ShopOwnerAppointment.class);
        appointment2= Mockito.mock(ShopOwnerAppointment.class);
        appointed1 = Mockito.mock(Member.class);
        appointed2= Mockito.mock(Member.class);
        supervisor1= Mockito.mock(Member.class);
        supervisor2= Mockito.mock(Member.class);
        Mockito.when(appointed1.getName()).thenReturn("raz");
        Mockito.when(appointed2.getName()).thenReturn("shaked");
        Mockito.when(supervisor1.getName()).thenReturn("ido");
        Mockito.when(supervisor2.getName()).thenReturn("ayala");
        Mockito.when(appointment1.getAppointed()).thenReturn(appointed1);
        Mockito.when(appointment2.getAppointed()).thenReturn(appointed2);
        Mockito.when(appointment1.getSuperVisor()).thenReturn(supervisor1);
        Mockito.when(appointment2.getSuperVisor()).thenReturn(supervisor2);
    }
    @Test
    @DisplayName("Add appointment test")
    public void addAppointmentTest(){
        List<String> owners = new ArrayList<>();
        owners.add("ido");owners.add("ayala");
        try {
            pendingAppointments.addAppointment(appointed1.getName(),appointment1,owners);
            Assertions.assertEquals(1,pendingAppointments.getAppointments().size());
            Assertions.assertTrue(pendingAppointments.getAppointments().containsKey("raz"));
        } catch (MarketException e) {
            assert false;
        }
    }
    @Test
    @DisplayName("Remove appointment test - valid appointed name")
    public void removeAppointmentTest(){
        List<String> owners = new ArrayList<>();
        owners.add("ido");
        owners.add("ayala");
        try
        {
            pendingAppointments.addAppointment(appointed1.getName(),appointment1,owners);
            pendingAppointments.removeAppointment(appointed1.getName());
            Assertions.assertEquals(0,pendingAppointments.getAppointments().size());
            Assertions.assertEquals(0,pendingAppointments.getAgreements().size());
        }
        catch (MarketException e){
            assert false;
        }
    }
    @Test
    @DisplayName("Remove appointment - Invalid appointment name")
    public void removeAppointmentInvalidCaseTest(){
        List<String> owners = new ArrayList<>();
        owners.add("ido");
        owners.add("ayala");
        try
        {
            pendingAppointments.addAppointment(appointed1.getName(),appointment1,owners);
            pendingAppointments.removeAppointment("ido");
            assert false;
        }
        catch (MarketException e){
            assert true;
        }
    }
    @Test
    @DisplayName("Get my pending appointments")
    public void getMyPendingTest(){
        List<String> owners = new ArrayList<>();
        owners.add("ido");owners.add("ayala");owners.add("bar");
        try {
            pendingAppointments.addAppointment(appointed1.getName(),appointment1,owners);
            pendingAppointments.addAppointment(appointed2.getName(),appointment2,owners);
            pendingAppointments.approve(appointed1.getName(),supervisor1.getName());
            List<String> myPendingApps = pendingAppointments.getMyPendingAppointments(supervisor1.getName());
            Assertions.assertEquals(1,myPendingApps.size());
        } catch (MarketException e) {
            assert false;
        }
    }
    @Test
    @DisplayName("Get my pending appointments - owner who is not supervisor ")
    public void getMyPendingOwnerNotSupervisorTest(){
        List<String> owners = new ArrayList<>();
        owners.add("ido");owners.add("ayala");owners.add("bar");
        try {
            pendingAppointments.addAppointment(appointed1.getName(),appointment1,owners);
            pendingAppointments.addAppointment(appointed2.getName(),appointment2,owners);
            pendingAppointments.approve(appointed1.getName(),supervisor1.getName());
            List<String> myPendingApps = pendingAppointments.getMyPendingAppointments("bar");
            Assertions.assertEquals(2,myPendingApps.size());
        } catch (MarketException e) {
            assert false;
        }
    }

    @Test
    @DisplayName("Approve test - valid case all approved ")
    public void approveValidCaseAllAgreedTest(){
        List<String> owners = new ArrayList<>();
        owners.add("ido");owners.add("ayala");
        try {
            pendingAppointments.addAppointment(appointed1.getName(),appointment1,owners);
            Assertions.assertTrue(pendingAppointments.approve(appointed1.getName(),supervisor2.getName()));

        } catch (MarketException e) {
            assert false;
        }
    }
    @Test
    @DisplayName("Approve test - valid case - partly agreed")
    public void approveValidCaseNotAllAgreedTest(){
        List<String> owners = new ArrayList<>();
        owners.add("ido");owners.add("ayala");owners.add("bar");
        try {
            pendingAppointments.addAppointment(appointed1.getName(),appointment1,owners);
            Assertions.assertFalse(pendingAppointments.approve(appointed1.getName(),supervisor2.getName()));

        } catch (MarketException e) {
            assert false;
        }
    }
    @Test
    @DisplayName("Remove owner test - isnt a supervisor")
    public void removeNotSupervisorOwner () {
        List<String> owners = new ArrayList<>();
        owners.add("ido");
        owners.add("ayala");
        owners.add("bar");
        try {
            pendingAppointments.addAppointment(appointed1.getName(),appointment1,owners);
        } catch (MarketException e) {
            assert false;
        }
        List<String> completed = pendingAppointments.removeOwner("bar");
        Assertions.assertTrue(completed.isEmpty());
    }
    @Test
    @DisplayName("Remove owner test - is a supervisor")
    public void removeSupervisorOwner () {
        List<String> owners = new ArrayList<>();
        owners.add("ido");
        owners.add("ayala");
        try {
            pendingAppointments.addAppointment(appointed1.getName(), appointment1,owners);
        } catch (MarketException e) {
            assert false;
        }
        List<String> completed = pendingAppointments.removeOwner("ayala");
        Assertions.assertEquals(1,completed.size());
        Assertions.assertTrue(completed.contains("raz"));
    }
    @Test
    @DisplayName("Remove owner test - is a supervisor")
    public void removeSupervisorOwnerToComplete() {
        List<String> owners = new ArrayList<>();
        owners.add("ido");owners.add("bar");
        owners.add("ayala");
        try {
            pendingAppointments.addAppointment(appointed1.getName(), appointment1,owners);
            pendingAppointments.approve(appointed1.getName(),"bar");
        } catch (MarketException e) {
            assert false;
        }
        List<String> completed = pendingAppointments.removeOwner("ayala");
        Assertions.assertEquals(1,completed.size());
        Assertions.assertTrue(completed.contains("raz"));
    }

}

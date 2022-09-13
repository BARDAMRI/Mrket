package com.example.server.UnitTests;

import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.businessLayer.Market.Appointment.ShopOwnerAppointment;
import com.example.server.businessLayer.Market.AcquisitionHistory;
import com.example.server.businessLayer.Market.ShoppingCart;
import org.junit.jupiter.api.Test;
import com.example.server.businessLayer.Market.Appointment.Appointment;
import com.example.server.businessLayer.Market.Users.Member;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MemberTest {

    Member member;
    String name = "moshe";
    ShoppingCart myCart = Mockito.mock(ShoppingCart.class);
    ShoppingCart oldCart = Mockito.mock(ShoppingCart.class);
    List<Appointment> appointedByMe = new ArrayList<>();
    List<Appointment> myAppointments = new ArrayList<>();
    List<ShoppingCart> purchaseHistory = new ArrayList<>();

    @BeforeEach
    public void initMemberTest() throws MarketException {
        member = new Member(name);
        member.setMyCart(myCart);

    }

    //TODO need to check for name as visitor12 (autogeneratedName)...
    @Test
    @DisplayName("invalid inputs to constructor")
    public void constructorNullName(){
        try{
            Member m = new Member(null);
            assert false;
        }catch (Exception ignore){
            assert true;
        };

    }
    @Test
    @DisplayName("invalid inputs to constructor - empty string as name")
    public void constructorEmptyString(){
        try{
            Member m = new Member("");
            assert false;
        }catch (MarketException ignore){};
        assert true;
    }

    @Test
    @DisplayName("valid inputs to constructor")
    public void constructorValid(){
        try{
            Member m = new Member(name);
            assert true;
        }
        catch (Exception ignore){assert false;};

    }

    @BeforeEach
    @Test
    public void getName() {
        Assertions.assertEquals(member.getName(),name);
    }


    @Test
    public void getMyCart() {
        Assertions.assertEquals(member.getMyCart(), myCart);
    }


    @Test
    public void addAppointmentByMe() {

        ShopOwnerAppointment shopOwnerAppointment = Mockito.mock(ShopOwnerAppointment.class);
        member.addAppointmentByMe(shopOwnerAppointment);
        assertEquals(1, member.getAppointedByMe().size());
    }

    @Test
    public void addAppointmentToMe() {
        ShopOwnerAppointment shopOwnerAppointment = Mockito.mock(ShopOwnerAppointment.class);
        member.addAppointmentToMe(shopOwnerAppointment);
        assertEquals(1, member.getMyAppointments().size());
    }

    @Test
    public void getPurchaseHistory() {
        String test = member.getReview().toString();
        Assertions.assertEquals(test,String.format ("%s:\n", member.getName ()));
        AcquisitionHistory acq = new AcquisitionHistory(myCart, name, myCart.getCurrentPrice(), myCart.getCurrentPrice());
        member.savePurchase(acq);
        assertEquals(1, member.getPurchaseHistory().size());
        Assertions.assertTrue(member.getReview().toString().contains(name));
    }

}
package com.example.server.UnitTests;

import com.example.server.businessLayer.Market.Users.UserController;
import com.example.server.businessLayer.Market.Users.Visitor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

class UserControllerTest {
    @Mock
    Visitor visitor = Mockito.mock(Visitor.class);
    UserController controller;



    @BeforeEach
    public void reset(){
        controller = UserController.getInstance();
        Map<String,Visitor> emptyMap = new HashMap<>();
        controller.setVisitorsInMarket(emptyMap);
        controller.setNextUniqueNumber(1);
    }

    @Test
    @DisplayName("Guest login test - good test")
    public void GuestLoginTest(){
        controller.guestLogin();
        Assertions.assertEquals(1,controller.getVisitorsInMarket().size());
        Visitor visit = controller.guestLogin();
        Assertions.assertEquals("@visitor2",visit.getName());
    }

    @Test
    @DisplayName("Exit system - good test ")
    public void ExitSystemTest(){
        Visitor visitor1 = controller.guestLogin();
        Assertions.assertEquals(1,controller.getVisitorsInMarket().size());
        try {
            controller.exitSystem(visitor1.getName());
            Assertions.assertEquals(0,controller.getVisitorsInMarket().size());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert false;
        }
    }
    @Test
    @DisplayName("Exit system - fail test - user not logged exit ")
    public void ExitSystemFailTest(){
        Visitor visitor1 = controller.guestLogin();
        Assertions.assertEquals(1,controller.getVisitorsInMarket().size());
        try {
            controller.exitSystem("name");
            Assertions.assertEquals(0,controller.getVisitorsInMarket().size());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert true;
        }
    }

    @Test
    @DisplayName("Register test")
    public void RegisterTest(){
        try {
            controller.register("shaked");
            Assertions.assertNotNull(controller.getMembers().get("shaked"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert false;
        }
    }

    @Test
    @DisplayName("Member log out test - good test")
    public void MemberLogout(){
        try {
            controller.register("raz");
            controller.finishLogin("raz","@visitor1");
            controller.memberLogout("raz");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert false;
        }
    }
}
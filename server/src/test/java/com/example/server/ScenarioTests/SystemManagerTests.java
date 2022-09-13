package com.example.server.ScenarioTests;

import com.example.server.businessLayer.Payment.PaymentServiceProxy;
import com.example.server.businessLayer.Market.Market;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.businessLayer.Market.Users.Visitor;
import com.example.server.businessLayer.Publisher.TextDispatcher;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SystemManagerTests {

    //CHANGE EXTERNAL SERVICES


    static Market market;
    static String managerName = "u1";
    static String managerpassword = "password";
    static PaymentServiceProxy paymentService2 = new PaymentServiceProxy();


    @BeforeAll
    public static void setUp() {
        try {
            market = Market.getInstance();
            if (market.getPaymentService() == null) {
                market.firstInitMarket(managerName, managerpassword);
            }

            else
                market.register(managerName, managerpassword);
        } catch (Exception e) {
                System.out.println(e.getMessage());
        }
    }


    @Test
    @DisplayName("system manager get purchases history")
    public void PurchaseHistory() {
        try {
            try {
                loginManager(managerName,managerpassword);
            }catch (Exception e){
                System.out.println (e.getMessage () );
            }
            String str= new String(market.getAllSystemPurchaseHistory(managerName));
            Assertions.assertNotNull(str);
            try{
                logoutMember(managerName);
            }catch (MarketException e){
                System.out.println(e.getMessage());
            }
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("system manager get purchases history bad case- not logged in")
    public void PurchaseHistoryFail() {
        try {

            try{
                logoutMember(managerName);
            }
            catch (Exception e){}
            market.getAllSystemPurchaseHistory(managerName);
            assert  false;
        } catch (Exception e) {
            assert true;
        }
    }
    @Test
    @DisplayName("system manager get purchases history bad case- not system manager")
    public void PurchaseHistoryFail2() {
        String memberName = "bar1";
        String memberPassword = "password";
        try {
            registerVisitor(memberName,memberPassword);
            loginManager(managerName,managerpassword);
            market.getAllSystemPurchaseHistory(memberName);
            assert  false;
            try {
                logoutMember(managerName);
                logoutMember(memberName);
            }catch (MarketException ex){
                System.out.println(ex.getMessage());
            }
        } catch (Exception e) {
            assert true;
            try {
                logoutMember(managerName);
                logoutMember(memberName);
            } catch (MarketException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    @Test
    @DisplayName("system manager change services")
    public void changeServices() {
        try {
            loginManager(managerName,managerpassword);
            market.setPaymentServiceProxy(paymentService2, managerName,false);
            assert  true;
            try {
                logoutMember(managerName);
            }catch (MarketException ex){
                System.out.println(ex.getMessage());
            }
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("system manager change services bad case- not logged in")
    public void changeServicesFail() {
        try {
            market.setPaymentServiceProxy(paymentService2, managerName,false);
            assert false;
        } catch (MarketException e) {
            assert true;
        }
    }
    @Test
    @DisplayName("system manager change services bad case- not system manager")
    public void changeServicesFail2() {
        try {
            String memberName = "bar1";
            String memberPassword = "password";
            loginManager(managerName,managerpassword);
            market.setPaymentServiceProxy(paymentService2, "ayala" ,false);
            assert  false;
            try {
                logoutMember(managerName);
            }catch (MarketException ex){
                System.out.println(ex.getMessage());
            }
        } catch (Exception e) {
            assert true;
            try {
                logoutMember(managerName);
            } catch (MarketException ex) {
                System.out.printf(ex.getMessage());
            }
        }
    }
    public void loginManager(String managerName, String managerPass) throws MarketException {
        Visitor visitor = market.guestLogin();
        market.memberLogin(managerName,managerPass);
        market.validateSecurityQuestions(managerName, new ArrayList<>(), visitor.getName());
    }

    public void registerVisitor(String name, String pass) throws MarketException {
        // shop manager register
        Visitor visitor = market.guestLogin();
        market.register(name, pass);
        List<String> questions = market.memberLogin(name, pass);
        market.validateSecurityQuestions(name, new ArrayList<>(), visitor.getName());
    }
    public void logoutMember(String name) throws MarketException {
        market.memberLogout(name);
    }
}

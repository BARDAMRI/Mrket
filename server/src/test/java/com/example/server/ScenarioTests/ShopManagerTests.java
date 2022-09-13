package com.example.server.ScenarioTests;

import com.example.server.businessLayer.Payment.PaymentServiceProxy;
import com.example.server.businessLayer.Publisher.TextDispatcher;
import com.example.server.businessLayer.Supply.SupplyServiceProxy;
import com.example.server.businessLayer.Market.Market;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.businessLayer.Market.Users.Visitor;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ShopManagerTests {


    Market market;
    String userName = "u1";
    String password = "password";
    String shopOwnerName = "bar3";
    String shopOwnerPassword = "password";
    String managerName = "bar4";
    String managerPassword = "password";
    String shopName = "foodStore";
    int productAmount;
    Double productPrice;
    double newAmount;

    @BeforeAll
    public void setUp() {
        try {
            market = Market.getInstance();
            productAmount = 3;
            productPrice = 1.2;
            newAmount=10;
            if (market.getPaymentService() == null) {
                market.firstInitMarket(userName, password);
            }

            // shop manager register
            registerVisitor(shopOwnerName,shopOwnerPassword);
            // open shop
            registerVisitor(managerName,managerPassword);
            loginMember(shopOwnerName,shopOwnerPassword);
            openShop();
        } catch (Exception Ignored) {
        }
    }
    @Test
    @DisplayName("get shop purchase history")
    public void purchaseHistory() {
        try {
            try {
                market.appointShopManager(shopOwnerName,managerName,shopName);
            }catch (MarketException e){}
            String visitorName = market.memberLogout(shopOwnerName);
            market.memberLogin(managerName,managerPassword);
            market.validateSecurityQuestions(managerName, new ArrayList<>(), visitorName);
            String  str = new String( market.getShopPurchaseHistory(managerName,shopName));
            Assertions.assertNotNull(str);
            assert true;
        } catch (Exception e) {
            assert false;
        }
        try {
            market.memberLogout(managerName);
        }catch (MarketException ex){
            System.out.println(ex.getMessage());
        }
    }

    @Test
    @DisplayName("get shop purchase history bad case - not authorized member")
    public void purchaseHistoryFail() {
        try {
            loginMember(managerName,managerPassword);
            market.getShopPurchaseHistory(managerName,shopName);
            assert false;
            try {
                market.memberLogout(managerName);
            }catch (MarketException ex){
                System.out.println(ex.getMessage());
            }
        } catch (Exception e) {
            assert true;
            try {
                market.memberLogout(managerName);
            }catch (MarketException ex){
                System.out.println(ex.getMessage());
            }
        }
    }

    @Test
    @DisplayName("get shop purchase history bad case -not real shop")
    public void purchaseHistoryFail1() {
        try {
            loginMember(managerName,managerPassword);
            market.getShopPurchaseHistory(managerName,"not a real shop name");
            assert false;
            try {
                market.memberLogout(managerName);
            }catch (MarketException ex){
                System.out.println(ex.getMessage());
            }
        } catch (Exception e) {
            assert true;
            try {
                market.memberLogout(managerName);
            }catch (MarketException ex){
                System.out.println(ex.getMessage());
            }
        }
    }


    public void loginMember(String memberName, String memberP) throws MarketException {
        Visitor visitor = market.guestLogin();
        market.memberLogin(memberName,memberP);
        List<String> questions = market.memberLogin(memberName, memberP);
        market.validateSecurityQuestions(memberName, new ArrayList<>(), visitor.getName());
    }
    public void registerVisitor(String name, String pass) throws MarketException {
        // shop manager register
        Visitor visitor = market.guestLogin();
        market.register(name, pass);
    }

    private void openShop() throws MarketException {
        market.openNewShop(shopOwnerName, shopName);
    }
}

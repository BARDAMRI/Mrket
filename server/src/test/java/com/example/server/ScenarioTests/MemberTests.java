package com.example.server.ScenarioTests;

import com.example.server.businessLayer.Market.ResourcesObjects.MarketConfig;
import com.example.server.businessLayer.Payment.CreditCard;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.businessLayer.Payment.PaymentServiceProxy;
import com.example.server.businessLayer.Publisher.TextDispatcher;
import com.example.server.businessLayer.Supply.Address;
import com.example.server.businessLayer.Supply.SupplyServiceProxy;
import com.example.server.businessLayer.Market.Item;
import com.example.server.businessLayer.Market.Market;
import com.example.server.businessLayer.Market.ShoppingBasket;
import com.example.server.businessLayer.Market.ShoppingCart;
import com.example.server.businessLayer.Market.Users.Member;
import com.example.server.businessLayer.Market.Users.UserController;
import com.example.server.businessLayer.Market.Users.Visitor;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MemberTests {
    static Market market;
    static Member testMember;
    static String testMemberPassword;
    static String testMemberName;
    static String systemManager = "u1";
    static String userName = "userTest";
    static String password = "password";
    static String shopManagerName = "shakedMember";
    static String shopManagerPassword = "password";
    static String shopName = "kolbo2";
    static Double productAmount;
    static Double productPrice;
    static CreditCard creditCard;
    static Address address;
    static Item milk;
    static Item cookies;

    @BeforeAll
    public static void setUpMember() {
        try {
            market = Market.getInstance();
            if (market.getPaymentService() == null) {
                market.firstInitMarket("", password);
            }

            // shop manager register
            Visitor visitor = market.guestLogin();
            market.register(shopManagerName, shopManagerPassword);
            List<String> questions = market.memberLogin(shopManagerName, shopManagerPassword);
            market.validateSecurityQuestions(shopManagerName, new ArrayList<>(), visitor.getName());
            // open shop
            market.openNewShop(shopManagerName, shopName);
            productAmount = 3.0;
            productPrice = 1.2;
            List<String> keywords = new ArrayList<>();
            keywords.add("in sale");
            milk = market.addItemToShopItem(shopManagerName, "milk", productPrice, Item.Category.general,
                    "soy",keywords , productAmount,shopName);
            cookies = market.addItemToShopItem(shopManagerName, "cookies", productPrice, Item.Category.general, "",
                    keywords, productAmount, shopName);
            testMemberName = "managerTest";
            testMemberPassword = "password";
            Visitor visitor2 = market.guestLogin();
            market.register(testMemberName, testMemberPassword);
            market.memberLogin(testMemberName, testMemberPassword);
            market.validateSecurityQuestions(testMemberName, new ArrayList<>(), visitor2.getName());
            creditCard = new CreditCard("1234567890", "5","24", "555","Ido livne","204534839");            address = new Address("Bar Damri","atad 3","Tel Aviv", "Israel" , "1");


        } catch (Exception ignored) {
            System.out.printf(ignored.getMessage());
        }
    }

    @BeforeEach
    public void resetMember() {
        try {
            if (!UserController.getInstance().isLoggedIn(testMemberName)) {
                Visitor visitor = market.guestLogin();
                List<String> questions = market.memberLogin(testMemberName, testMemberPassword);
                market.validateSecurityQuestions(testMemberName, new ArrayList<>(), visitor.getName());
            }
            testMember = UserController.getInstance().getMember(testMemberName);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @AfterEach
    public void endOfMemberTest() throws Exception {
        market.memberLogout(testMember.getName());
    }

    @Test
    @DisplayName("open new shop")
    public void openNewShop() {
        try {
            String shopTest = "shopNewName";
            market.openNewShop(testMember.getName(), shopTest);
            assert true;
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("open shop with visitor - fails")
    public void visitorOpenShop() {
        // open with visitor
        try {
            Visitor visitor = market.guestLogin();
            market.openNewShop(visitor.getName(), "testHere");
            assert false;
        } catch (Exception e) {
            assert true;
        }
    }

    @Test
    @DisplayName("member opens multiple shops")
    public void openMultipleShops() {
        try {
            String shopTest = "shopTest2";
            market.openNewShop(testMember.getName(), shopTest);
            assert true;
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("open shop with no name  - fails")
    public void openShopWithNoName() {
        try {
            String shopTest = "";
            market.openNewShop(testMember.getName(), shopTest);
            assert false;
        } catch (Exception e) {
            assert true;
        }

    }

    @Test
    @DisplayName("open shop with used name")
    public void usedNameOpenShop() {
        try {
            String shopTest = "shopTest";
            market.openNewShop(testMember.getName(), shopTest);
            market.openNewShop(testMember.getName(), shopTest);
            assert false;
        } catch (Exception e) {
            assert true;
        }
    }

    @Test
    @DisplayName("logout - check member saved")
    public void checkMemberSaved() {
        try {
            market.addItemToShoppingCart(milk, productAmount-1,testMemberName);
            testMember = UserController.getInstance().getMember(testMemberName);
            ShoppingCart prevCart = testMember.getMyCart();
            String visitorName = market.memberLogout(testMember.getName());
            Visitor visitor = UserController.getInstance().getVisitor(visitorName);
            List<String> questions = market.memberLogin(testMember.getName(), testMemberPassword);
            Member returnedMember = market.validateSecurityQuestions(testMember.getName(), new ArrayList<>(),
                    visitorName);
            Assertions.assertEquals(returnedMember.getAppointedByMe(), testMember.getAppointedByMe());
            Assertions.assertEquals(returnedMember.getName(), testMember.getName());
            Assertions.assertEquals(returnedMember.getMyAppointments(), testMember.getMyAppointments());
            if (!(testMember.getMyCart() == returnedMember.getMyCart())) {
                assert testMember.getMyCart().getCart().size() == returnedMember.getMyCart().getCart().size();
                // for each shop - check equals
                prevCart.getCart().forEach((shop, prevBasket) -> {
                    assert returnedMember.getMyCart().getCart().containsKey(shop);
                    ShoppingBasket newBasket = returnedMember.getMyCart().getCart().get(shop);
                    // for each item in shopping basket
                    prevBasket.getItems().forEach((item, amount) -> {
                        assert newBasket.getItems().size() == prevBasket.getItems().size();
                        assert newBasket.getItems().containsKey(item);
                        assert newBasket.getItems().get(item).equals(amount);
                    });
                });
            }
            // for each shopping basket - checks equals
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    @DisplayName("login twice")
    public void loginTwice() {
        try {
            Visitor visitor = market.guestLogin();
            List<String> questions = market.memberLogin(testMemberName, testMemberPassword);
            Member newMember = market.validateSecurityQuestions(testMemberName, new ArrayList<>(), visitor.getName());
            Assertions.assertNotNull(newMember);
            visitor = market.guestLogin();
            questions = market.memberLogin(testMemberName, testMemberPassword);
            newMember = market.validateSecurityQuestions(testMemberName, new ArrayList<>(), visitor.getName());
            assert false;
        } catch (Exception e) {
            assert true;
        }
    }

    @Test
    @DisplayName("member exit system logs out")
    public void memberExitSystem() {
        try {
            market.visitorExitSystem(testMember.getName());
            Visitor visitor = market.guestLogin();
            List<String> questions = market.memberLogin(testMemberName, testMemberPassword);
            testMember = market.validateSecurityQuestions(testMemberName, new ArrayList<>(), visitor.getName());
            assert testMember != null;
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("add question")
    public void addQuestion() {
        try {
            Visitor visitor = market.guestLogin();
            String currName = "questionsName";
            String password = "password";
            market.register(currName, password);
            List<String> questions = market.memberLogin(currName, password);
            assert questions.size() == 0;
            Member member = market.validateSecurityQuestions(currName, new ArrayList<>(), visitor.getName());
            market.addPersonalQuery("whats your mother's name?", "idk", member.getName());
            String visitorName = market.memberLogout(member.getName());
            questions = market.memberLogin(currName, password);
            assert questions.size() == 1;
            assert questions.contains("whats your mother's name?");
            ArrayList<String> answers = new ArrayList<>();
            answers.add("idk");
            member = market.validateSecurityQuestions(currName, answers, member.getName());
            assert member != null;

        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("invalid authentication")
    public void invalidAuthentication() {
        try {
            Visitor visitor = market.guestLogin();
            String currName = "questionsName2";
            String password = "password";
            market.register(currName, password);
            List<String> questions = market.memberLogin(currName, password);
            Member member = market.validateSecurityQuestions(currName, new ArrayList<>(), visitor.getName());
            market.addPersonalQuery("whats your mother's name?", "idk", member.getName());
            String visitorName = market.memberLogout(member.getName());
            try {
                questions = market.memberLogin(currName, "123");
                assert false;
            }catch (MarketException e){
                assert true;
            }
            ArrayList<String> answers = new ArrayList<>();
            answers.add("idk1");
            try {
                member = market.validateSecurityQuestions(currName, answers, member.getName());
            } catch (Exception ignored) {
                assert true;
            }
            answers.clear();
            answers.add("idk");
            member = market.validateSecurityQuestions(currName, answers, member.getName());
            Assertions.assertNotNull(member);
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("login-addToCart-logout-addToCart-buy")
    public void loginAndLogoutSenario() {
        String memberName = "Kim";
        String password = "password";
        Visitor visitor = market.guestLogin();
        try {
            market.register(memberName, password);
            market.memberLogin(memberName, password);
            Member member = market.validateSecurityQuestions(memberName, null, visitor.getName());
            market.addItemToShoppingCart(milk, 1, memberName);
            String visitorName = market.memberLogout(memberName);
            market.addItemToShoppingCart(cookies, 1, visitorName);
            visitor = UserController.getInstance().getVisitor(visitorName);
            assert visitor.getCart().getItemQuantity(milk) == 0;
            assert visitor.getCart().getItemQuantity(cookies) == 1;
            market.memberLogin(memberName, password);
            member = market.validateSecurityQuestions(memberName, null, visitorName);
            assert member.getMyCart().getItemQuantity(milk) == 1;
            assert member.getMyCart().getItemQuantity(cookies) == 0;
        }catch (MarketException e){
            assert false;
        }
    }



}

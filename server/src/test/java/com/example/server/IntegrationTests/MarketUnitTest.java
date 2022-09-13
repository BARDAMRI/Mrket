package com.example.server.IntegrationTests;


import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.businessLayer.Payment.PaymentServiceProxy;
import com.example.server.businessLayer.Publisher.NotificationDispatcher;
import com.example.server.businessLayer.Security.Security;
import com.example.server.businessLayer.Supply.SupplyServiceProxy;
import com.example.server.businessLayer.Market.*;
import com.example.server.businessLayer.Market.Users.Member;
import com.example.server.businessLayer.Market.Users.UserController;
import com.example.server.businessLayer.Market.Users.Visitor;

import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MarketUnitTest {
    Member member;
    Visitor notMemberVisitor;
    Visitor memberVisitor;
    UserController userController;
    ClosedShopsHistory shopsHistory;
    Shop shop;
    Market market;
    String memberName;
    String visitorName;
    String shopName;
    Security security;
    Item item;

    @BeforeAll
    public static void init(){
        PaymentServiceProxy paymentService = new PaymentServiceProxy();
        SupplyServiceProxy supplyService = new SupplyServiceProxy();
        try{
            Market.getInstance().firstInitMarket("Ido","password");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @BeforeEach
    public void marketUnitTestInit(){
        try {
            Market.getInstance().reset("password", null, null, false);
        } catch (MarketException e){}
        List<String> keywords = new ArrayList<>();
        keywords.add("dairy");
        shopsHistory = ClosedShopsHistory.getInstance();
        market = Market.getInstance();
        security = Security.getInstance();
        userController = UserController.getInstance();
        try {
            market.register("raz","password");
            market.register("ayala","password");
            market.memberLogin("raz","password");
            market.validateSecurityQuestions("raz",new ArrayList<>(),"@visitor1");
            market.openNewShop("raz","razShop");
            shop = market.getShopByName("razShop");
            item = market.addItemToShopItem("raz","milk", 5.0, Item.Category.general,"",keywords,10.0,"razShop");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }

    }


    @Test
    @DisplayName("First init market - fail test - one of the external service is null")
    public void initFailTest(){
        SupplyServiceProxy supplyService = new SupplyServiceProxy();
        try{
            market.firstInitMarket("raz","password");
            assert false;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            assert true;
        }
    }
    @Test
    @DisplayName("First init market - fail test - one service already exist")
    public void initFailTestOneServiceIsNotNull(){
        try {
            market.setPaymentServiceProxy(new PaymentServiceProxy(), "raz",false);
        } catch (MarketException e) {}
        PaymentServiceProxy paymentService = new PaymentServiceProxy();
        SupplyServiceProxy supplyService = new SupplyServiceProxy();
        try{
            market.firstInitMarket("raz","password");
            assert false;
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            assert true;
        }
    }

    @Test
    @DisplayName("Register test - good test")
    public void registerTest(){
        try {
            market.register("ido", "password");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            assert false;
        }
    }
    @Test
    @DisplayName("Register test - fail test - valid")
    public void registerFailTest(){
        try {
            market.register("raz", "password");
            assert false;
        }
        catch (MarketException e){
            assert true;
        }catch (Exception e){
            System.out.printf(e.getMessage());
            assert false;
        }
    }
    @Test
    @DisplayName("Member login - good test")
    public void MemberLoginTest(){
        try {
            market.register("ido","password");
            market.memberLogin("ido","password");
            assert true;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            assert false;
        }
    }

    @Test
    @DisplayName("Guest login")
    public void guestLogin(){
        Visitor visitor = userController.guestLogin();
        Assertions.assertNotNull(visitor);
    }

    @Test
    @DisplayName("Calculate shopping cart - good test")
    public void CalculateCart(){
        try{
            market.addItemToShoppingCart(item,1.0,"raz");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            System.out.println("End of test");
            System.out.println("-------------------------------------------------------------");
            assert false;
        }
        try {
            ShoppingCart updatedCart = market.calculateShoppingCart("raz");
            Assertions.assertEquals(5.0,updatedCart.getCurrentPrice());
            System.out.println("End of test");
            System.out.println("-------------------------------------------------------------");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("End of test");
            System.out.println("-------------------------------------------------------------");
            assert false;
        }


    }

    @Test
    @DisplayName("Get item by name test")
    public void GetItemByNameTest(){
        List<String> keywords = new ArrayList<>();
        keywords.add("fruit");
        try {
            Item item1 = new Item(2,"strawberry",2.5,"red", Item.Category.fruit,
            keywords);
//            market.addItemToShop("raz",item.getName(),item.getPrice(),item.getCategory(),"",
//                    item.getKeywords(),5.0,"razShop");
            market.addItemToShop("raz",item1.getName(),item1.getPrice(),item1.getCategory(),item1.getInfo(),
                    item1.getKeywords(),5.0,"razShop");
            market.openNewShop("raz","razShop2");
            market.addItemToShop("raz",item.getName(),item.getPrice(),item.getCategory(),"On shop 2 we have info",item.getKeywords(),3.0,"razShop2");

        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert false;
        }
        List<Item> res = market.getItemByName("milk");
        Assertions.assertEquals(2,res.size());
        Assertions.assertEquals("",res.get(0).getInfo());
        Assertions.assertEquals("On shop 2 we have info",res.get(1).getInfo());
        System.out.println("End of test");
        System.out.println("-------------------------------------------------------------");


    }

    @Test
    @DisplayName("Get Item By Category")
    public void getItemByCategoryTest(){
        List<String> keywords = new ArrayList<>();
        keywords.add("fruit");
        Item item1 = null;
        try {
            item1 = market.addItemToShopItem("raz","strawberry",2.5, Item.Category.fruit, "red strawberry",
                    keywords,5.0,"razShop");
            market.openNewShop("raz","razShop2");
            Item item2 = market.addItemToShopItem("raz",item.getName(),item.getPrice(),item.getCategory(),"On shop 2 we have info",item.getKeywords(),3.0,"razShop2");

        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert false;
        }
        List<Item> res = market.getItemByCategory(item1.getCategory());
        Assertions.assertEquals(1,res.size());
        Assertions.assertEquals("red strawberry",res.get(0).getInfo());
        System.out.println("End of test");
        System.out.println("-------------------------------------------------------------");
    }
    @Test
    @DisplayName("Add Personal Query - good test")
    public void addPersonalQueryTest(){
        String q = "What is your birth year?";
        String a = "1995";
        try {
            market.addPersonalQuery(q,a,"raz");
            Assertions.assertEquals(1,security.getNamesToLoginInfo().get("raz").getQandA().size());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert false;
        }
    }
    @Test
    @DisplayName("Add Personal Query - fail Test")
    public void addPersonalQueryFailTest(){
        String q = "What is your birth year?";
        String a = "1995";
        try {
            market.addPersonalQuery(q,a,"notMember");
            assert false;

        } catch (Exception e) {
            try {
                market.addPersonalQuery(q,null,"raz");

                System.out.println("End of test");
                System.out.println("-------------------------------------------------------------");
                assert false;
            } catch (Exception ex) {
                System.out.println(ex.getMessage());

                System.out.println("End of test");
                System.out.println("-------------------------------------------------------------");
                assert true;
            }
        }
    }
    @Test
    @DisplayName("Validate Security questions - good test")
    public void validateSecurityQuestionsTest(){
        String q = "What is your birth year?";
        String a = "1995";
        try {
            market.addPersonalQuery(q,a,"raz");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert false;
        }
        List<String> ans = new ArrayList<>();
        ans.add("1995");
        try {
            market.memberLogout("raz");
        } catch (MarketException e) {
            System.out.println(e.getMessage());
        }
        try {
            Member test= market.validateSecurityQuestions("raz",ans,"@visitor1");
            Assertions.assertNotNull(test);

            System.out.println("End of test");
            System.out.println("-------------------------------------------------------------");
            assert true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("End of test");
            System.out.println("-------------------------------------------------------------");
            assert false;
        }

    }
    @Test
    @DisplayName("Validate Security Questions - fail test - empty answer list")
    public void validateSecurityQuestionsFailTestEmptyAnswerList(){
        String q = "What is your birth year?";
        String a = "1995";
        try {
            market.addPersonalQuery(q,a,"raz");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert false;
        }
        List<String> ans = new ArrayList<>();
        ans.add("1995");
        try {
            Member test= market.validateSecurityQuestions("raz",new ArrayList<>(),"@visitor1");
            System.out.println("End of test");
            System.out.println("-------------------------------------------------------------");
            assert false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("End of test");
            System.out.println("-------------------------------------------------------------");
            assert true;
        }
    }
    @Test
    @DisplayName("Validate Security Questions - fail test - wrong answers")
    public void validateSecurityQuestionsFailTest(){
        String q = "What is your birth year?";
        String a = "1995";
        try {
            market.addPersonalQuery(q,a,"raz");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert false;
        }
        List<String> ans = new ArrayList<>();
        ans.add("1996");
        try {
            market.validateSecurityQuestions("raz",ans,"@visitor1");
            System.out.println("End of test");
            System.out.println("-------------------------------------------------------------");
            assert false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("End of test");
            System.out.println("-------------------------------------------------------------");
            assert true;
        }
    }
    @Test
    @DisplayName("Visitor Exit System - good test")
    public void visitorExitSystemTest(){
        try
        {
            market.visitorExitSystem("raz");
            assert true;

            System.out.println("-------------------------------------------------------------");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            assert false;

            System.out.println("-------------------------------------------------------------");
        }
    }
    @Test
    @DisplayName("Visitor Exit System- fail test - not a visitor")
    public void visitorExitSystemFailTest(){
        try
        {
            market.visitorExitSystem("notVisitor");
            assert false;

            System.out.println("-------------------------------------------------------------");
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            assert true;

            System.out.println("-------------------------------------------------------------");
        }
    }
    @Test
    @DisplayName("Close Shop test -good test")
    public void closeShopTest(){
        try {
            market.closeShop("raz","razShop");
            Assertions.assertEquals(1,shopsHistory.getClosedShops().size());
            Assertions.assertTrue(shopsHistory.getClosedShops().containsKey("razShop"));
        } catch (MarketException e) {
            System.out.println(e.getMessage());
            assert false;
            System.out.println("-----------------------------------");
        }
    }
    @Test
    @DisplayName("Close Shop test - fail test - not logged in")
    public void closeShopFailTest(){
        try {
            market.closeShop("ayala","razShop");
            assert false;
        } catch (MarketException e) {
            try {
                System.out.println(e.getMessage());
                market.closeShop("raz","razShopTBO");
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                assert true;
            }
            System.out.println("-----------------------------------");
        }
    }
    @Test
    @DisplayName("Remove Item From Shop")
    public void removeItemFromShopTest(){
        Assertions.assertEquals(1,shop.getItemMap().size());
        try {
            market.removeItemFromShop("raz",item.getID(),"razShop");
            Assertions.assertEquals(0,shop.getItemMap().size());
            System.out.println("-----------------------------------------------");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert false;
            System.out.println("-----------------------------------------------");
        }
    }
    @Test
    @DisplayName("Remove Item From Shop-fail test ")
    public void removeItemFromShopFailTest() {
        Assertions.assertEquals(1, shop.getItemMap().size());
        try {
            market.removeItemFromShop("razb", item.getID(), "razShop");//No such member razb
            assert false;
            System.out.println("-----------------------------------------------");
        } catch (Exception e) {
            try {
                market.removeItemFromShop("raz", 10, "razShop");//no such item with ID 10
                assert false;
            } catch (Exception ex) {
                try {
                    market.removeItemFromShop("raz",item.getID(),"NoShop");//No such shop with name NoShop
                    assert false;
                } catch (Exception exc) {
                    assert true;
                }
            }
            System.out.println("-----------------------------------------------");
        }
    }

    @Test
    @DisplayName("Add Item To Shop -good test")
    public void addItemToShopTest(){
        try {
            Assertions.assertEquals(1,shop.getItemMap().size());
            market.addItemToShop("raz","soap",10.0, Item.Category.general,"Muy kef",new ArrayList<>(),50.0,"razShop");
            Assertions.assertEquals(2,shop.getItemMap().size());
            Assertions.assertEquals(3,market.getNextItemID());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert false;
        }
    }

    @Test
    @DisplayName("Add Item To Shop - fail test")
    public void addItemToShopFailTest(){
        try {
            market.addItemToShop("razb","soap",10.0, Item.Category.general,"Muy kef",new ArrayList<>(),50.0,"razShop");
            assert false;
            //No such member
        } catch (Exception e) {
            try {
                market.addItemToShop("raz","soap",10.0, Item.Category.general,"Muy kef",new ArrayList<>(),50.0,"NullShop");
                assert false;
            } catch (Exception ex) {
                Assertions.assertEquals(2,market.getNextItemID());
            }
        }
    }
    @Test
    @DisplayName("Set Item Current Amount - good test")
    public void setItemCurrentAmountTest(){
        try {
            market.setItemCurrentAmount("raz",item,3.0,"razShop");
            Assertions.assertEquals(3.0,shop.getItemCurrentAmount(item));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert false;
        }
    }
    @Test
    @DisplayName("Set Item Current Amount - fail test")
    public void setItemCurrentAmountFailTest(){
        try {
            market.setItemCurrentAmount("raz",item,-3.0,"razShop");
            assert false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert true;
        }
    }
    @Test
    @DisplayName("Member Logout-good test")
    public void memberLogoutTest(){
        try {
            market.memberLogout("raz");
            assert true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert false;
        }
    }
    @Test
    @DisplayName("Member Logout- fail test")
    public void memberLogoutFailTest(){
        try {
            market.memberLogout("Ido");
            assert false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert true;
        }
    }
    
    @Test
    @DisplayName("Get Shop Info Test")
    public void getShopInfoTest(){
        try {
            Shop test = market.getShopInfo("ayala","razShop");
            assert false;
        } catch (Exception e) {
            try {
                Shop test = market.getShopInfo("raz","razShop");
                Assertions.assertNotNull(test);
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                assert false;
            }
        }
    }
    @Test
    @DisplayName("Open new Shop - good test")
    public void openNewShopTest(){
        try {
            market.openNewShop("raz","razShop2");
            Assertions.assertEquals(2,market.getShops().size());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert false;
        }
    }
    @Test
    @DisplayName("Open new Shop - fail test")
    public void openNewShopFailTest(){
        try {
            market.openNewShop("razb","razShop2");//no such member
            assert false;
        } catch (Exception e) {
            try {
                market.openNewShop("raz","razShop");//taken name
                assert false;
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
                assert true;
            }
        }
    }
    @Test
    @DisplayName("Add Item To Shopping Cart - good test")
    public void addItemToShoppingCartTest(){
        try {
            market.addItemToShoppingCart(item,5.0,"raz");
            Member raz = userController.getMember("raz");
            Map<Shop,ShoppingBasket> cartRaz = raz.getMyCart().getCart();
            Assertions.assertEquals(1,cartRaz.size());
            Assertions.assertEquals(5.0,cartRaz.get(shop).getItems().get(item.getID()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert false;
        }
    }
    @Test
    @DisplayName("Add Item To Shopping Cart - fail test")
    public void addItemToShoppingCartFailTest() {
        try {
            market.addItemToShoppingCart(null, 5.0, "raz");//No item
            assert false;
        } catch (Exception e) {
            try {
                market.addItemToShoppingCart(item, -5.0,  "raz");// negative amount
                assert false;
            } catch (Exception ex) {
                try {
                    market.addItemToShoppingCart(null, 5.0,  "ayala");//member not logged in
                    assert false;
                } catch (Exception Exce) {
                    try {
                        market.addItemToShoppingCart(null, 15.0,  "raz");// amount large shop amount
                        assert false;
                    } catch (Exception Excep) {
                        assert true;
                    }
                }
            }
        }

    }

    @Test
    @DisplayName("Appoint Shop Owner - good test")
    public void appointShopOwnerTest(){
        try {
            market.appointShopOwner("raz","ayala","razShop");
            Assertions.assertEquals(2,market.getShops().get("razShop").getShopOwners().size());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert false;
        }
    }
    @Test
    @DisplayName("Appoint shop owner - fail test")
    public void appointShopOwnerFailTest(){
        try {
            market.appointShopOwner("raz","NotMember","razShop");
            assert false;
        } catch (Exception e) {
            try {
                market.appointShopOwner("ayala","raz","razShop");
                assert false;
            } catch (Exception ex) {
                assert true;
            }
        }
    }
    @Test
    @DisplayName("Appoint shop manager - good test")
    public void appointShopManagerTest(){
        try {
            market.appointShopManager("raz","ayala","razShop");
            Assertions.assertEquals(1,market.getShops().get("razShop").getShopManagers().size());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert false;
        }

    }
    @Test
    @DisplayName("Appoint shop manager - fail test")
    public void appointShopManagerFailTest(){ // testing : no member , member who is not owner
        try {
            market.appointShopManager("raz","NotMember","razShop");
            assert false;
        } catch (Exception e) {
            try {
                market.appointShopManager("ayala","moshe","razShop");
                assert false;
            } catch (Exception ex) {
                assert true;
            }
        }
    }

    @Test
    @DisplayName("Appoint shop manager - fail test - owner not logged in")
    public void appointShopManagerFailTestOwnerLoggedOut(){
        try {
            market.memberLogout("raz");
        } catch (Exception e) {
            System.out.println("Build up for test: Appoint shop manager -owner logged out has failed.");
            assert false;
        }
        try {
            market.appointShopManager("raz","ayala","razShop");
            assert false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert true;
        }


    }
    @Test
    @DisplayName("Edit cart - good test")
    public void editCartTest(){
        try {
            market.addItemToShoppingCart(item,5.0,"raz");
        } catch (Exception e) {
            System.out.println("Build up for test:Edit cart  has failed");
            assert false;
        }
        try {
            market.editCart(2.0,item,"razShop","raz");
            Member raz = userController.getMember("raz");
            Map<Shop, ShoppingBasket> razCart = raz.getMyCart().getCart();
            ShoppingBasket razShopBasket = razCart.get(shop);
            Assertions.assertEquals(2.0,razShopBasket.getItems().get(item.getID()));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert false;

        }
    }
    @Test
    @DisplayName("Edit cart - fail test")
    public void editCartFailTest(){
        try {
            market.editCart(3.0,item,"razShop","moshe");
            assert false;
        } catch (Exception exc) {
            try {
                market.editCart(3.0,item,"razShop","ayala");
                assert false;
            } catch (MarketException marketException) {
                assert true;
            }
        }
    }
    @Test
    @DisplayName("Edit Item - good test")
    public void editItemTest(){
        try {
            double oldPrice = item.getPrice();
            Item updated = new Item(item.getID(),item.getName(),item.getPrice()+1,item.getInfo(),item.getCategory(),item.getKeywords());
            market.editItem(updated,item.getID().toString());
            String shopName = market.getAllItemsInMarketToShop().get(item.getID());
            Shop testShop= market.getShops().get(shopName);
            updated = testShop.getItemMap().get(item.getID());
            Assertions.assertNotEquals(oldPrice,updated.getPrice());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert false;
        }

    }

    @Test
    @DisplayName("Edit Item - fail test")
    public void editItemFailTest(){
        try {
            market.editItem(item,"20");
            assert false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert true;
        }
    }

    @Test
    @DisplayName("Buy Shopping Cart - good test")
    public void buyShoppingCartTest(){}//TODO
    @Test
    @DisplayName("Buy Shopping Cart - fail test")
    public void buyShoppingCartFailTest(){}//TODO

    @Test
    @DisplayName("Remove shop owner - good test - valid shop")
    public void removeShopOwnerTest(){
        try {
            market.appointShopOwner("raz","ayala","razShop");
        } catch (Exception e) {
            assert false;
        }
        try{
            market.removeShopOwnerAppointment("raz","ayala","razShop");
            assert true;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            assert false;
        }
    }

    @Test
    @DisplayName("Remove shop owner - fail test - no such shop")
    public void removeShopOwnerFailTest(){
        try {
            market.appointShopOwner("raz","ayala","razShop");
        } catch (Exception e) {
            assert false;
        }
        try{
            market.removeShopOwnerAppointment("raz","ayala","stamShop");
            assert false;
        }
        catch (MarketException e)
        {
            System.out.println(e.getMessage());
            assert true;
        }
        catch (Exception ex)
        {
            System.out.println(ex.getMessage());
            assert false;
        }
    }

    @Test
    @DisplayName("Remove member - good test")
    public void removeMemberTest(){
        try {
            market.register("shaked", "password");
            market.appointShopOwner("raz", "ayala", "razShop");
        }
        catch (Exception e)
        {
            assert false;
        }
        try {
            market.removeMember(market.getSystemManagerName(),"shaked");
            assert true;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            assert false;
        }
    }
    @Test
    @DisplayName("Remove member - fail test - trying to remove employee")
    public void removeMemberFailTestRemoveEmployee(){
        try {
            market.register("shaked", "password");
            market.appointShopOwner("raz", "ayala", "razShop");
        }
        catch (Exception e)
        {
            assert false;
        }
        try {
            market.removeMember("Ido","ayala");
            assert false;
        }
        catch (MarketException e)
        {
            assert true;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            assert false;
        }
    }
    @Test
    @DisplayName("Remove member - fail test - trying to remove system manager")
    public void removeMemberFailTestRemoveSysManager(){
        try {
            market.register("shaked", "password");
            market.appointShopOwner("raz", "ayala", "razShop");
        }
        catch (Exception e)
        {
            assert false;
        }
        try {
            market.removeMember("raz","Ido");
            assert false;
        }
        catch (MarketException e)
        {
            assert true;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            assert false;
        }
    }
    @Test
    @DisplayName("Remove member - fail test - trying to remove myself")
    public void removeMemberFailTestRemoveMyself(){
        try {
            market.register("shaked", "password");
            market.appointShopOwner("raz", "ayala", "razShop");
        }
        catch (Exception e)
        {
            assert false;
        }
        try {
            market.removeMember("Ido","Ido");
            assert false;
        }
        catch (MarketException e)
        {
            assert true;
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            assert false;
        }
    }
    @Test
    @DisplayName("Reopen closed shop - valid case")
    public void reopenShop(){
        try {
            market.closeShop("raz","razShop");
        } catch (MarketException e) {
            assert false;
        }
        try {
            market.reopenClosedShop("razShop","raz");
            Assertions.assertFalse(shop.isClosed());
        } catch (MarketException e) {
            assert false;
        }
    }
    @Test
    @DisplayName("Reopen shop - Invalid case - shop is not closed.")
    public void reopenOpenShop(){
        try {
            market.reopenClosedShop("razShop","raz");
            assert false;
        } catch (MarketException e) {
            assert true;
        }
    }

    @Test
    @DisplayName("Reopen shop - Invalid case - non existing shop.")
    public void reopenNonExistingShop(){
        try {
            market.closeShop("raz","razShop");
        } catch (MarketException e) {
            assert false;
        }
        try {
            market.reopenClosedShop("razrazShop","raz");
            assert false;
        } catch (MarketException e) {
            assert true;
        }
    }

    @Test
    @DisplayName("Reopen shop - Invalid case - member is not founder.")
    public void reopenShopNotFounder(){
        try {
            market.closeShop("raz","razShop");
        } catch (MarketException e) {
            assert false;
        }
        try {
            market.reopenClosedShop("razShop","notRaz");
            assert false;
        } catch (MarketException e) {
            assert true;
        }
    }
    @Test
    @DisplayName("Reopen shop and look for item")
    public void reopenShopAndLookForItemTest(){
        try{
            market.closeShop("raz","razShop");
            List<Item> items = market.getItemByName("milk");
            Assertions.assertEquals(0,items.size());
            market.reopenClosedShop("razShop","raz");
            items = market.getItemByName("milk");
            Assertions.assertEquals(1,items.size());
        }
        catch (MarketException e){
            assert false;
        }
    }
    @Test
    @DisplayName("Add a bid - good test.")
    public void addBidTest(){
        try {
            market.memberLogin("ayala","password");
            userController.finishLogin("ayala","@visitor1");
            market.addABid("ayala","razShop",item.getID(),3.0,1.0);
        } catch (MarketException e) {
            assert false;
        }
        member = userController.getMember("ayala");
        ShoppingCart ayalaCart = member.getMyCart();
        ShoppingBasket ayalaShopBasket = ayalaCart.getCart().get(shop);
        Assertions.assertEquals(1,ayalaShopBasket.getBids().size());
        Assertions.assertEquals(1,shop.getBids().size());
    }
    @Test
    @DisplayName("Add a bid - fail test - No such shop.")
    public void addBidFailNoSuchShopTest(){
        try {
            market.addABid("raz","razrazShop",item.getID(),3.0,1.0);
            assert false;
        } catch (MarketException e) {
            assert true;
        }
    }
    @Test
    @DisplayName("Add a bid - fail test - NULL item.")
    public void addBidFailNullItemTest(){
        try {
            market.addABid("raz","razrazShop",null,3.0,1.0);
            assert false;
        } catch (MarketException e) {
            assert true;
        }
    }
    @Test
    @DisplayName("Approve a bid - good test")
    public void approveBidTest(){
        try {
            market.memberLogin("ayala","password");
            userController.finishLogin("ayala","@visitor1");
            market.addABid("ayala","razShop",item.getID(),3.0,1.0);
        } catch (MarketException e) {
            assert false;
        }
        try {
            market.approveABid("raz","razShop","ayala",item.getID());
            member = userController.getMember("ayala");
            ShoppingCart ayalaCart = member.getMyCart();
            ShoppingBasket ayalaShopBasket = ayalaCart.getCart().get(shop);
            Bid bid = ayalaShopBasket.getBids().get(1);
            Assertions.assertTrue(bid.isApproved());
        } catch (MarketException e) {
            assert false;
        }
    }
    @Test
    @DisplayName("Approve bid no such shop in market")
    public void approveBidNoSuchShop(){
        try {
            market.memberLogin("ayala","password");
            userController.finishLogin("ayala","@visitor1");
            market.addABid("ayala","razShop",item.getID(),3.0,1.0);
        } catch (MarketException e) {
            assert false;
        }
        try {
            market.approveABid("raz","razrazShop","ayala",item.getID());
            assert false;
        } catch (MarketException e) {
            assert true;
        }
    }
    @Test
    @DisplayName("Approve bid no such bid in the shop")
    public void approveBidNoSuchBidInTheShop(){
        try {
            market.openNewShop("raz","razShop2");
            market.memberLogin("ayala","password");
            userController.finishLogin("ayala","@visitor1");
            market.addABid("ayala","razShop",item.getID(),3.0,1.0);
        } catch (MarketException e) {
            assert false;
        }
        try {
            market.approveABid("raz","razShop2","ayala",item.getID());
            assert false;
        } catch (MarketException e) {
            assert true;
        }
    }
    @Test
    @DisplayName("Suggest new offer to bid - good test")
    public void suggestNewOfferTest(){
        try {
            market.openNewShop("raz","razShop2");
            market.memberLogin("ayala","password");
            userController.finishLogin("ayala","@visitor1");
            market.addABid("ayala","razShop",item.getID(),3.0,1.0);
        } catch (MarketException e) {
            assert false;
        }
        try {
            market.suggestNewOfferToBid("raz","razShop","ayala",item.getID(),4.0);
            member = userController.getMember("ayala");
            ShoppingCart ayalaCart = member.getMyCart();
            ShoppingBasket ayalaShopBasket = ayalaCart.getCart().get(shop);
            Bid bid = ayalaShopBasket.getBids().get(1);
            Assertions.assertEquals(4.0,bid.getPrice());
        } catch (MarketException e) {
            assert false;
        }
    }
    @Test
    @DisplayName("Suggest new offer to bid - fail test - No such shop")
    public void suggestNewOfferFailTestNoShop(){
        try {
            market.memberLogin("ayala","password");
            userController.finishLogin("ayala","@visitor1");
            market.addABid("ayala","razShop",item.getID(),3.0,1.0);
        } catch (MarketException e) {
            assert false;
        }
        try {
            market.suggestNewOfferToBid("raz","razShop2","ayala",item.getID(),4.0);
            assert false;
        } catch (MarketException e) {
            assert true;
        }
    }
    @Test
    @DisplayName("Reject bid - good test")
    public void rejectBidTest(){
        try {
            market.memberLogin("ayala","password");
            userController.finishLogin("ayala","@visitor1");
            market.addABid("ayala","razShop",item.getID(),3.0,1.0);
        } catch (MarketException e) {
            assert false;
        }
        try {
            market.rejectABid("raz","razShop","ayala",item.getID());
            member = userController.getMember("ayala");
            ShoppingCart ayalaCart = member.getMyCart();
            ShoppingBasket ayalaShopBasket = ayalaCart.getCart().get(shop);
            Assertions.assertEquals(0,ayalaShopBasket.getBids().size());

        } catch (MarketException e) {
            assert false;
        }
    }
    @Test
    @DisplayName("Reject bid - fail test - No such shop")
    public void rejectBidFailTest(){
        try {
            market.memberLogin("ayala","password");
            userController.finishLogin("ayala","@visitor1");
            market.addABid("ayala","razShop",item.getID(),3.0,1.0);
        } catch (MarketException e) {
            assert false;
        }
        try {
            market.rejectABid("raz","razrazShop","ayala",item.getID());
            assert false;
        } catch (MarketException e) {
            assert true;
        }
    }
    @Test
    @DisplayName("Cancel a bid - good test")
    public void cancelBidTest(){
        try {
            market.memberLogin("ayala","password");
            userController.finishLogin("ayala","@visitor1");
            market.addABid("ayala","razShop",item.getID(),3.0,1.0);
        } catch (MarketException e) {
            assert false;
        }
        try {
            market.cancelABid("razShop","ayala",item.getID());
            member = userController.getMember("ayala");
            ShoppingCart ayalaCart = member.getMyCart();
            ShoppingBasket ayalaShopBasket = ayalaCart.getCart().get(shop);
            Assertions.assertEquals(0,ayalaShopBasket.getBids().size());

        } catch (MarketException e) {
            assert false;
        }
    }
    @Test
    @DisplayName("Approve appointment - good test")
    public void approveAppointment(){
        try {
            market.register("ido","password");
            market.appointShopOwner("raz","ayala","razShop");
            //automatically approved because raz is the only owner.
            market.appointShopOwner("raz","ido","razShop");
        } catch (MarketException e) {
            assert false;
        }
        try {
            market.approveAppointment("razShop","ido","raz");
            Assertions.assertEquals(2,shop.getShopOwners().size());//waiting for ayala's approval
            market.approveAppointment("razShop","ido","ayala");
            Assertions.assertEquals(3,shop.getShopOwners().size());

        } catch (MarketException e) {
            assert false;
        }
    }
    @Test
    @DisplayName("Approve appointment - fail test - No such shop")
    public void approveAppointmentFailTestNoShop(){
        try {
            market.register("ido","password");
            market.appointShopOwner("raz","ayala","razShop");
            //automatically approved because raz is the only owner.
            market.appointShopOwner("raz","ido","razShop");
        } catch (MarketException e) {
            assert false;
        }
        try {
            market.approveAppointment("razShop2","ido","raz");
            assert false;

        } catch (MarketException e) {
            assert true;
        }
    }
    @Test
    @DisplayName("Approve appointment - fail test - Not shop owner")
    public void approveAppointmentFailTestNotOwner(){
        try {
            market.register("ido","password");
            market.appointShopOwner("raz","ayala","razShop");
            //automatically approved because raz is the only owner.
            market.appointShopOwner("raz","ido","razShop");
        } catch (MarketException e) {
            assert false;
        }
        try {
            market.approveAppointment("razShop","ido","razraz");
            assert false;

        } catch (MarketException e) {
            assert true;
        }
    }
    @Test
    @DisplayName("Approve appointment - fail test - No such appointment for member")
    public void approveAppointmentFailTestNotAppointed(){
        try {
            market.register("ido","password");
            market.appointShopOwner("raz","ayala","razShop");
            //automatically approved because raz is the only owner.
            market.appointShopOwner("raz","ido","razShop");
        } catch (MarketException e) {
            assert false;
        }
        try {
            market.approveAppointment("razShop","idoo","raz");
            assert false;

        } catch (MarketException e) {
            assert true;
        }
    }
    @Test
    @DisplayName("Approve appointment - fail test - Non existing member")
    public void approveAppointmentFailTestNonMember(){
        try {
            market.register("ido","password");
            market.appointShopOwner("raz","ayala","razShop");
            //automatically approved because raz is the only owner.
            market.appointShopOwner("raz","ido","razShop");
        } catch (MarketException e) {
            assert false;
        }
        try {
            market.approveAppointment("razShop2","idooo","raz");
            assert false;

        } catch (MarketException e) {
            assert true;
        }
    }
    @Test
    @DisplayName("Reject appointment - good test")
    public void rejectAppointment(){
        try {
            market.register("ido","password");
            market.appointShopOwner("raz","ayala","razShop");
            //automatically approved because raz is the only owner.
            market.appointShopOwner("raz","ido","razShop");
        } catch (MarketException e) {
            assert false;
        }
        try {

            Assertions.assertEquals(1,market.getMyPendingAppointmentsToApprove("razShop","ayala").size());
            market.rejectAppointment("razShop","ido","raz");
            Assertions.assertEquals(0,market.getMyPendingAppointmentsToApprove("razShop","ayala").size());
            Assertions.assertEquals(0,market.getMyPendingAppointmentsToApprove("razShop","raz").size());
        } catch (MarketException e) {
            assert false;
        }
    }
    @Test
    @DisplayName("Reject appointment - fail test - no such shop ")
    public void rejectAppointmentFailTestNoShop(){
        try {
            market.register("ido","password");
            market.appointShopOwner("raz","ayala","razShop");
            //automatically approved because raz is the only owner.
            market.appointShopOwner("raz","ido","razShop");
        } catch (MarketException e) {
            assert false;
        }
        try {
            Assertions.assertEquals(1,market.getMyPendingAppointmentsToApprove("razShop","ayala").size());
            market.rejectAppointment("razShop","ido","raz");
            Assertions.assertEquals(0,market.getMyPendingAppointmentsToApprove("razShop","ayala").size());
            Assertions.assertEquals(0,market.getMyPendingAppointmentsToApprove("razShop","raz").size());
        } catch (MarketException e) {
            assert false;
        }
    }
    @Test
    @DisplayName("Reject appointment - fail test - not an owner ")
    public void rejectAppointmentFailTestNotOnwer(){
        try {
            market.register("ido","password");
            market.appointShopOwner("raz","ayala","razShop");
            //automatically approved because raz is the only owner.
            market.appointShopOwner("raz","ido","razShop");
        } catch (MarketException e) {
            assert false;
        }
        try {
            Assertions.assertEquals(1,market.getMyPendingAppointmentsToApprove("razShop","ayala").size());
            market.rejectAppointment("razShop","ido","razraz");
            assert false;
        } catch (MarketException e) {
            assert true;
        }
    }
    @Test
    @DisplayName("Reject appointment - fail test - no such appointment ")
    public void rejectAppointmentFailTestNoSuchAppointment(){
        try {
            market.register("ido","password");
            market.appointShopOwner("raz","ayala","razShop");
            //automatically approved because raz is the only owner.
            market.appointShopOwner("raz","ido","razShop");
        } catch (MarketException e) {
            assert false;
        }
        try {
            Assertions.assertEquals(1,market.getMyPendingAppointmentsToApprove("razShop","ayala").size());
            market.rejectAppointment("razShop","idoo","raz");
            assert false;
        } catch (MarketException e) {
            assert true;
        }
    }
    @Test
    @DisplayName("Reject appointment - fail test - owner does not take part in agreement ")
    public void rejectAppointmentFailTestOwnerNotInAgreement(){
        try {
            market.register("ido","password");
            market.register("shaked","password");
            market.appointShopOwner("raz","ayala","razShop");
            //automatically approved because raz is the only owner.
            market.appointShopOwner("raz","ido","razShop");
            market.appointShopOwner("raz","shaked","razShop");
            market.approveAppointment("razShop","ido","ayala");
        } catch (MarketException e) {
            assert false;
        }
        try {
            market.rejectAppointment("razShop","shaked","ido");
            assert false;
        } catch (MarketException e) {
            assert true;
        }
    }

}

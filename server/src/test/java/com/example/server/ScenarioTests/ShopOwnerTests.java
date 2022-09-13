package com.example.server.ScenarioTests;

import com.example.server.businessLayer.Market.Appointment.Appointment;
import com.example.server.businessLayer.Payment.PaymentServiceProxy;
import com.example.server.businessLayer.Publisher.TextDispatcher;
import com.example.server.businessLayer.Supply.SupplyServiceProxy;
import com.example.server.businessLayer.Market.Item;
import com.example.server.businessLayer.Market.Market;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.businessLayer.Market.Users.UserController;
import com.example.server.businessLayer.Market.Users.Visitor;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ShopOwnerTests {



    Market market;
    String userName = "u1";
    String password = "password";
    String shopOwnerName = "bar";
    String shopOwnerPassword = "password";
    String memberName = "bar1";
    String memberPassword = "password";

    String loggedInmemberName = "bar2";

    String loggedInmemberPassword = "password";
    String shopName = "store";
    String ItemName= "item1";
    Item itemAdded;
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
            openShop();
            registerVisitor(memberName,memberPassword);
            registerVisitor(loggedInmemberName, loggedInmemberPassword);
            loginMember(loggedInmemberName, loggedInmemberPassword);
        } catch (Exception Ignored) {
        }
    }

    private void openShop() throws MarketException {
        loginMember(shopOwnerName,shopOwnerPassword);
        market.openNewShop(shopOwnerName, shopName);
        itemAdded = market.addItemToShopItem(shopOwnerName, ItemName, productPrice, Item.Category.electricity, "", new ArrayList<>(), productAmount, shopName);

    }

    @Test
    @DisplayName("shop owner add new item")
    public void addNewItem() {
        try {
            //login owner and add product
            loginMember(shopOwnerName, shopOwnerPassword);
            market.addItemToShop(shopOwnerName, "bikini", 150.99, Item.Category.general,"big size"
                    ,new ArrayList<>() , 3,shopName);
            Assertions.assertTrue(market.getItemByName(ItemName).size()>=1);
            try {
                logoutMember(shopOwnerName);
            } catch (MarketException ex) {
                System.out.println(ex.getMessage());
            }
        } catch (Exception e) {
            assert false;
            try {
                logoutMember(shopOwnerName);
            } catch (MarketException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    @Test
    @DisplayName("shop owner add new item bad case - not an owner")
    public void addNewItemFail() {
        try {
            //login owner and add product
            loginMember(shopOwnerName, shopOwnerPassword);
            market.addItemToShop("non existing owner", ItemName, productPrice, Item.Category.general,
                    "some info",new ArrayList<>() , productAmount,shopName);
             assert  false;
            try {
                logoutMember(shopOwnerName);
            } catch (MarketException ex) {
                System.out.println(ex.getMessage());
            }
        } catch (Exception e) {
            assert true;
            try {
                logoutMember(shopOwnerName);
            } catch (MarketException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    @Test
    @DisplayName("shop owner add new item bad case - not a real shop")
    public void addNewItemFail2() {
        try {
            //login owner and add product

            loginMember(shopOwnerName, shopOwnerPassword);
            market.addItemToShop(shopOwnerName, ItemName, productPrice, Item.Category.general,
                    "some info",new ArrayList<>() , productAmount,"non existing shop name");
            assert  false;
            try {
                logoutMember(shopOwnerName);
            } catch (MarketException ex) {
                System.out.println(ex.getMessage());
            }
        } catch (Exception e) {
            assert true;
            try {
                logoutMember(shopOwnerName);
            } catch (MarketException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    @Test
    @DisplayName("set item new amount")
    public void setItemAmount() {
        try {
            //login owner and add product
            loginMember(shopOwnerName,shopOwnerPassword);
            //get the item from the market for args.
            market.setItemCurrentAmount(shopOwnerName,itemAdded,newAmount,shopName);
            assert market.getItemCurrentAmount(itemAdded.getID()).equals(newAmount);
            try {
                logoutMember(shopOwnerName);
            } catch (MarketException ex) {
                System.out.println(ex.getMessage());
            }
        } catch (Exception e) {
            assert false;
            try {
                logoutMember(shopOwnerName);
            } catch (MarketException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    @Test
    @DisplayName("set item new amount bad case - not an existing item")
    public void setItemAmountFail() {
        try {
            //login owner and add product

            loginMember(shopOwnerName,shopOwnerPassword);
            //set amount for item that is not exists.
            market.setItemCurrentAmount(shopOwnerName,new Item(111,ItemName,10,"inf", Item.Category.fruit,new ArrayList<>()),newAmount,shopName);
            assert false;
            try {
                logoutMember(shopOwnerName);
            } catch (MarketException ex) {
                System.out.println(ex.getMessage());
            }
        } catch (Exception e) {
            assert true;
            try {
                logoutMember(shopOwnerName);
            } catch (MarketException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }


    @Test
    @DisplayName("set item new amount bad case - unauthorized member to update")
    public void setItemAmountFail2() {
        try {
            //login owner and add product
            loginMember(shopOwnerName,shopOwnerPassword);
            addItem(shopOwnerName,"newName",productPrice, Item.Category.general,"info", new ArrayList(),productAmount,shopName);
            //get the item from the market for args.
            Item it= market.getItemByName(ItemName).get(0);
            market.setItemCurrentAmount(loggedInmemberName,it,newAmount,shopName);
            assert false;
            try {
                logoutMember(shopOwnerName);
            } catch (MarketException ex) {
                System.out.println(ex.getMessage());
            }
        } catch (Exception e) {
            Assertions.assertEquals("member is not the shop owner and is not authorized to effect the inventory.",e.getMessage());
            try {
                logoutMember(shopOwnerName);
            } catch (MarketException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    @Test
    @DisplayName("edit item info")
    public void editItemInfo()   {
        try {
            //login owner and add product
            loginMember(shopOwnerName,shopOwnerPassword);
            //get the item from the market for args.
            market.changeShopItemInfo(shopOwnerName,"another info",itemAdded.getID(),shopName);
            assert market.getItemByID(itemAdded.getID()).getInfo().equals("another info");
            try {
                logoutMember(shopOwnerName);
            } catch (MarketException ex) {
                System.out.println(ex.getMessage());
            }
        } catch (Exception e) {
            assert false;
            try {
                logoutMember(shopOwnerName);
            } catch (MarketException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    @Test
    @DisplayName("edit item info bad case - item to update does not exists")
    public void editItemInfoFail()  {
        try {
            //login owner
            loginMember(shopOwnerName,shopOwnerPassword);
            market.changeShopItemInfo(shopOwnerName,"another info",33,shopName);
            assert false;
            try {
                logoutMember(shopOwnerName);
            } catch (MarketException ex) {
                System.out.println(ex.getMessage());
            }
        } catch (Exception e) {
            assert true;
            try {
                logoutMember(shopOwnerName);
            } catch (MarketException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    @Test
    @DisplayName("edit item info bad case - not a real shop name")
    public void editItemFail1()  {
        try {
            //login owner and add product
            loginMember(shopOwnerName,shopOwnerPassword);
            addItem(shopOwnerName,ItemName,productPrice, Item.Category.general,"info", new ArrayList(),productAmount,shopName);
            //get the item from the market for args.
            Item it= market.getItemByName(ItemName).get(0);
            market.changeShopItemInfo(shopOwnerName,"another info",it.getID(),"not real shop name");
            assert false;
            try {
                logoutMember(shopOwnerName);
            } catch (MarketException ex) {
                System.out.println(ex.getMessage());
            }
        } catch (Exception e) {
            assert true;
            try {
                logoutMember(shopOwnerName);
            } catch (MarketException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    @Test
    @DisplayName("appoint new shop owner")
    public void appointNewOwner() {
        try {
            loginMember(shopOwnerName,shopOwnerPassword);
            market.appointShopOwner(shopOwnerName,memberName,shopName);
            assert true;
            try {
                logoutMember(shopOwnerName);
            } catch (MarketException ex) {
                System.out.println(ex.getMessage());
            }
        } catch (Exception e) {
            assert false;
            try {
                logoutMember(shopOwnerName);
            } catch (MarketException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

    @Test
    @DisplayName("appoint new shop owner bad case - appoint for not a real shop")
    public void appointNewOwnerFail() {
        try {
            market.appointShopOwner(shopOwnerName,"not real member",shopName);
            assert false;
            try {
                logoutMember(shopOwnerName);
            } catch (MarketException ex) {
                System.out.println(ex.getMessage());
            }
        } catch (Exception e) {
            assert true;
            try {
                logoutMember(shopOwnerName);
            } catch (MarketException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
    @Test
    @DisplayName("appoint new shop owner bad case - appoint twice a member")
    public void appointNewOwnerFail2() {
        try {
            loginMember(shopOwnerName,shopOwnerPassword);
            market.appointShopOwner(shopOwnerName,memberName,shopName);
            try {
                market.appointShopOwner(shopOwnerName, memberName, shopName);
                assert false;
                market.removeShopOwnerAppointment(shopOwnerName,memberName,shopName);
            }
            catch(Exception e){
                market.removeShopOwnerAppointment(shopOwnerName,memberName,shopName);
                assert  true;
            }
        } catch (Exception e) {
            assert false;
        }
        try {
            logoutMember(shopOwnerName);
        } catch (MarketException ex) {
            System.out.println(ex.getMessage());
        }
    }



    @Test
    @DisplayName("appoint new shop manager")
    public void appointNewManager() {
        try {
            loginMember(shopOwnerName,shopOwnerPassword);
            market.appointShopManager(shopOwnerName,memberName,shopName);
            assert true;
        } catch (Exception e) {
            assert false;
        }
        try {
            logoutMember(shopOwnerName);
        } catch (MarketException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    @DisplayName("appoint new shop manager bad case - appoint not a real member")
    public void appointNewManagerFail() {
        try {

            loginMember(shopOwnerName,shopOwnerPassword);
            market.appointShopManager(shopOwnerName,"not real member",shopName);
            assert false;
        } catch (Exception e) {
            assert true;
        }
        try {
            logoutMember(shopOwnerName);
        } catch (MarketException ex) {
            System.out.println(ex.getMessage());
        }
    }
    @Test
    @DisplayName("appoint new shop manager bad case - appoint twice a member")
    public void appointNewManagerFail2()  {
        try {
            loginMember(shopOwnerName,shopOwnerPassword);
            market.appointShopManager(shopOwnerName,loggedInmemberName,shopName);
            try {
                market.appointShopManager(shopOwnerName, loggedInmemberName, shopName);
                assert false;
            }
            catch(Exception e){
                assert  true;
            }
        } catch (Exception e) {
            assert false;
        }
        try {
            logoutMember(shopOwnerName);
        } catch (MarketException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    @DisplayName("close shop")
    public void closeShop() {
        try {
            String sname="new shop name";
            loginMember(shopOwnerName,shopOwnerPassword);
            loginMember(shopOwnerName,shopOwnerPassword);
            market.openNewShop(shopOwnerName, sname);
            market.closeShop(shopOwnerName,sname);
            assert true;

        } catch (Exception e) {
            assert false;
        }
        try {
            logoutMember(shopOwnerName);
        } catch (MarketException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    @DisplayName("close shop bad case- owner not login")
    public void closeShopFail() {
        try {
            market.closeShop(shopOwnerName,shopName);
            assert false;
        } catch (Exception e) {
            assert true;
        }
    }

    @Test
    @DisplayName("close shop bad case- shop not exists")
    public void closeShopFail2() {
        try {
            loginMember(shopOwnerName,shopOwnerPassword);
            market.closeShop(shopOwnerName,"not a shop name");
            assert false;
        } catch (Exception e) {
            assert true;
        }
        try {
            logoutMember(shopOwnerName);
        } catch (MarketException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    @DisplayName("get employees info")
    public void empInfo() {
        try {
            loginMember(shopOwnerName,shopOwnerPassword);
            Map<String , Appointment> emps=  market.getShopEmployeesInfo(shopOwnerName,shopName);
            Assertions.assertNotNull(emps);
            assert true;
        } catch (Exception e) {
            assert false;
        }
        try {
            logoutMember(shopOwnerName);
        } catch (MarketException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    @DisplayName("get employees info bad case - not authorized member")
    public void empInfoFail() {
        try {
            loginMember(shopOwnerName,shopOwnerPassword);
            Map<String , Appointment> emps=  market.getShopEmployeesInfo(memberName,shopName);
            Assertions.assertNotNull(emps);
            assert false;
        } catch (Exception e) {
            assert true;
        }
        try {
            logoutMember(shopOwnerName);
        } catch (MarketException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    @DisplayName("get employees info bad case - member not logged in")
    public void empInfoFail1() {
        try {
            market.getShopEmployeesInfo(memberName,shopName);
            assert false;
        } catch (Exception e) {
            assert true;
        }
    }
    @Test
    @DisplayName("get shop purchase history")
    public void purchaseHistory() {
        try {
            loginMember(shopOwnerName,shopOwnerPassword);
            String  str = new String( market.getShopPurchaseHistory(shopOwnerName,shopName));
            Assertions.assertNotNull(str);
            assert true;
        } catch (Exception e) {
            assert false;
        }
        try {
            logoutMember(shopOwnerName);
        } catch (MarketException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    @DisplayName("get shop purchase history bad case - not authorized member")
    public void purchaseHistoryFail() {
        try {
            loginMember(shopOwnerName,shopOwnerPassword);
            market.getShopPurchaseHistory(memberName,shopName);
            assert false;
        } catch (Exception e) {
            assert true;
        }
        try {
            logoutMember(shopOwnerName);
        } catch (MarketException ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Test
    @DisplayName("get shop purchase history bad case -not real shop")
    public void purchaseHistoryFail1() {
        try {
            loginMember(shopOwnerName,shopOwnerPassword);
            market.getShopPurchaseHistory(memberName,"not a real shop name");
            assert false;
        } catch (Exception e) {
            assert true;
        }
        try {
            logoutMember(shopOwnerName);
        } catch (MarketException ex) {
            System.out.println(ex.getMessage());
        }
    }
/*
    @Test
    @DisplayName("add new simple discount to shop")
    public void addNewSimpleDiscountToShopSuccess() {
        throw new UnsupportedOperationException (  );
    }

    @Test
    @DisplayName("add new complex discount to shop")
    public void addNewComplexDiscountToShopSuccess() {
        throw new UnsupportedOperationException (  );
    }

    @Test
    @DisplayName("add new complex discount to shop - existing simple discount")
    public void addNewComplexDiscountToShopExistingSimpleDiscount() {
        throw new UnsupportedOperationException (  );
    }
*/
    public void loginMember(String name, String password) throws MarketException {
        if(UserController.getInstance().isLoggedIn(name))
            return;
        Visitor visitor = market.guestLogin();
        market.memberLogin(name, password);
        market.validateSecurityQuestions(name, new ArrayList<>(), visitor.getName());
        market.memberLogin(name,password);
    }
    public void logoutMember(String name) throws MarketException {
        market.memberLogout(name);
    }
    public void registerVisitor(String name, String pass) throws MarketException {
        // shop manager register
        Visitor visitor = market.guestLogin();
        market.register(name, pass);
    }
    public Item addItem(String son, String in, double pp , Item.Category cat, String inf, List<String> lis, int am, String sn) throws MarketException {
        return market.addItemToShopItem(son, in, pp, cat,
                inf, lis , am,sn);
    }
}

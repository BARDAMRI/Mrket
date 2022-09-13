package com.example.server.ScenarioTests;

import com.example.server.businessLayer.Market.Item;
import com.example.server.businessLayer.Market.Market;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.businessLayer.Market.Shop;
import com.example.server.businessLayer.Market.ShoppingCart;
import com.example.server.businessLayer.Market.Users.Visitor;
import com.example.server.businessLayer.Payment.CreditCard;
import com.example.server.businessLayer.Payment.PaymentServiceProxy;
import com.example.server.businessLayer.Payment.WSEPPaymentServiceAdapter;
import com.example.server.businessLayer.Publisher.TextDispatcher;
import com.example.server.businessLayer.Supply.Address;
import com.example.server.businessLayer.Supply.WSEPSupplyServiceAdapter;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AcquisitionTests {


    static Market market;
    static String userName = "userTest";
    static String password = "password";
    static String shopManagerName = "shakedAcquisitionTests";
    static String shopManagerPassword = "password";
    static String shopName = "kolboAcquisitionTests";
    static Double productAmount;
    static Double productPrice;
    static CreditCard creditCard;
    static Address address;


    @BeforeAll
    public static void setUp() {
        try {
            market = Market.getInstance();
            if (market.getPaymentService() == null) {
                market.firstInitMarket(userName, password);
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
            market.addItemToShop(shopManagerName, "milk", productPrice, Item.Category.general,
                    "soy",keywords , productAmount,shopName);
            market.addItemToShop(shopManagerName, "milk chocolate", productPrice, Item.Category.general,
                    "soy",keywords , productAmount,shopName);
            creditCard = new CreditCard("1234567890", "5","2024", "555","Ido livne","204534839");            address = new Address("Bar Damri","Ben Gurion 3","Tel Aviv", "Israel", "1234");

        } catch (Exception e) {
            System.out.println (e.getMessage () );
        }
    }

    //good purchase.
    @Test
    @DisplayName("buy item, valid amount")
    public void buyItemValid() {
        try {
            Visitor visitor = market.guestLogin();
            Shop shop = market.getShopInfo(shopManagerName, shopName);
            List<Item> res = market.getItemByName("milk chocolate");
            Item chocolate = res.get(0);
            Double itemAmount = shop.getItemCurrentAmount(chocolate);
            double buyingAmount = itemAmount;
            market.addItemToShoppingCart(chocolate, buyingAmount, visitor.getName());
            market.buyShoppingCart(visitor.getName(), productPrice * buyingAmount, creditCard, address);
            shop = market.getShopInfo(shopManagerName, shopName);
            Double newAMount = shop.getItemCurrentAmount(chocolate);
            assert newAMount == 0;
        } catch (Exception e) {
            assert false;
        }
    }
    //price not correct
    @Test
    @DisplayName("buy cart with unexpected price")
    public void buyWithUnexpectedPrice() {
        try {
            Visitor visitor = market.guestLogin();
            Shop shop = market.getShopInfo(shopManagerName, shopName);
            List<Item> res = market.getItemByName("milk");
            Item milk = res.get(0);
            Double itemAmount = shop.getItemCurrentAmount(milk);
            double buyingAmount = itemAmount + 1;
            market.addItemToShoppingCart(milk, buyingAmount, visitor.getName());
            market.buyShoppingCart(visitor.getName(), productPrice * buyingAmount, creditCard, address);

        } catch (Exception e) {
            assert true;
        }
    }

    //item not exists.
    @Test
    @DisplayName("buy not existing item")
    public void buyNotExistingItem() {
        //TODO
        try {
            Visitor visitor = market.guestLogin();
            Visitor visitor2 = market.guestLogin ();
            Shop shop = market.getShopInfo(shopManagerName, shopName);
            List<Item> res = market.getItemByName("milk chocolate");
            Item chocolate = res.get(0);
            market.setItemCurrentAmount(shopManagerName,chocolate,10,shopName);
            Item toiletPaper = new Item(12345,"toilet paper",10,"some info", Item.Category.general,new ArrayList<>());
            Double itemAmount = shop.getItemCurrentAmount(chocolate);
            double buyingAmount = itemAmount;
            market.addItemToShoppingCart(chocolate, buyingAmount, visitor.getName());
            market.addItemToShoppingCart(chocolate, 1, visitor2.getName());
            market.addItemToShoppingCart(toiletPaper, 1, visitor2.getName());
             market.buyShoppingCart(visitor.getName(), productPrice * buyingAmount, creditCard, address);
            //Assertions.assertNull ( shoppingCart );
             market.buyShoppingCart(visitor2.getName(), productPrice + productPrice , creditCard, address);
            //assert !shoppingCart2.getCart ().isEmpty ();
        }
        catch (MarketException m){
            Assertions.assertEquals("Cannot add item that does not exists in the shop.",m.getMessage());
        }
        catch (Exception e) {
            assert false;
        }
    }

    //the payment method isn't right'
    @Test
    @DisplayName("buy with no payment method")
    public void buyWithIllegalPaymentMethodA() {
        try {
            Visitor visitor = market.guestLogin();
            Shop shop = market.getShopInfo(shopManagerName, shopName);
            List<Item> res = market.getItemByName("milk chocolate");
            Item chocolate = res.get(0);
            market.setItemCurrentAmount(shopManagerName,chocolate,10,shopName);
            Double itemAmount = shop.getItemCurrentAmount(chocolate);
            market.addItemToShoppingCart(chocolate, itemAmount, visitor.getName());
            try {
                market.buyShoppingCart(visitor.getName(), productPrice * itemAmount, null, address);
                assert false;
            }catch (MarketException e){
               Assertions.assertEquals("Payment method not supplied.",e.getMessage());
            }

        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("buy with illegal payment method")
    public void buyWithIllegalPaymentMethodB() {
        try {
            Visitor visitor = market.guestLogin();
            Shop shop = market.getShopInfo(shopManagerName, shopName);
            List<Item> res = market.getItemByName("milk chocolate");
            Item chocolate = res.get(0);
            market.setItemCurrentAmount(shopManagerName,chocolate,10,shopName);
            Double itemAmount = shop.getItemCurrentAmount(chocolate);
            market.addItemToShoppingCart(chocolate, itemAmount, visitor.getName());
            try {
                market.buyShoppingCart(visitor.getName(), productPrice * itemAmount, new CreditCard("","-1","1000","123","no holder","1234567"), address);
                assert false;
            }catch (MarketException e){
                Assertions.assertEquals("Payment method details are illegal.",e.getMessage());
            }
        } catch (Exception e) {
            assert false;
        }
    }

    // address isn't good.
    @Test
    @DisplayName("buy with no address")
    public void buyWithoutAddress() {
        Visitor visitor;
        try {
             visitor = market.guestLogin();
            Shop shop = market.getShopInfo(shopManagerName, shopName);
            List<Item> res = market.getItemByName("milk");
            Item milk = res.get(0);
            Double itemAmount = shop.getItemCurrentAmount(milk);
            market.addItemToShoppingCart(milk, itemAmount, visitor.getName());
            try {
                market.buyShoppingCart(visitor.getName(), productPrice * itemAmount, new CreditCard("1234567890","2","2024","123","no holder","1234567"), null);
                assert false;
            }catch (MarketException e){
                if(visitor !=null) {
                    Assertions.assertEquals("Address not supplied.", e.getMessage());
                }
                else {
                    assert false;
                }
            }
        } catch (Exception e) {
            assert true;
        }
    }

    @Test
    @DisplayName("buy with illegal address")
    public void buyWithIllegalAddress() {
        try {
            Visitor visitor = market.guestLogin();
            Shop shop = market.getShopInfo(shopManagerName, shopName);
            List<Item> res = market.getItemByName("milk");
            Item milk = res.get(0);
            Double itemAmount = shop.getItemCurrentAmount(milk);
            market.addItemToShoppingCart(milk, itemAmount, visitor.getName());
            try {
                market.buyShoppingCart(visitor.getName(), productPrice * itemAmount, new CreditCard("1234567890","2","2024","123","no holder","1234567"), new Address("","Atad 30", "Beer sheva","israel","8484400"));
                assert false;
            }catch (MarketException e){
                Assertions.assertEquals("Address details are illegal.",e.getMessage());
            }
        } catch (Exception e) {
            assert true;
        }
    }

    //payment services not exists.
    @Test
    @DisplayName("buy when payment service is not connected")
    public void buyWhenPaymentServiceIsNotConnected() throws MarketException {
        try {
            Visitor visitor = market.guestLogin();
            Mockito.when ( market.getPaymentService() ).then ( null );
            Shop shop = market.getShopInfo(shopManagerName, shopName);
            List<Item> res = market.getItemByName("milk");
            Item milk = res.get(0);
            Double itemAmount = shop.getItemCurrentAmount(milk);
            double buyingAmount = itemAmount + 1;
            market.addItemToShoppingCart(milk, buyingAmount, visitor.getName());
            market.setPaymentServiceProxy(null,userName,true);
            try {
                market.buyShoppingCart(visitor.getName(), productPrice * buyingAmount, creditCard, address);
                market.setPaymentServiceProxy(null,userName,true);
                assert false;
                market.setPaymentServiceProxy(new PaymentServiceProxy(WSEPPaymentServiceAdapter.getinstance(),true),userName,true);

            }catch (MarketException e){
                Assertions.assertEquals("The payment service is not available right now.",e.getMessage());
                market.setPaymentServiceProxy(new PaymentServiceProxy(WSEPPaymentServiceAdapter.getinstance(),true),userName,true);
            }
        } catch (Exception e) {
            assert true;
            market.setPaymentServiceProxy(new PaymentServiceProxy(WSEPPaymentServiceAdapter.getinstance(),true),userName,true);
        }
    }

    //supply services not exists.
    @Test
    @DisplayName("buy when supply service is not connected")
    public void buyWhenSupplyServiceIsNotConnected() {
        try {
            Visitor visitor = market.guestLogin();
            Mockito.when ( market.getPaymentService() ).then ( null );
            Shop shop = market.getShopInfo(shopManagerName, shopName);
            List<Item> res = market.getItemByName("milk");
            Item milk = res.get(0);
            Double itemAmount = shop.getItemCurrentAmount(milk);
            double buyingAmount = itemAmount + 1;
            market.addItemToShoppingCart(milk, buyingAmount, visitor.getName());
            market.setSupplyService(null,userName);
            try {
                market.buyShoppingCart(visitor.getName(), productPrice * buyingAmount, creditCard, address);
                market.setSupplyService( WSEPSupplyServiceAdapter.getInstance(),userName);
                assert false;
            }catch (MarketException e){
                Assertions.assertEquals("The supply service is not available right now.",e.getMessage());
                market.setSupplyService( WSEPSupplyServiceAdapter.getInstance(),userName);
            }
        } catch (Exception e) {
            assert true;
        }
    }
    //basket empty.
    @Test
    @DisplayName("buy item, un valid amount")
    public void buyEmptyCart() {
        Visitor visitor = null;
        try {
             visitor = market.guestLogin();
            market.buyShoppingCart(visitor.getName(), 0, creditCard, address);
            assert false;
        } catch (Exception e) {
            if(visitor!=null) {
                Assertions.assertEquals("Shopping cart is not exists for the user." , e.getMessage());
            }
            else{
                assert false;
            }
        }
    }
}

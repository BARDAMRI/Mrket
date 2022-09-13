package com.example.server.ScenarioTests;

import com.example.server.businessLayer.Payment.CreditCard;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.businessLayer.Payment.PaymentServiceProxy;
import com.example.server.businessLayer.Publisher.TextDispatcher;
import com.example.server.businessLayer.Supply.Address;
import com.example.server.businessLayer.Supply.SupplyServiceProxy;
import com.example.server.businessLayer.Market.Item;
import com.example.server.businessLayer.Market.Market;
import com.example.server.businessLayer.Market.Shop;
import com.example.server.businessLayer.Market.ShoppingCart;
import com.example.server.businessLayer.Market.Users.Visitor;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class VisitorTests {
    Market market;
    String userName = "u1";
    String password = "password";
    String shopManagerName = "shaked";
    String shopManagerPassword = "password";
    String shopName = "kolbo";
    Double productAmount;
    Double productPrice;
    CreditCard creditCard;
    Address address;


    @BeforeAll
    public void setUp() {
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
            market.addItemToShop(shopManagerName, "chocolate", productPrice, Item.Category.general,
                    "soy",keywords , productAmount,shopName);
            creditCard = new CreditCard("1234567890", "5","24", "555","Ido livne","204534839");            address = new Address("Bar Damri","Ben Gurion 3","Tel Aviv", "Israel", "1234");

        } catch (Exception Ignored) {
        }
    }

    @Test
    @DisplayName("valid guest login")
    public void guestLoginValid() throws Exception {
//        initMarket();
        try {
            Visitor visitor = market.guestLogin();
            assert !(visitor == null);
            Assertions.assertNotNull(visitor.getCart());
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("guest leaves the market")
    public void guestExitMarket() throws Exception {
        Visitor visitor = market.guestLogin();
        try {
            market.visitorExitSystem(visitor.getName());
            Visitor visitor2 = market.guestLogin();
            assert !(visitor2.getName().equals(visitor.getName()));

        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("register taken name")
    public void registerTest() {
        try {
            Visitor visitor = market.guestLogin();
            market.register("registerTest", "1234R");
        } catch (Exception e) {
            assert false;
            try {
                market.register("registerTest", "1234R");
            } catch (Exception e2) {
                assert true;
            }
        }
    }

    @Test
    @DisplayName("get shop info")
    public void shopInfoTest() {
//        String shopName = "shopTest";
        try {
            Shop res = market.getShopInfo(shopManagerName, shopName);
            assert res.getShopName().equals(shopName);
            assert true;
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("search item by name")
    public void searchItemName() {
        try {
            List<Item> res = market.getItemByName("milk");
            assert res.size() > 0;
            assert res.get(0).getName().equals("milk");
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("search item by keyWord")
    public void searchItemByKeyword(){
        try {
            List<Item> res = market.getItemsByKeyword("in sale");
            assert res.size() > 0;
            boolean milkFound = false;
            for (Item item: res){
                assert item.getKeywords().contains("in sale");
                if (item.getName().equals("milk")) {
                    milkFound = true;
                    break;
                }
            }
            assert milkFound;
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("search item by Category")
    public void searchItemByCategory(){
        try {
            List<Item> res = market.getItemByCategory(Item.Category.general);
            assert res.size() > 0;
            for (Item item: res){
                assert item.getCategory() == Item.Category.general;
            }
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("add item to cart")
    public void addItemCart() {
        try {
            Visitor visitor = market.guestLogin();
            List<Item> res = market.getItemByName("milk");
            Item milk = res.get(0);
            market.addItemToShoppingCart(milk, 3, visitor.getName());
            assert true;
            //check shopping basket includes only the milk
            assert visitor.getCart().getCart().size() == 1;
            visitor.getCart().getCart().forEach((shop, basket) -> {
                assert basket.getItems().size() == 1;
                assert shop.getShopName().equals(shopName);
                // check right amount added
                assert basket.getItems().get(milk.getID()).equals(3.0);
            });

        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("add item to cart, exit - empty cart")
    public void emptyCartWhenExit() {
        try {
            Visitor visitor = market.guestLogin();
            List<Item> res = market.getItemByName("milk");
            Item milk = res.get(0);
            market.addItemToShoppingCart(milk, 3, visitor.getName());
            assert !visitor.getCart().getCart().isEmpty();
            market.visitorExitSystem(visitor.getName());
            visitor = market.guestLogin();
            assert visitor.getCart().getCart().isEmpty();
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("add item to cart, 0 amount")
    public void addZeroAmount() {
        try {
            Visitor visitor = market.guestLogin();
            List<Item> res = market.getItemByName("milk");
            Item milk = res.get(0);
            market.addItemToShoppingCart(milk, 0, visitor.getName());
            assert false;
        } catch (MarketException e) {
            assert true;
        } catch (Exception e){
            assert false;
        }
    }

}

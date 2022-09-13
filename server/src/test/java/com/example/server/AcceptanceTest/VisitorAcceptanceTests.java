package com.example.server.AcceptanceTest;

import com.example.server.businessLayer.Market.Market;
import com.example.server.businessLayer.Payment.CreditCard;
import com.example.server.businessLayer.Payment.PaymentServiceProxy;
import com.example.server.businessLayer.Payment.WSEPPaymentServiceAdapter;
import com.example.server.businessLayer.Publisher.TextDispatcher;
import com.example.server.businessLayer.Supply.Address;
import com.example.server.businessLayer.Market.Item;
import com.example.server.serviceLayer.FacadeObjects.*;
import com.example.server.serviceLayer.Response;
import com.example.server.serviceLayer.ResponseT;
import com.example.server.serviceLayer.Service;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VisitorAcceptanceTests extends AcceptanceTests {

//    VisitorFacade curVisitor;
    static String shopOwnerName = "shaked";
    static String shopOwnerPassword = "password";
    static String shopName = "kolbo";
    static Double productAmount;
    static Double productPrice;
    static CreditCard creditCard;
    static Address address;
    static ItemFacade oil;

    static ItemFacade bamba;

    static double melonAmount;
    static String melonName;
    static Item.Category melonCategory;
    static double melonPrice;
    static ArrayList<String> melonKeywords;
    static String melonInfo;
    static ItemFacade melon;

    static double onePlusAmount;
    static String onePlusName;
    static Item.Category onePlusCategory;
    static double onePlusPrice;
    static List<String> onePlusKeywords;
    static String onePlusInfo;
    static ItemFacade onePlus;

    @BeforeAll
    public static void setup() {
        try {
            try {
                initMarket();
                Market market = Market.getInstance();
                market.isInit();
            }catch (Exception e){}
            // shop manager register
            VisitorFacade visitor = guestLogin();
            register(shopOwnerName, shopOwnerPassword);
            List<String> questions = memberLogin(shopOwnerName, shopOwnerPassword).getValue();
            validateSecurityQuestions(shopOwnerName, new ArrayList<>(), visitor.getName());
            // open shop
            openShop(shopOwnerName, shopName);
            productAmount = 3.0;
            productPrice = 1.2;
            addItemToShop(shopOwnerName, "oil", productPrice, Item.Category.general,
                    "soy", new ArrayList<>(), productAmount, shopName).getValue ();
            addItemToShop(shopOwnerName, "bamba", productPrice, Item.Category.general,
                    "nugat", new ArrayList<>(), productAmount, shopName);
            List<ItemFacade> res = searchProductByName("bamba");
            bamba = res.get(0);

            melonAmount = 4.0;
            melonName = "melon";
            melonCategory = Item.Category.fruit;
            melonPrice = 10.0;
            melonKeywords = new ArrayList<>();
            melonKeywords.add("tasty");
            melonKeywords.add("in sale");
            melonInfo = "big";
            addItemToShop(shopOwnerName, melonName, melonPrice, melonCategory, melonInfo, melonKeywords, melonAmount, shopName);
            melon = searchProductByName("melon").get(0);

            onePlusAmount = 2.0;
            onePlusName = "onePlus";
            onePlusCategory = Item.Category.cellular;
            onePlusPrice = 1000;
            onePlusKeywords = new ArrayList<>();
            onePlusKeywords.add("best seller");
            onePlusKeywords.add("in sale");
            onePlusInfo = "9-5g";
            addItemToShop(shopOwnerName, onePlusName, onePlusPrice, onePlusCategory,
                    onePlusInfo, onePlusKeywords, onePlusAmount, shopName);
            onePlus = searchProductByName(onePlusName).get(0);
            creditCard = new CreditCard("1234567890111111", "5","2024", "555","Ido livne","204534839");            address = new Address("Bar Damri","Ben Gurion 3","Tel Aviv", "Israel", "1234");
        } catch (Exception e) {
            String msg = e.getMessage();
        }
    }



    @BeforeEach
    public void reset() {
        try {
            setItemCurrentAmount(shopOwnerName, oil, productAmount, shopName);
        } catch (Exception e) {
            String msg = e.getMessage();
        }
    }

    /**
     * 2.1.1 use case
     * @throws Exception
     */
    @Test
    @DisplayName("valid guest login")
    public void guestLoginValid() throws Exception {
        try {
            ResponseT<VisitorFacade> result = Service.getInstance().guestLogin();
            assert !result.isErrorOccurred();
            VisitorFacade visitor = result.getValue();
            Assertions.assertNotNull(visitor.getCart());
        } catch (Exception e) {
            assert false;
        }

    }


    /**
     * 2.1.2 use case
     * @throws Exception
     */
    @Test
    @DisplayName("guest leaves the market")
    public void guestExitMarket() throws Exception {

        VisitorFacade visitor = guestLogin();
        try {
            Response result = exitMarket(visitor.getName());
            assert !result.isErrorOccurred();
            VisitorFacade visitor2 = guestLogin();
            assert !(visitor2.getName().equals(visitor.getName()));

        } catch (Exception e) {
            assert false;
        }
    }




    @Test
    @DisplayName("register taken name")
    public void registerTest() {
        try {
            VisitorFacade visitor = guestLogin();
            ResponseT<Boolean> res = register("registerTest", "1234R");
            assert !res.isErrorOccurred();
            ResponseT<Boolean> res2 = register("registerTest", "1234R");
            assert res2.isErrorOccurred();

        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("get shop info")
    public void shopInfoTest() {
        try {
            VisitorFacade visitor = guestLogin();
            ShopFacade res = getShopInfo(visitor.getName(), shopName).getValue();
            assert res.getShopName().equals(shopName);
            exitMarket(visitor.getName());
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("get item info")
    public void itemInfoTest() {
        try {
            VisitorFacade visitor = guestLogin();
            ItemFacade res = getItemInfo(visitor.getName(), bamba.getId());
            assert res.getName().equals(bamba.getName());
            exitMarket(visitor.getName());
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("search item by name")
    public void searchItemName() {
        try {
            List<ItemFacade> res = searchProductByName("oil");
            assert res.size() > 0;
            assert res.get(0).getName().equals("oil");
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("search item by keyWord")
    public void searchItemByKeyword() {
        try {
            List<ItemFacade> res = searchProductByKeyword("in sale");
            assert res.size() > 1;
            boolean onePLusFound = false;
            boolean melonFound = false;
            for (ItemFacade item : res) {
                assert item.getKeywords().contains("in sale");
                melonFound = item.getName().equals(melonName) || melonFound;
                onePLusFound = item.getName().equals(onePlusName) || onePLusFound;
            }
            assert melonFound && onePLusFound;
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("search item by Category")
    public void searchItemByCategory() {
        try {
            List<ItemFacade> res = searchProductByCategory(Item.Category.fruit);
            assert res.size() >= 1;
            boolean melonFound = false;
            for (ItemFacade item : res) {
                assert item.getCategory() == Item.Category.fruit;
                melonFound = melonFound || item.getName().equals(melonName);
            }
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("add item to cart")
    public void addItemCart() {
        try {
            VisitorFacade visitor = guestLogin();
            List<ItemFacade> res = searchProductByName("oil");
            ItemFacade oil = res.get(0);
            Response response = addItemToCart(oil, 3,  visitor.getName());
            assert !response.isErrorOccurred();
            //check shopping basket includes only the oil
            visitor = getVisitor(visitor.getName());
            assert visitor.getCart().getCart().size() == 1;
            for (Map.Entry<String, ShoppingBasketFacade> entry : visitor.getCart().getCart().entrySet()){
                assert entry.getValue().getItems().size() == 1;
                assert entry.getKey().equals(shopName);
                // check shop amount didn't change
                ShopFacade shopFacade = getShopInfo(visitor.getName(), shopName).getValue();
                assert shopFacade.getItemsCurrentAmount().get(oil.getId()).equals(productAmount);
                // check right amount added
                assert entry.getValue().getItems().get(oil.getId()).equals(3.0);
            }
            // checks adding item
            response = addItemToCart(oil, 2,  visitor.getName());
            visitor = getVisitor(visitor.getName());
            assert !response.isErrorOccurred();
            for(Map.Entry<String, ShoppingBasketFacade> entry:visitor.getCart().getCart().entrySet()){
                assert entry.getValue().getItems().get(oil.getId()).equals(5.0);
            }
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("add item to cart, exit - empty cart")
    public void emptyCartWhenExit() {
        try {
            VisitorFacade visitor = guestLogin();
            List<ItemFacade> res = searchProductByName("oil");
            ItemFacade oil = res.get(0);
            Response response = addItemToCart(oil, 3,  visitor.getName());
            assert !response.isErrorOccurred();
            visitor = getVisitor(visitor.getName());
            assert !visitor.getCart().getCart().isEmpty();
            exitMarket(visitor.getName());
            visitor = guestLogin();
            assert visitor.getCart().getCart().isEmpty();
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("add item to cart, 0 amount")
    public void addZeroAmount() {
        try {
            VisitorFacade visitor = guestLogin();
            List<ItemFacade> res = searchProductByName("oil");
            ItemFacade oil = res.get(0);
            Response response = addItemToCart(oil, 0,  visitor.getName());
            assert response.isErrorOccurred();
            visitor = getVisitor(visitor.getName());
            assert visitor.getCart().getCart().isEmpty();
        } catch (Exception e) {
            assert false;
        }
    }


    @Test
    @DisplayName("buy item, valid amount")
    public void buyItemValid() {
        try {
            Market market= Market.getInstance();
            market.setPaymentServiceProxy(new PaymentServiceProxy(WSEPPaymentServiceAdapter.getinstance(),true),market.getSystemManagerName(),true);
            VisitorFacade visitor = guestLogin();
            ShopFacade shop = getShopInfo(shopOwnerName, shopName).getValue();
            List<ItemFacade> res = searchProductByName("bamba");
            ItemFacade bamba = res.get(0);
            Double itemAmount = shop.getItemsCurrentAmount().get(bamba.getId());
            double buyingAmount = itemAmount - 1;
            Response response = addItemToCart(bamba, buyingAmount,  visitor.getName());
            Response result = buyShoppingCart(visitor.getName(), productPrice * buyingAmount, creditCard, address);
            assert !result.isErrorOccurred();
            shop = getShopInfo(shopOwnerName, shopName).getValue();
            Double newAMount = shop.getItemsCurrentAmount().get(bamba.getId());
            assert newAMount == 1;
            itemAmount = newAMount;

        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("buy not existing item")
    public void buyWithUnexpectedPrice() {
        try {
            VisitorFacade visitor = guestLogin();
            ShopFacade shop = getShopInfo(shopOwnerName, shopName).getValue();
            List<ItemFacade> res = searchProductByName("oil");
            ItemFacade oil = res.get(0);
            Double itemAmount = shop.getItemsCurrentAmount().get(oil.getId());
            double buyingAmount = itemAmount + 1;
            Response response = addItemToCart(oil, buyingAmount,  visitor.getName());
            // add not existing item shouldn't fail
            assert !response.isErrorOccurred();
            Response result = buyShoppingCart(visitor.getName(), productPrice * buyingAmount, creditCard, address);
            assert result.isErrorOccurred();
            shop = getShopInfo(shopOwnerName, shopName).getValue();
            Double newAMount = shop.getItemsCurrentAmount().get(oil.getId());
            Assertions.assertEquals(newAMount, itemAmount);

        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("buy cart with unexpected price")
    public void buyNotExistingItem() {
        try {
            VisitorFacade visitor = guestLogin();
            ShopFacade shop = getShopInfo(shopOwnerName, shopName).getValue();
            List<ItemFacade> res = searchProductByName("bamba");
            ItemFacade bamba = res.get(0);
            Double itemAmount = shop.getItemsCurrentAmount().get(bamba.getId());
            double buyingAmount = itemAmount;
            Response response = addItemToCart(bamba, buyingAmount,  visitor.getName());
            // add not existing item shouldn't fail
            assert !response.isErrorOccurred();
            Double amount = shop.getItemsCurrentAmount().get(bamba.getId());
            Response result = buyShoppingCart(visitor.getName(), productPrice * buyingAmount + 1, creditCard, address);
            assert result.isErrorOccurred();
            shop = getShopInfo(shopOwnerName, shopName).getValue();
            Double newAMount = shop.getItemsCurrentAmount().get(bamba.getId());
            Assertions.assertEquals(newAMount, amount);

        } catch (Exception e) {
            assert false;
        }
    }
}

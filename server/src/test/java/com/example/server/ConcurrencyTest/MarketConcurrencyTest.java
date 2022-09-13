package com.example.server.ConcurrencyTest;

import com.example.server.businessLayer.Payment.CreditCard;
import com.example.server.businessLayer.Supply.Address;
import com.example.server.businessLayer.Market.Item;
import com.example.server.businessLayer.Market.Market;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.businessLayer.Security.Security;
import com.example.server.businessLayer.Market.Users.Member;
import com.example.server.businessLayer.Market.Users.UserController;
import com.example.server.businessLayer.Market.Users.Visitor;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;


public class MarketConcurrencyTest {

    //TODO - special exceptions
    static Market market;
    static UserController userController;
    static Security security;
    MyThread[] threads;
    static String password = "password";
    static String initialLoggedIn = "Mira";
    static String[] namesToRegister = {"Raz", "Raz", "Raz", "Raz", "Raz", "Raz",
            "Ido", "Ido", "Ido", "Ido", "Ido", "Ido",
            "Shaked", "Shaked", "Shaked", "Shaked", "Shaked", "Shaked",
            "Bar", "Bar", "Bar", "Bar", "Bar", "Bar",
            "Ayala", "Ayala", "Ayala", "Ayala", "Ayala", "Ayala"};
    static String[] namesToLogin = {"name1", "name1", "name1", "name1", "name1", "name1",
            "name2", "name2", "name2", "name2", "name2", "name2",
            "name3", "name3", "name3", "name3", "name3", "name3",
            "name4", "name4", "name4", "name4", "name4", "name4",
            "name5", "name5", "name5", "name5", "name5", "name5"};
    static String[] names = {"abc", "def", "ghi", "jkl", "mno"};
    static Visitor[] visitors = new Visitor[5];
    static Member[] members = new Member[5];
    static SynchronizedCounter i = new SynchronizedCounter ();
    SynchronizedBoolean first = new SynchronizedBoolean ();
    int test;

    @BeforeAll
    public static void setUp() {
        market = Market.getInstance();
        security = Security.getInstance();
        userController = UserController.getInstance();
        createShop("shopName");
    }
    @BeforeEach
    public void initMemberTest() throws MarketException {
        threads = new MyThread[30];
    }

    @Test
    @DisplayName("Register concurrency test")
    public void registerConcurrencyTest(){
        int numOfExceptions = 0;
        for(i.reset (); i.value () < 30; i.increment ()){
            threads[i.value ()] = new MyThread ();
            threads[i.value ()].setRunnable ( new MyRunnable ( i.value ()) {
                @Override
                public void run() throws MarketException {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {}
                    market.register ( namesToRegister[index], password );
                }
            } ) ;
        }
        for(i.reset (); i.value () < 30; i.increment ()) {
            threads[i.value ()].run ( );
        }
        for(i.reset (); i.value () < 30; i.increment ()) {
            try {
                threads[i.value ()].join ( );
            } catch (InterruptedException e) {}
        }
        for(i.reset (); i.value () < 30; i.increment ()){
            if(threads[i.value ()].getEx () != null)
                numOfExceptions ++;
        }
        Assertions.assertTrue(numOfExceptions >= 25);
    }

    @Test
    @DisplayName("Member login concurrency test")
    public void loginConcurrencyTest() {
        int numOfExceptions = 0;
        for (i.reset(); i.value() < 5; i.increment()){
            try {
                market.register(namesToLogin[i.value() * 6], password);
            } catch (MarketException e) {}
        }
        for ( i.reset (); i.value () < 30 ; i.increment () ) {
            threads[i.value ()] = new MyThread ( );
            threads[i.value ()].setRunnable ( new MyRunnable ( i.value ()) {
                @Override
                public void run() throws MarketException{
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {}
                    Visitor visitor = market.guestLogin ();
                    market.memberLogin ( namesToLogin[index], password );
                    market.validateSecurityQuestions ( namesToLogin[index] , new ArrayList<> (  ), visitor.getName ());
                }
            } );
        }
        for ( i.reset (); i.value () < 30 ; i.increment () ) {
            threads[i.value ()].start ( );
        }
        for(i.reset (); i.value () < 30; i.increment ()) {
            try {
                threads[i.value ()].join ( );
            } catch (InterruptedException e) {}
        }
        for ( i.reset () ; i.value () < 30 ; i.increment () ) {
            if(threads[i.value ()].getEx () != null)
                numOfExceptions ++;
        }
        Assertions.assertTrue(numOfExceptions >= 25);
    }

    @Test
    @DisplayName("Open new shop concurrency test")
    public void openNewShopConcurrencyTest() throws MarketException {
        int numOfExceptions = 0;
        registerAndLogin ();
        for ( i.reset () ; i.value () < 5 ; i.increment () ) {
            threads[i.value ()] = new MyThread ( );
            threads[i.value ()].setRunnable ( new MyRunnable ( i.value () ) {
                @Override
                public void run() throws MarketException{
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {}
                    market.openNewShop ( names[index], "sameShopName" );
                }
            } );
        }
        for (i.reset () ; i.value () < 5 ; i.increment () ) {
            threads[i.value ()].start ( );
        }
        for(i.reset (); i.value () < 5; i.increment ()) {
            try {
                threads[i.value ()].join ( );
            } catch (InterruptedException e) {}
        }
        for (i.reset () ; i.value () < 5 ; i.increment () ) {
            if(threads[i.value ()].getEx () != null)
                numOfExceptions ++;
        }
        Assertions.assertTrue(numOfExceptions == 4);
    }

//    @Test
//    @DisplayName("Add an item to shop concurrency test")
//    public void addItemToSHopShopConcurrencyTest() throws MarketException {
//        createShop("cheapMarket");
//        registerAndLogin();
//        for ( i.reset () ; i.value () < 5 ; i.increment () ) {
//            market.appointShopOwner ( initialLoggedIn, names[i.value ()],  "cheapMarket");
//        }
//
//        for ( i.reset () ; i.value () < 5 ; i.increment () ) {
//            threads[i.value ()] = new MyThread ( );
//            threads[i.value ()].setRunnable ( new MyRunnable ( i.value () ) {
//                @Override
//                public void run() throws MarketException{
//                    try {
//                        Thread.sleep(500);
//                    } catch (InterruptedException e) {}
//                    String itemName = "justItemName";
//                    market.addItemToShop ( names[index], itemName, i.value (), Item.Category.fruit, "", new ArrayList<>(), 1, "cheapMarket" );
//                }
//            } );
//        }
//        for (i.reset () ; i.value () < 5 ; i.increment () ) {
//            threads[i.value ()].start ( );
//        }
//        for(i.reset (); i.value () < 5; i.increment ()) {
//            try {
//                threads[i.value ()].join ( );
//            } catch (InterruptedException e) {}
//        }
//        List<Item> itemsAdded = market.getItemByName("justItemName");
//        Assertions.assertTrue(itemsAdded.size() == 1);
//    }

    @Test
    @DisplayName("Purchase concurrency test")
    public void purchaseConcurrencyTest() throws MarketException {
        registerAndLogin();
        market.openNewShop ( names[0],  "shopName5");
        market.addItemToShop ( names[0], "itemToBuy", 10, Item.Category.fruit, null, null, 1, "shopName5" );

        for ( i.reset () ; i.value () < 5 ; i.increment () ) {
            threads[i.value ()] = new MyThread ( );
            threads[i.value ()].setRunnable ( new MyRunnable ( i.value () ) {
                @Override
                public void run() throws MarketException {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                    }
                    List<Item> items = market.getItemByName("itemToBuy");
                    Item item = items.get(0);
                    try {
                        market.addItemToShoppingCart(item, 1, names[index]);
                        market.buyShoppingCart(names[index], 10, new CreditCard("1234567890111111", "5", "2024", "555", "Ido livne", "204534839"), new Address("Ido livne", "harimon 7", "ramat-gan", "israel", "123456"));
                    }catch(Exception e){}
                }
            } );
        }
        for (i.reset () ; i.value () < 5 ; i.increment () ) {
            threads[i.value ()].start ( );
        }
        for (i.reset () ; i.value () < 5 ; i.increment () ) {
            try {
                threads[i.value ()].join ( );
            } catch (InterruptedException e) {}
        }
//        for (i.reset () ; i.value () < 5 ; i.increment () ) {
//            if(threads[i.value ()].getEx () != null)
//                numOfExceptions ++;
//        }
        int numOfSuccess = 0;
        for(int index = 0; index < 5; index++)
            if (userController.getVisitor (names[index]).getCart () == null)
                numOfSuccess ++;
        assert numOfSuccess <= 1;
    }

    private static synchronized void registerAndLogin() {
        for ( i.reset (); i.value () < 5 ; i.increment () ) {
            Visitor visitor = market.guestLogin ();
            visitors[i.value ()] = visitor;
            String name = names[i.value ()];
            try {
                market.register (name , password );
            }catch (MarketException e){}
            try {
                market.memberLogin ( names[i.value ()], password );
                members[i.value ()] = market.validateSecurityQuestions ( names[i.value () ] , null, visitor.getName ());
            } catch (MarketException e) {}
        }
    }

    private static void createShop(String shopName) {
        Visitor visitor = market.guestLogin ();
        try {
            market.register (initialLoggedIn , password );
        } catch (MarketException e) {}
        try {
            market.memberLogin(initialLoggedIn, password);
            market.validateSecurityQuestions(initialLoggedIn, null, visitor.getName());
        } catch (MarketException e) {}
        try {
            market.openNewShop ( initialLoggedIn,  shopName);
        } catch (MarketException e) {}
    }

}

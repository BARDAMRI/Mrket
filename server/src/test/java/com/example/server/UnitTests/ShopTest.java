package com.example.server.UnitTests;

import com.example.server.businessLayer.Market.Appointment.*;
import com.example.server.businessLayer.Market.Market;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.businessLayer.Market.Item;
import com.example.server.businessLayer.Market.Shop;
import com.example.server.businessLayer.Market.ShoppingBasket;
import com.example.server.businessLayer.Market.Users.Member;
import com.example.server.businessLayer.Market.Users.UserController;
import com.example.server.businessLayer.Market.Users.Visitor;
import com.example.server.businessLayer.Publisher.NotificationHandler;
import com.example.server.businessLayer.Publisher.TextDispatcher;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopTest {
    Shop shop;
    @Mock
    Appointment appointment;
    Appointment managerAppointment;
    Appointment ownerAppointment;
    Member memberFounder;
    ShoppingBasket basket;
    Item item;

    List<String> keywords;
    UserController userController;

    static TextDispatcher textDispatcher= TextDispatcher.getInstance();

    @BeforeEach
    public void reset(){
        userController = Mockito.mock ( UserController.class );
        memberFounder = Mockito.mock(Member.class);
        Mockito.when(memberFounder.getName()).thenReturn("The founder");
        try {
            Mockito.when ( userController.getVisitor ("The founder" ) ).thenReturn ( new Visitor ( "The founder" ) );
        } catch (MarketException e) {
            throw new RuntimeException ( e );
        }
        ownerAppointment = Mockito.mock(ShopOwnerAppointment.class);
        Mockito.when(ownerAppointment.getAppointed()).thenReturn(memberFounder);
        Mockito.when(ownerAppointment.getAppointed().getName()).thenReturn("The founder");
        Mockito.when(ownerAppointment.isOwner()).thenReturn(true);
        shop = new Shop("shop", memberFounder);
        item = Mockito.mock(Item.class);
        basket = Mockito.mock(ShoppingBasket.class);
        managerAppointment = Mockito.mock(ShopManagerAppointment.class);
        keywords = new ArrayList<>();
        keywords.add("fruit");
        Mockito.when(item.getID()).thenReturn(1);
        Mockito.when(item.getName()).thenReturn("item_test");
        Member otherMember = Mockito.mock(Member.class);
        Mockito.when(otherMember.getName()).thenReturn("The twin");
        Mockito.when(managerAppointment.getAppointed()).thenReturn(otherMember);
        Mockito.when(managerAppointment.getAppointed().getName()).thenReturn("The twin");
        Mockito.when(managerAppointment.getSuperVisor()).thenReturn(memberFounder);
        Mockito.when(managerAppointment.getSuperVisor().getName()).thenReturn("The founder");
        try{
            shop.addItem("The founder", item.getName(),item.getPrice(), item.getCategory(),item.getInfo(),keywords,1.0,item.getID());
        }
        catch (MarketException e)
        {
            System.out.println(e.getMessage());
        }
    }

    @Test
    @DisplayName("Add item - good test")
    public void addItemTest() {
        try {
            Assertions.assertEquals(1,shop.getItemMap().size());
            shop.addItem("The founder","specialName",item.getPrice(), item.getCategory(),item.getInfo(),keywords,1.0,2);
            Assertions.assertEquals(2,shop.getItemMap().size());

        } catch (Exception e) {
            assert false;
        }
    }
    @Test
    @DisplayName("Add item - fail test - ID taken")
    public void addItemTestIDTaken(){
        try {
            shop.addItem("The founder", item.getName(),item.getPrice(), item.getCategory(),item.getInfo(),keywords,1.0,item.getID());
            assert false;

        } catch (MarketException e) {
            assert true;
        }
    }
    @Test
    @DisplayName("edit item - good test")
    public void editItemTest(){
        try {
            keywords.add("tasty");
            Mockito.when(item.getName()).thenReturn("itemTestNewName");
            Mockito.when(item.getID()).thenReturn(1);
            shop.editItem(item,"1");
            Assertions.assertEquals("itemTestNewName" , shop.getItemMap().get(1).getName());

        } catch (Exception e) {
            System.out.println(e.getMessage());;
            assert false;
        }
    }

    @Test
    @DisplayName("edit item - fail test - change ID")
    public void editItemFailTestChangeID(){
        try {
            Mockito.when(item.getID()).thenReturn(2);
            shop.editItem(item,"1");
            assert false;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert true;
        }
    }
    @Test
    @DisplayName("edit item - good test - Does not add")
    public void editItemFailTestDoesNotAdd(){
        try {
            Mockito.when(item.getID()).thenReturn(1);
            Mockito.when(item.getInfo()).thenReturn("brand new info");
            Mockito.when(item.getName()).thenReturn("itemTestNewName");
            shop.editItem(item,"1");
            Assertions.assertEquals(1,shop.getItemMap().size());
            Assertions.assertTrue(shop.getItemMap().get(1).getName().equals("itemTestNewName"));

        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert false;
        }
    }
    @Test
    @DisplayName("delete item - good test")
    public void deleteItemTest(){
        try {
            Assertions.assertEquals(1,shop.getItemMap().size());
            shop.deleteItem(item, shop.getShopOwners ().values ().stream ().toList ().get ( 0 ).getAppointed ().getName ());
            Assertions.assertEquals(0,shop.getItemMap().size());

        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert false;
        }
    }

    @Test
    @DisplayName("Add employee test - good test")
    public void addEmployeeTest(){
        try {
            Member testMember = Mockito.mock(Member.class);
            Mockito.when(testMember.getName()).thenReturn("raz");
            ShopOwnerAppointment testOwnerApp = Mockito.mock(ShopOwnerAppointment.class);
            Mockito.when(testOwnerApp.getAppointed()).thenReturn(testMember);
            Mockito.when(testOwnerApp.getSuperVisor()).thenReturn(memberFounder);
            Mockito.when(testOwnerApp.isOwner()).thenReturn(true);
            shop.addEmployee(testOwnerApp);
            Assertions.assertTrue(shop.isShopOwner("raz"));
        }
        catch (MarketException e)
        {
            System.out.println(e.getMessage());
            assert false;
        }
    }
    @Test
    @DisplayName("Add employee test - fail test - add same employee ")
    public void addEmployeeFailTestAddTwice(){
        try {
            shop.addEmployee(ownerAppointment);
            assert false;
        }
        catch (MarketException e)
        {
            System.out.println(e.getMessage());
            assert true;
        }
    }
    @Test
    @DisplayName("Add employee test - good test - add same owner as manager")
    public void addEmployeeFailTestOwnerAndManager(){
        try {
            ShopOwnerAppointment testOwnerApp = Mockito.mock(ShopOwnerAppointment.class);
            ShopManagerAppointment testManagerApp = Mockito.mock(ShopManagerAppointment.class);
            Member testMember = Mockito.mock(Member.class);
            Member testMember2 = Mockito.mock(Member.class);
            Mockito.when(testOwnerApp.getAppointed()).thenReturn(testMember);
            Mockito.when(testOwnerApp.getAppointed().getName()).thenReturn("raz");
            Mockito.when(testOwnerApp.isOwner()).thenReturn(true);
            Mockito.when(testOwnerApp.getSuperVisor()).thenReturn(memberFounder);
            Mockito.when(testManagerApp.getAppointed()).thenReturn(testMember);
            Mockito.when(testManagerApp.getSuperVisor()).thenReturn(memberFounder);
            shop.addEmployee(testOwnerApp);
            shop.addEmployee(testManagerApp);

            assert true;
        }
        catch (MarketException e)
        {
            System.out.println(e.getMessage());
            assert false;
        }
    }
    @Test
    @DisplayName("Set item amount - good test")
    public void setItemAmountTest(){
        try {
            shop.setItemAmount("The founder",item.getID(),10);
            Assertions.assertEquals(1,shop.getItemMap().size());

        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert false;
        }
    }

    @Test
    @DisplayName("Set item amount - fail test - negative amount")
    public void setItemAmountFailTest(){
        try {
            shop.setItemAmount("The founder",item.getID(),-10);
            assert false;

        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert true;
        }
    }
    @Test
    @DisplayName("Set item amount - good test - zero amount")
    public void setItemZeroAmountTest(){
        try {

            shop.setItemAmount("The founder",item.getID(),0);
            Assertions.assertEquals(1,shop.getItemMap().size());

        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert false;
        }
    }

    @Test
    @DisplayName("release items - good test")
    public void releaseItemsTest(){
        try {
            Item item2 = Mockito.mock(Item.class);
            Mockito.when(item2.getName()).thenReturn("item2");
            Mockito.when(item2.getID()).thenReturn(2);
            Map<java.lang.Integer, Item> items = new HashMap<>();
            items.put(item.getID(), item);
            items.put(item2.getID(), item2);
            ReflectionTestUtils.setField(shop, "itemMap", items);
            Map<java.lang.Integer, Double> currAmount = new HashMap<>();
            currAmount.put(item.getID(), 1.0);
            currAmount.put(item2.getID(), 10.0);
            ReflectionTestUtils.setField(shop, "itemsCurrentAmount", currAmount);
            Map<java.lang.Integer,Double> map = new HashMap<>();
            map.put(item2.getID(),1.0);
            Mockito.when(basket.getItems()).thenReturn(map);
            ReflectionTestUtils.setField(basket, "items", map);
            //Mockito.when(basket.getItems().get(item)).thenReturn(10.0);
            shop.releaseItems(basket);
            Assertions.assertEquals(11,shop.getItemCurrentAmount(item2));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            assert false;
        }
    }

    @Test
    @DisplayName("release items - good test - no relevant item , stays the same")
    public void releaseItemsTestNoRelevantItem(){
        try {
            Item item2 = Mockito.mock(Item.class);
            Map <Item, java.lang.Integer>basketItems = new HashMap();
            basketItems.put(item2, 10);
            Mockito.when(item2.getName()).thenReturn("item2");
            Mockito.when(item2.getID()).thenReturn(2);
            ReflectionTestUtils.setField(basket, "items", basketItems);
            Mockito.when(basket.getItems()).thenCallRealMethod();
//            shop.setItemAmount("The founder",item,0);
            shop.releaseItems(basket);
            Assertions.assertEquals(1,shop.getItemCurrentAmount(item));
        } catch (MarketException e) {
            assert true;
        } catch (Exception e){
            System.out.println(e.getMessage());
            assert false;
        }
    }

    @Test
    @DisplayName("Buy basket - good test.")
    public void buyBasketTest() {//TODO check with ido.
        Map<java.lang.Integer,Double> itemsMap = new HashMap<>();
        Item item1 = Mockito.mock(Item.class);
        Item item2 = Mockito.mock(Item.class);
        Mockito.when(item1.getID()).thenReturn(555);
        Mockito.when(item2.getID()).thenReturn(666);
        itemsMap.put(item1.getID(),5.0);
        itemsMap.put(item2.getID(),10.0);
        Mockito.when(item1.getPrice()).thenReturn(5.0);
        Mockito.when(item2.getPrice()).thenReturn(1.0);
        ReflectionTestUtils.setField(basket, "items", itemsMap);
        Mockito.when(basket.getItems()).thenReturn(itemsMap);
        Mockito.when(basket.getPrice()).thenCallRealMethod();
        Map<java.lang.Integer, Item> itemMap = new HashMap<>();
        itemMap.put(item1.getID(), item1);
        itemMap.put(item2.getID(), item2);
        ReflectionTestUtils.setField(shop, "itemMap", itemMap);
        ReflectionTestUtils.setField(basket, "itemMap", itemMap);
        Mockito.when(basket.getItemMap()).thenCallRealMethod();
        Map<java.lang.Integer, Double> currAmount = new HashMap<>();
        currAmount.put(item1.getID(), 10.0);
        currAmount.put(item2.getID(), 20.0);
        ReflectionTestUtils.setField(shop, "itemsCurrentAmount", currAmount);
        try {
            NotificationHandler nh= NotificationHandler.getInstance();
            nh.setService(textDispatcher);
            Assertions.assertEquals(35.0,shop.buyBasket(nh,basket, memberFounder.getName()));
        } catch (MarketException e) {
            System.out.println(e.getMessage());
            assert false;
        }
    }

    @Test
    @DisplayName("Validate basket.")
    public void validateBasketTest() {
        Map<java.lang.Integer, Double> items = new HashMap<>();
        Item item1 = Mockito.mock(Item.class);
        Mockito.when(item1.getID()).thenReturn(555);
        Mockito.when(item1.getID()).thenReturn(666);
        Item item2 = Mockito.mock(Item.class);
        items.put(item1.getID(), 5.0);
        items.put(item2.getID(), 5.0);
        Mockito.when(basket.getItems()).thenReturn(items);
        Map<java.lang.Integer, Item> itemsInShop = new HashMap<>();
        itemsInShop.put(item1.getID(), item1);
        itemsInShop.put(item2.getID(), item2);
        ReflectionTestUtils.setField(shop, "itemMap", items);
        Map<java.lang.Integer, Double> currAmount = new HashMap<>();
        currAmount.put(item1.getID(), 1.0);
        currAmount.put(item2.getID(), 10.0);
        ReflectionTestUtils.setField(shop, "itemsCurrentAmount", currAmount);
        shop.validateBasket(basket);
        Assertions.assertEquals(1.0, basket.getItems().get(item1.getID()));
        Assertions.assertEquals(5.0, basket.getItems().get(item2.getID()));
    }

    @Test
    @DisplayName("Remove shop owner - good test")
    public void removeShopOwnerTest(){
        ShopOwnerAppointment app1 = Mockito.mock(ShopOwnerAppointment.class);
        Mockito.when(app1.getSuperVisor()).thenReturn(memberFounder);
        Mockito.when(app1.getSuperVisor().getName()).thenReturn("The founder");
        Member remove = Mockito.mock(Member.class);
        Mockito.when(remove.getName()).thenReturn("Remove owner");
        Mockito.when(app1.getAppointed()).thenReturn(remove);
        Mockito.when(app1.getAppointed().getName()).thenReturn("Remove owner");
        Mockito.when(app1.isOwner()).thenReturn(true);
        try {
            shop.addEmployee(app1);
        } catch (MarketException e) {
            System.out.println(e.getMessage());
            assert false;
        }
        try {
            shop.removeShopOwnerAppointment(memberFounder.getName(),remove.getName());
            Assertions.assertFalse(shop.getShopOwners().containsKey(remove.getName()));
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
            assert false;
        }
    }

    @Test
    @DisplayName("Remove shop owner - fail test ")
    public void removeShopOwnerFailTest(){
        ShopOwnerAppointment app1 = Mockito.mock(ShopOwnerAppointment.class);
        Mockito.when(app1.getSuperVisor()).thenReturn(memberFounder);
        Mockito.when(app1.getSuperVisor().getName()).thenReturn("The founder");
        Member member = Mockito.mock(Member.class);
        Mockito.when(member.getName()).thenReturn("Remove owner");
        Mockito.when(app1.getAppointed()).thenReturn(member);
        Mockito.when(app1.getAppointed().getName()).thenReturn("Remove owner");
        Mockito.when(app1.isOwner()).thenReturn(true);
        try {
            shop.addEmployee(app1);
        } catch (MarketException e) {
            System.out.println(e.getMessage());
            assert false;
        }
        try {
            shop.removeShopOwnerAppointment(memberFounder.getName(),"Ido who isn't an owner");
            assert false;
        }
        catch (MarketException e)
        {
            assert true;
        }
        catch (Exception ex){
            assert false;
        }
    }
    @Test
    @DisplayName("Get pending apps for owner - valid name")
    public void GetPendingAppsForOwner(){
        Assertions.assertDoesNotThrow(()->shop.getAllPendingForOwner(memberFounder.getName()));
    }
    @Test
    @DisplayName("Get pending apps for owner - Invalid name")
    public void GetPendingAppsForNotOwner(){
        Assertions.assertThrows(MarketException.class,()->shop.getAllPendingForOwner("not owner"));
    }
    @Test
    @DisplayName("Approve appointment - good test")
    public void approveAppointmentTest(){
        ShopOwnerAppointment app1 = Mockito.mock(ShopOwnerAppointment.class);
        Member otherOwner = Mockito.mock(Member.class);
        Mockito.when(otherOwner.getName()).thenReturn("ido");
        ShopOwnerAppointment ownerApp = Mockito.mock(ShopOwnerAppointment.class);
        Map<String,Appointment> owners = new HashMap<>();
        owners.put(memberFounder.getName(),ownerApp);
        owners.put("ido",ownerApp);
        shop.setShopOwners(owners);
        PendingAppointments pending = Mockito.mock(PendingAppointments.class);
        Map<String, ShopOwnerAppointment> pendingMap=new HashMap<>();
        pendingMap.put("ayala",app1);
        shop.setPendingAppointments(pending);
        Mockito.when(pending.getAppointments()).thenReturn(pendingMap);
        try {
            Mockito.when(pending.approve("ayala","ido")).thenReturn(true);
        } catch (MarketException e) {
            assert false;
        }

        try {
            shop.approveAppointment("ayala","ido");
            Assertions.assertTrue(shop.getShopOwners().containsKey("ayala"));
        } catch (MarketException e) {
            assert false;
        }
    }
    @Test
    @DisplayName("Approve appointment - fail test - not an owner")
    public void approveAppointmentFailTestNotOwner(){
        ShopOwnerAppointment app1 = Mockito.mock(ShopOwnerAppointment.class);
        Member otherOwner = Mockito.mock(Member.class);
        Mockito.when(otherOwner.getName()).thenReturn("ido");
        ShopOwnerAppointment ownerApp = Mockito.mock(ShopOwnerAppointment.class);
        Map<String,Appointment> owners = new HashMap<>();
        owners.put(memberFounder.getName(),ownerApp);
        owners.put("ido",ownerApp);
        shop.setShopOwners(owners);
        PendingAppointments pending = Mockito.mock(PendingAppointments.class);
        Map<String, ShopOwnerAppointment> pendingMap=new HashMap<>();
        pendingMap.put("ayala",app1);
        shop.setPendingAppointments(pending);
        Mockito.when(pending.getAppointments()).thenReturn(pendingMap);
        try {
            Mockito.when(pending.approve("raz","ido")).thenReturn(true);
        } catch (MarketException e) {
            assert false;
        }

        try {
            shop.approveAppointment("ayala","idoo");
            assert false;
        } catch (MarketException e) {
            assert true;
        }
    }
    @Test
    @DisplayName("Reject appointment - good test")
    public void rejectAppointmentTest(){
        ShopOwnerAppointment app1 = Mockito.mock(ShopOwnerAppointment.class);
        Member otherOwner = Mockito.mock(Member.class);
        Mockito.when(otherOwner.getName()).thenReturn("ido");
        ShopOwnerAppointment ownerApp = Mockito.mock(ShopOwnerAppointment.class);
        Map<String,Appointment> owners = new HashMap<>();
        owners.put(memberFounder.getName(),ownerApp);
        owners.put("ido",ownerApp);
        shop.setShopOwners(owners);
        PendingAppointments pending = Mockito.mock(PendingAppointments.class);
        Map<String, ShopOwnerAppointment> pendingMap=new HashMap<>();
        pendingMap.put("ayala",app1);
        shop.setPendingAppointments(pending);
        Mockito.when(pending.getAppointments()).thenReturn(pendingMap);
        Agreement agreement = Mockito.mock(Agreement.class);
        Map<String,Agreement> agreementMap = new HashMap<>();
        agreementMap.put("ayala",agreement);
        Mockito.when(pending.getAgreements()).thenReturn(agreementMap);
        Map<String,Boolean> ownersApproval = new HashMap<>();
        ownersApproval.put("ido",false);
        ownersApproval.put(memberFounder.getName(),true);
        Mockito.when(pending.getAgreements().get("ayala").getOwnersAppointmentApproval()).thenReturn(ownersApproval);

        try {
            shop.rejectAppointment("ayala","ido");
            Assertions.assertFalse(shop.getShopOwners().containsKey("ayala"));
        } catch (MarketException e) {
            assert false;
        }
    }
    @Test
    @DisplayName("Reject appointment -fail test - no such owner")
    public void rejectAppointmentFailTestNoSuchOwner(){
        ShopOwnerAppointment app1 = Mockito.mock(ShopOwnerAppointment.class);
        Member otherOwner = Mockito.mock(Member.class);
        Mockito.when(otherOwner.getName()).thenReturn("ido");
        ShopOwnerAppointment ownerApp = Mockito.mock(ShopOwnerAppointment.class);
        Map<String,Appointment> owners = new HashMap<>();
        owners.put(memberFounder.getName(),ownerApp);
        owners.put("ido",ownerApp);
        shop.setShopOwners(owners);
        PendingAppointments pending = Mockito.mock(PendingAppointments.class);
        Map<String, ShopOwnerAppointment> pendingMap=new HashMap<>();
        pendingMap.put("ayala",app1);
        shop.setPendingAppointments(pending);
        Mockito.when(pending.getAppointments()).thenReturn(pendingMap);
        Agreement agreement = Mockito.mock(Agreement.class);
        Map<String,Agreement> agreementMap = new HashMap<>();
        agreementMap.put("ayala",agreement);
        Mockito.when(pending.getAgreements()).thenReturn(agreementMap);
        Map<String,Boolean> ownersApproval = new HashMap<>();
        ownersApproval.put("ido",false);
        ownersApproval.put(memberFounder.getName(),true);
        Mockito.when(pending.getAgreements().get("ayala").getOwnersAppointmentApproval()).thenReturn(ownersApproval);

        try {
            shop.rejectAppointment("ayala","idoo");
            assert false;
        } catch (MarketException e) {
            assert true;
        }
    }
    @Test
    @DisplayName("Reject appointment - fail test - No such appointed")
    public void rejectAppointmentFailTestNoSuchAppointed(){
        ShopOwnerAppointment app1 = Mockito.mock(ShopOwnerAppointment.class);
        Member otherOwner = Mockito.mock(Member.class);
        Mockito.when(otherOwner.getName()).thenReturn("ido");
        ShopOwnerAppointment ownerApp = Mockito.mock(ShopOwnerAppointment.class);
        Map<String,Appointment> owners = new HashMap<>();
        owners.put(memberFounder.getName(),ownerApp);
        owners.put("ido",ownerApp);
        shop.setShopOwners(owners);
        PendingAppointments pending = Mockito.mock(PendingAppointments.class);
        Map<String, ShopOwnerAppointment> pendingMap=new HashMap<>();
        pendingMap.put("ayala",app1);
        shop.setPendingAppointments(pending);
        Mockito.when(pending.getAppointments()).thenReturn(pendingMap);
        Agreement agreement = Mockito.mock(Agreement.class);
        Map<String,Agreement> agreementMap = new HashMap<>();
        agreementMap.put("ayala",agreement);
        Mockito.when(pending.getAgreements()).thenReturn(agreementMap);
        Map<String,Boolean> ownersApproval = new HashMap<>();
        ownersApproval.put("ido",false);
        ownersApproval.put(memberFounder.getName(),true);
        Mockito.when(pending.getAgreements().get("ayala").getOwnersAppointmentApproval()).thenReturn(ownersApproval);

        try {
            shop.rejectAppointment("ayalaaaa","ido");
            assert false;
        } catch (MarketException e) {
            assert true;
        }
    }



}

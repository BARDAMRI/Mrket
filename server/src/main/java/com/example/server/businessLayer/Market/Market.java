package com.example.server.businessLayer.Market;

import com.example.server.businessLayer.Market.Appointment.Appointment;

import com.example.server.businessLayer.Market.Appointment.ShopManagerAppointment;
import com.example.server.businessLayer.Market.Appointment.ShopOwnerAppointment;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.DiscountType;
import com.example.server.businessLayer.Market.Policies.PurchasePolicy.PurchasePolicyType;
import com.example.server.businessLayer.Payment.PaymentService;
import com.example.server.businessLayer.Payment.PaymentServiceProxy;
import com.example.server.businessLayer.Payment.WSEPPaymentServiceAdapter;
import com.example.server.businessLayer.Publisher.Publisher;
import com.example.server.businessLayer.Publisher.TextDispatcher;
import com.example.server.businessLayer.Supply.Address;
import com.example.server.businessLayer.Payment.PaymentMethod;
import com.example.server.businessLayer.Supply.SupplyService;
import com.example.server.businessLayer.Supply.SupplyServiceProxy;
import com.example.server.businessLayer.Market.ResourcesObjects.*;
import com.example.server.businessLayer.Publisher.NotificationDispatcher;
import com.example.server.businessLayer.Publisher.NotificationHandler;
import com.example.server.businessLayer.Security.Security;
import com.example.server.businessLayer.Market.Users.Member;
import com.example.server.businessLayer.Market.Users.UserController;
import com.example.server.businessLayer.Market.Users.Visitor;
import com.example.server.businessLayer.Supply.WSEPSupplyServiceAdapter;
import com.example.server.serviceLayer.Notifications.RealTimeNotifications;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class Market {
    private UserController userController;
    private String systemManagerName;
    private Map<String, Shop> shops;                                 // <shopName, shop>
    private NotificationHandler notificationHandler;
    private Map<java.lang.Integer, String> allItemsInMarketToShop;             // <itemID,ShopName>
    private Map<String, List<java.lang.Integer>> itemByName;                   // <itemName ,List<itemID>>
    private SynchronizedCounter nextItemID;
    private PaymentServiceProxy paymentServiceProxy;
    private SupplyServiceProxy supplyServiceProxy;
    private Publisher publisher;
    private static Market instance;
    Map<String, Integer> numOfAcqsPerShop;

    private Statistics statistics;

    private Market() {
        this.shops = new ConcurrentHashMap<>();
        this.allItemsInMarketToShop = new ConcurrentHashMap<>();
        this.itemByName = new ConcurrentHashMap<>();
        this.userController = UserController.getInstance();
        nextItemID = new SynchronizedCounter();
        this.numOfAcqsPerShop = new HashMap<>();
        paymentServiceProxy = null;
        supplyServiceProxy = null;
        publisher = null;
        notificationHandler = null;
        statistics=Statistics.getInstance();
    }


    public synchronized static Market getInstance() {
        if (instance == null) {
            instance = new Market();
        }
        return instance;
    }

    /**
     * init system from default files. With given system manager details.
     *
     * @param userName the system manager username
     * @param password the system manager password.
     * @throws MarketException
     */
    public synchronized void firstInitMarket(String userName, String password) throws MarketException {

        try {
            if (this.paymentServiceProxy != null || this.supplyServiceProxy != null) {
                DebugLog.getInstance().Log("A market initialization failed .already initialized");
                throw new MarketException("market is already initialized");
            }
            readConfigurationFile(MarketConfig.SERVICES_FILE_NAME);
            if (userName != null && !userName.isEmpty() & password != null && !password.isEmpty()) {
                register(userName, password);
                if(instance.systemManagerName == null) {
                    instance.systemManagerName = userName;
                    statistics.setSystemManager(userName);
                }
            }
            checkSystemInit();
            EventLog eventLog = EventLog.getInstance();
            eventLog.Log("A market has been initialized successfully");
        } catch (MarketException e) {
            throw e;
        }
    }



    public StringBuilder getAllSystemPurchaseHistory(String memberName) throws MarketException {
        alertIfNotLoggedIn(memberName);
        if (!systemManagerName.equals(memberName)) {
            DebugLog debugLog = DebugLog.getInstance();
            debugLog.Log("Member who is not the system manager tried to access system purchase history");
            throw new MarketException("member is not a system manager so is not authorized to get th information");
        }
        StringBuilder history = new StringBuilder("Market history: \n");
        for (Shop shop : shops.values()) {
            history.append(shop.getReview()).append("\n");
        }
        EventLog eventLog = EventLog.getInstance();
        eventLog.Log("System manager got purchase history");
        return history;
    }


    public StringBuilder getHistoryByShop(String member, String shopName) throws MarketException {
        alertIfNotLoggedIn(member);
        if (!systemManagerName.equals(member)) {
            DebugLog debugLog = DebugLog.getInstance();
            debugLog.Log("Member who is not the system manager tried to access system purchase history");
            throw new MarketException("member is not a system manager so is not authorized to get th information");
        }
        Shop shop = shops.get(shopName);
        if (shop == null) {
            DebugLog debugLog = DebugLog.getInstance();
            debugLog.Log("User tried to get shop history for a non exiting shop");
            throw new MarketException("shop does not exist in the market");
        }
        return shop.getReview();
    }

    public StringBuilder getHistoryByMember(String systemManagerName, String memberName) throws MarketException {
        alertIfNotLoggedIn(systemManagerName);
        if (systemManagerName.equals(this.systemManagerName)) {
            DebugLog debugLog = DebugLog.getInstance();
            debugLog.Log("Member who is not the system manager tried to access system purchase history");
            throw new MarketException("member is not a system manager so is not authorized to get th information");
        }

        Member member = userController.getMember(memberName);
        if (member == null) {
            DebugLog debugLog = DebugLog.getInstance();
            debugLog.Log("Tried to get history for a non existing member");
            throw new MarketException("member does not exist");
        }
        StringBuilder history = member.getPurchaseHistoryString();
        return history;
    }


    public void register(String name, String password) throws MarketException {
        Security security = Security.getInstance();
        security.validateRegister(name, password);
        userController.register(name);
        EventLog eventLog = EventLog.getInstance();
        eventLog.Log("A new user registered , welcome " + name);
    }


    public List<String> memberLogin(String userName, String userPassword) throws MarketException {
        Security security = Security.getInstance();
        return security.validatePassword(userName, userPassword);
    }


    public Visitor guestLogin() {
        Visitor visitor= userController.guestLogin();
        RealTimeNotifications notifications= new RealTimeNotifications();
        notifications.createUserLoggedIn(visitor.getName(),userController.getVisitorsInMarket().size());
        statistics.incNumOfVisitors();
        return visitor;
    }


    public Shop getShopByName(String shopName) {
        return shops.get(shopName);
    }


    public void addSecurity() {

    }

    public ShoppingCart calculateShoppingCart(String visitorName) throws MarketException {
        alertIfNotLoggedIn(visitorName);
        ShoppingCart currentCart = userController.getVisitorsInMarket().get(visitorName).getCart();
        ShoppingCart updatedCart = validateCart(currentCart);
        return updatedCart;
    }


    public Map<String, Shop> getShops() {
        return shops;
    }


    public Map<java.lang.Integer, String> getAllItemsInMarketToShop() {
        return allItemsInMarketToShop;
    }

    public List<Item> getItemByName(String name) {
        if (!itemByName.containsKey(name)) {
            return new ArrayList<>();
        }
        List<Item> toReturn = new ArrayList<>();
        List<java.lang.Integer> itemIds = itemByName.get(name);
        for (int item : itemIds) {
            String shopStr = allItemsInMarketToShop.get(item);
            Shop shop = shops.get(shopStr);
            toReturn.add(shop.getItemMap().get(item));
        }
        EventLog.getInstance().Log("Filtered items by item name.");
        return toReturn;
    }

    public List<Item> getItemByCategory(Item.Category category) {
        List<Item> toReturn = new ArrayList<>();
        for (Shop shop : shops.values()) {
            toReturn.addAll(shop.getItemsByCategory(category));
        }
        EventLog.getInstance().Log("Filtered items by item category.");
        return toReturn;
    }

    public List<Item> getItemsByKeyword(String keyword) {
        List<Item> toReturn = new ArrayList<>();
        for (Shop shop : shops.values()) {
            toReturn.addAll(shop.getItemsByKeyword(keyword));
        }
        EventLog.getInstance().Log("Filtered items by keywords.");
        return toReturn;
    }

    public List<Item> filterItemsByPrice(List<Item> items, double minPrice, double maxPrice) {
        List<Item> toReturn = new ArrayList<>();
        for (Item item : items) {
            if (item.getPrice() >= minPrice && item.getPrice() <= maxPrice)
                toReturn.add(item);
        }
        EventLog.getInstance().Log("Filtered items by price.");
        return toReturn;
    }

    public List<Item> filterItemsByCategory(List<Item> items, Item.Category category) {
        List<Item> toReturn = new ArrayList<>();
        for (Item item : items) {
            if (item.getCategory().equals(category))
                toReturn.add(item);
        }
        return toReturn;
    }


    public static void setInstance(Market instance) {
        Market.instance = instance;
    }

    public PaymentServiceProxy getPaymentService() {
        return paymentServiceProxy;

    }

    //TODO make private

    /**
     * @param memberName the paying member's name.
     * @throws MarketException
     */
    public void setPaymentServiceProxy(PaymentServiceProxy paymentService1, String memberName, boolean override) throws MarketException {
        if (!override && !userController.isLoggedIn(memberName)) {
            DebugLog.getInstance().Log("Member must be logged in for making this action");
            throw new MarketException("Member must be logged in for making this action");
        }
        if (!override && !memberName.equals(systemManagerName)) {
            DebugLog.getInstance().Log("Only a system manager can change the payment service");
            throw new MarketException("Only a system manager can change the payment service");
        }
        if (!override && paymentService1 == null) {
            DebugLog.getInstance().Log("Try to initiate payment service with null");
            throw new MarketException("Try to initiate payment service with null");
        }
        this.paymentServiceProxy = paymentService1;
    }

    public boolean setPaymentService(PaymentService paymentService1, String memberName) throws MarketException {
        alertIfNotLoggedIn(memberName);
        if (!memberName.equals(systemManagerName)) {
            DebugLog.getInstance().Log("Only a system manager can change the payment service");
            throw new MarketException("Only a system manager can change the payment service");
        }

        this.paymentServiceProxy.setService(paymentService1);
        return true;
    }

    public boolean setPaymentServiceAddress(String address, String managerName) throws MarketException {
        if (!userController.isLoggedIn(managerName)) {
            DebugLog.getInstance().Log("Member must be logged in for making this action");
            throw new MarketException("Member must be logged in for making this action");
        }
        if (!managerName.equals(systemManagerName)) {
            DebugLog.getInstance().Log("Only a system manager can change the payment service");
            throw new MarketException("Only a system manager can change the payment service");
        }

        this.paymentServiceProxy.setAddress(address);
        return true;
    }

    public boolean setSupplyService(SupplyService supplyService1, String memberName) throws MarketException {
        if (!userController.isLoggedIn(memberName)) {
            DebugLog.getInstance().Log("Member must be logged in for making this action");
            throw new MarketException("Member must be logged in for making this action");
        }
        if (!memberName.equals(systemManagerName)) {
            DebugLog.getInstance().Log("Only a system manager can change the supply service");
            throw new MarketException("Only a system manager can change the supply service");
        }

        this.supplyServiceProxy.setService(supplyService1);
        return true;
    }
    public boolean setSupplyServiceAddress(String address, String memberName) throws MarketException {
        if (!userController.isLoggedIn(memberName)) {
            DebugLog.getInstance().Log("Member must be logged in for making this action");
            throw new MarketException("Member must be logged in for making this action");
        }
        if (!memberName.equals(systemManagerName)) {
            DebugLog.getInstance().Log("Only a system manager can change the supply service");
            throw new MarketException("Only a system manager can change the supply service");
        }

        this.supplyServiceProxy.setAddress(address);
        return true;
    }

    public Member validateSecurityQuestions(String userName, List<String> answers, String visitorName) throws MarketException {
        Security security = Security.getInstance();
        security.validateQuestions(userName, answers);
        Member member = userController.getMembers().get(userName);
        List<Appointment> appointmentByMe = member.getAppointedByMe();
        List<Appointment> myAppointments = member.getMyAppointments();
        userController.finishLogin(userName, visitorName);
        Member ret=  new Member(member.getName(), member.getMyCart(), appointmentByMe, myAppointments, member.getPurchaseHistory());//,member.getPurchaseHistory()
        setLoginMemberPermissionStatistics(ret);
        return ret;
    }

    private void setLoginMemberPermissionStatistics(Member ret) {
        List<Appointment> myAppointments= ret.getMyAppointments();
        boolean isOwner=false;
        boolean isManager=false;
        for(Appointment appointment : myAppointments){
            if(appointment instanceof ShopOwnerAppointment){
                isOwner=true;
            }
            else if( appointment instanceof ShopManagerAppointment){
                isManager=true;
            }
        }
        if(ret.getName().equals(systemManagerName)){
            statistics.incNumOfSystemManagers(ret.getName());
        }
        if (isOwner){
            statistics.incNumOfOwners(ret.getName());
        }
        else if(isManager){
            statistics.incNumOfManagers(ret.getName());
        }
        else {
            statistics.incNumOfMembers(ret.getName());
        }
    }

    public void visitorExitSystem(String visitorName) throws MarketException {
        alertIfNotLoggedIn(visitorName);
        userController.exitSystem(visitorName);
        RealTimeNotifications notifications= new RealTimeNotifications();
        notifications.createUserLoggedout(visitorName,userController.getVisitorsInMarket().size());
        EventLog.getInstance().Log("A visitor exited the market.");
    }

    public Appointment getManagerAppointment(String shopOwnerName, String managerName, String relatedShop) throws MarketException {
        alertIfNotLoggedIn(shopOwnerName);
        for (Map.Entry<String, Shop> shopEntry : this.shops.entrySet()) {
            Shop shop = shopEntry.getValue();
            if (shop.getShopName().equals(relatedShop)) {
                return shop.getManagerAppointment(shopOwnerName, managerName);
            }
        }
        throw new MarketException("shop couldn't be found");
    }

    public void editShopManagerPermissions(String shopOwnerName, String managerName, String relatedShop, Appointment updatedAppointment) throws MarketException {
        alertIfNotLoggedIn(shopOwnerName);
        Shop shop = shops.get(relatedShop);
        if (shop == null) {
            DebugLog.getInstance().Log(String.format("related shop %s does not exist in the market", relatedShop));
            throw new MarketException(String.format("related shop %s does not exist in the market"), relatedShop);
        }
        shop.editManagerPermission(shopOwnerName, managerName, updatedAppointment);
        EventLog.getInstance().Log("Permissions for"+managerName+"in the shop:"+relatedShop+" has been edited by:"+shopOwnerName);
    }

    public void closeShop(String shopOwnerName, String shopName) throws MarketException {
        alertIfNotLoggedIn(shopOwnerName);

        Shop shopToClose = shops.get(shopName);
        if (shopToClose == null) {
            DebugLog.getInstance().Log("Tried to close shop named " + shopName + " but there is no such shop.");
            throw new MarketException("Tried to close non existing shop");
        }
        if (shopToClose.getShopFounder().getName().equals(shopOwnerName)) {
            //shops.remove(shopName);
            removeClosedShopItemsFromMarket(shopToClose);
            //send Notification V2
            ClosedShopsHistory history = ClosedShopsHistory.getInstance();
            history.closeShop(shopToClose);
            //send notifications to shop owners:
            try {
                notificationHandler.sendShopClosedBatchNotificationsBatch(new ArrayList<>(shopToClose.getShopOwners().values().stream()
                        .collect(Collectors.toList()).stream().map(appointment -> appointment.getAppointed().getName())
                        .collect(Collectors.toList())), shopName);
            } catch (Exception e) {
            }
            shopToClose.setClosed(true);
            EventLog.getInstance().Log("The shop " + shopName + " has been closed.");
        }
    }


    public void removeItemFromShop(String shopOwnerName, int itemID, String shopName) throws MarketException {
        alertIfNotLoggedIn(shopOwnerName);
        Shop shop = shops.get(shopName);
        if (shop == null) {
            DebugLog.getInstance().Log("Tried to remove item from non existing shop");
            throw new MarketException("shop does not exist in the market");
        }
        Item itemToDelete = shop.getItemMap().get(itemID);
        userController.updateVisitorsInRemoveOfItem(shop, itemToDelete);
        shop.deleteItem(itemToDelete, shopOwnerName);
        updateMarketOnDeleteItem(itemToDelete);
        EventLog.getInstance().Log("Item removed from and market.");
    }


    public Shop addItemToShop(String shopOwnerName, String itemName, double price, Item.Category category, String info,
                              List<String> keywords, double amount, String shopName) throws MarketException {
        alertIfNotLoggedIn(shopOwnerName);
        Shop shop = shops.get(shopName);
        if (shop == null) {
            DebugLog.getInstance().Log("Tried to add item to a non existing shop.");
            throw new MarketException("shop does not exist in the market");
        }
        try {
            Item addedItem = shop.addItem(shopOwnerName, itemName, price, category, info, keywords, amount, nextItemID.increment());
            updateMarketOnAddedItem(addedItem, shopName);
            EventLog.getInstance().Log("Item added to shop " + shopName);
            return shop;
        } catch (MarketException e) {
            nextItemID.decrement();
            throw e;
        }
    }


    public Double getItemCurrentAmount(int id) throws MarketException {
        String shopName = allItemsInMarketToShop.get(id);
        if (shopName == null)
            throw new MarketException("not such item in the market");
        Item item = getItemByID(id);
        return shops.get(shopName).getItemCurrentAmount(item);
    }

    public void setItemCurrentAmount(String shopOwnerName, Item item, double amount, String shopName) throws MarketException {
        alertIfNotLoggedIn(shopOwnerName);
        Shop shop = shops.get(shopName);
        if (shop == null) {
            DebugLog.getInstance().Log("Tried to edit item on a non existing shop.");
            throw new MarketException("shop does not exist in system");
        }
        shop.setItemAmount(shopOwnerName, item.getID(), amount);
        EventLog.getInstance().Log("Item " + item.getName() + " amount has been updated.");
    }

    public String memberLogout(String member) throws MarketException {
        alertIfNotLoggedIn(member);
        String ret= userController.memberLogout(member);
        RealTimeNotifications notifications= new RealTimeNotifications();
        notifications.createMemberLoggedOut(member,ret);
        EventLog.getInstance().Log("A member logged out from the system");
        return ret;
    }

    public void addPersonalQuery(String userAdditionalQueries, String userAdditionalAnswers, String member) throws MarketException {
        alertIfNotLoggedIn(member);
        Security security = Security.getInstance();
        security.addPersonalQuery(userAdditionalQueries, userAdditionalAnswers, member);
        EventLog.getInstance().Log("New personal query has been added to :"+member);
    }


    public Map<String, Appointment> getShopEmployeesInfo(String shopManagerName, String shopName) throws MarketException {
        alertIfNotLoggedIn(shopManagerName);
        if (!shops.containsKey(shopName)) {
            DebugLog.getInstance().Log("Tried to get employees info in a non existing shop.");
            throw new MarketException("shop does not exist");
        }

        EventLog.getInstance().Log("Owner got shop info.");
        return shops.get(shopName).getShopEmployeesInfo(shopManagerName);
    }

    public Shop getShopInfo(String member, String shopName) throws MarketException {
        alertIfNotLoggedIn(member);
        if (!shops.containsKey(shopName)) {
            if (!ClosedShopsHistory.getInstance().isClosed(shopName)) {
                EventLog.getInstance().Log(String.format("Asked for shop info while shop %s does not exist", shopName));
                throw new MarketException("shop does not exist in the market");
            }
            if (member.equals(systemManagerName)) {
                return ClosedShopsHistory.getInstance().getClosedShops().get(shopName);
            }
            throw new MarketException("only a system manager can get information about a closed shop");
        }
        Shop shop = shops.get(shopName);
        if (shop.isClosed()){
            DebugLog.getInstance().Log("user tried to reach a closed shop");
            throw new MarketException("the shop has been closed! there for cannot enter it any more");
        }
        //TODO - need to be an employee or not??
//        if (!shop.isEmployee(member)) {
//            throw new MarketException("You are not employee in this shop");
//        }
        return shop.getShopInfo(member);
    }

    //TODO check that shop name is not ""
    public boolean openNewShop(String visitorName, String shopName) throws MarketException {
        alertIfNotLoggedIn(visitorName);
        Member curMember;
        if (userController.isMember(visitorName)) {
            curMember = userController.getMember(visitorName);
            synchronized (shops) {
                if (shops.get(shopName) == null) {
                    if (shopName == null || shopName.length() == 0)
                        throw new MarketException("shop name length has to be positive");
                    Shop shop = new Shop(shopName, curMember);
//                ShopOwnerAppointment shopFounder = new ShopOwnerAppointment (curMember, null, shop, true );
//                shop.addEmployee(shopFounder);
                    shops.put(shopName, shop);

                } else {
                    DebugLog.getInstance().Log(visitorName + " tried to open a shop with taken name.");
                    throw new MarketException("Shop with the same shop name is already exists");
                }
            }
        } else {
            DebugLog.getInstance().Log("Non member tried to open a shop.");
            throw new MarketException("You are not a member. Only members can open a new shop in the market");
        }
        statistics.incNumOfOwners(visitorName);
        EventLog.getInstance().Log(visitorName + " opened a new shop named:" + shopName);
        return true;
    }


    //TODO -delete shop name
    public void addItemToShoppingCart(Item item, double amount, String visitorName) throws MarketException {
        alertIfNotLoggedIn(visitorName);
        ShoppingCart shoppingCart = userController.getVisitor(visitorName).getCart();
        if (!allItemsInMarketToShop.containsKey(item.getID())) {
            throw new MarketException("Cannot add item that does not exists in the shop.");
        }
        Shop curShop = shops.get(allItemsInMarketToShop.get(item.getID()));
        if (curShop == null) {
            EventLog.getInstance().Log("Tried to add item to cart from non existing shop.");
            throw new MarketException("this shop does not exist in the market");
        }
        if (item == null) {
            DebugLog.getInstance().Log("Tried to add null to cart.");
            throw new MarketException("this item does not exist in this shop");
        }
        if (amount < 0 || amount == 0) {
            DebugLog.getInstance().Log("Cant add item with negative or zero amount");
            throw new MarketException("Cant add item with negative amount");
        }
        if (!curShop.hasItem(item)) {
            DebugLog.getInstance().Log("Cannot add item that does not exists in the shop.");
            throw new MarketException("Cannot add item that does not exists in the shop.");
        }
        shoppingCart.addItem(curShop, item, amount);
        EventLog.getInstance().Log(amount + " " + item.getName() + " added to cart.");
    }

    public StringBuilder getShopPurchaseHistory(String shopManagerName, String shopName) throws MarketException {
        alertIfNotLoggedIn(shopManagerName);
        Shop shopToHistory = shops.get(shopName);
        if (shopToHistory == null) {
            DebugLog.getInstance().Log("Tried to get history for a non existing shop");
            throw new MarketException("shop does not exist in the market");
        }
        return shopToHistory.getPurchaseHistory(shopManagerName);
    }

    public void appointShopOwner(String shopOwnerName, String appointedShopOwner, String shopName) throws MarketException {
        alertIfNotLoggedIn(shopOwnerName);
        Member appointed = userController.getMember(appointedShopOwner);
        if (appointed == null) {
            DebugLog.getInstance().Log("Tried to appoint non member.");
            throw new MarketException("appointed shop owner is not a member");
        }
        Shop shop = shops.get(shopName);
        if (shop == null) {
            DebugLog.getInstance().Log("Tried to appoint shop owner for non existing shop.");
            throw new MarketException("shop does not exist in the market");
        }
        Member shopOwner = userController.getMember(shopOwnerName);
        shop.appointShopOwner(shopOwner, appointed);

    }

    public void appointShopManager(String shopOwnerName, String appointedShopManager, String shopName) throws MarketException {
        alertIfNotLoggedIn(shopOwnerName);
        Member appointed = userController.getMember(appointedShopManager);
        if (appointed == null) {
            DebugLog.getInstance().Log("Tried to appoint non member.");
            throw new MarketException("appointed shop manager is not a member");
        }
        Shop shop = shops.get(shopName);
        if (shop == null) {
            DebugLog.getInstance().Log("Tried to appoint shop manager for non existing shop.");
            throw new MarketException("Tried to appoint shop manager for non existing shop.");
        }
        Member shopOwner = userController.getMember(shopOwnerName);
        shop.appointShopManager(shopOwner, appointed);
        try {
            if(userController.isLoggedIn(appointedShopManager)) {
                statistics.incNumOfManagers(appointedShopManager);
            }
            notificationHandler.sendNewShopManager(shopOwner, appointed, shopName);
        } catch (Exception e) {
            ErrorLog.getInstance().Log("Failed to notify new shop manager on the appointment.");
        }
    }

    public boolean editCart(double amount, Item item, String shopName, String visitorName) throws MarketException {
        alertIfNotLoggedIn(visitorName);
        Visitor visitor = userController.getVisitor(visitorName);
        if (visitor == null) {
            DebugLog.getInstance().Log("Non member ");
            throw new MarketException("member does not exists, cannot update amount.");
        }
        return visitor.updateAmountInCart(amount, item, shopName);
    }


    public void changeShopItemInfo(String shopOwnerName, String info, java.lang.Integer oldItem, String shopName) throws MarketException {
        alertIfNotLoggedIn(shopOwnerName);
        Shop shop = shops.get(shopName);
        if (shop == null) {
            DebugLog.getInstance().Log("Tried to edit item in a non existing shop");
            throw new MarketException("shop does not exist in the market");
        }
        EventLog.getInstance().Log("Edited the item with id " + oldItem + " in the shop " + shopName);
        shop.changeShopItemInfo(shopOwnerName, info, oldItem);
    }
    public void reopenClosedShop(String shopName,String name) throws MarketException {
        Shop shopToOpen = shops.get(shopName);
        if (shopToOpen==null)
        {
            DebugLog.getInstance().Log("You tried to reopen a shop with invalid name.");
            throw new MarketException("You tried to reopen a shop with invalid name.");
        }
        if (!shopToOpen.isClosed()){
            DebugLog.getInstance().Log("The shop "+shopName+" is not closed.");
            throw new MarketException("The shop "+shopName+" is not closed.");
        }
        if (!shopToOpen.getShopFounder().getName().equals(name)){
            DebugLog.getInstance().Log("You are not the shop founder . Only the founder can reopen the shop");
            throw new MarketException("You are not the shop founder . Only the founder can reopen the shop");
        }
        ClosedShopsHistory.getInstance().reopenShop(shopName);
        shopToOpen.setClosed(false);
        validateAllEmployees(shopToOpen);
        addItemsFromReopenedShop(shopToOpen);
        try {
            List<String> works=  shopToOpen.getShopOwners().values().stream().map(x -> x.getAppointed().getName()).collect(Collectors.toList());
            works.addAll(shopToOpen.getShopManagers().values().stream().map(x-> x.getAppointed().getName()).collect(Collectors.toList()));
            NotificationHandler.getInstance().sendReOpenedShopBatch(works, name, shopName);
            EventLog.getInstance().Log("Message has been sent to shop workers about the re-open.");
        } catch (Exception e) {
            ErrorLog.getInstance().Log("Could not send notification to shop workers about the re-open.");
        }
        EventLog.getInstance().Log(shopName+" has been re-opened.");
    }

    private void addItemsFromReopenedShop(Shop shopToOpen) {
        Map<Integer, Item> shopItems = shopToOpen.getItemMap();
        for (Map.Entry<Integer,Item> entry:shopItems.entrySet())
        {
            this.allItemsInMarketToShop.put(entry.getKey(),shopToOpen.getShopName());
            if (itemByName.containsKey(entry.getValue().getName())){
                itemByName.get(entry.getValue().getName()).add(entry.getKey());
            }
            else {
                List<Integer> lst = new ArrayList<>();
                lst.add(entry.getKey());
                itemByName.put(entry.getValue().getName(),lst);
            }
        }
    }

    private void validateAllEmployees(Shop shopToOpen) {
        Map<String,Appointment> employees =  shopToOpen.getShopOwners();
        for (Map.Entry<String ,Appointment> emp: employees.entrySet())
        {
            if (!userController.isMember(emp.getKey()))
            {
                employees.remove(emp.getKey());
            }
        }
        shopToOpen.setShopOwners(employees);
        employees = shopToOpen.getShopManagers();
        for (Map.Entry<String ,Appointment> emp: employees.entrySet())
        {
            if (!userController.isMember(emp.getKey()))
            {
                employees.remove(emp.getKey());
            }
        }
        shopToOpen.setShopManagers(employees);

    }

    public ShoppingCart showShoppingCart(String visitorName) throws MarketException {
        alertIfNotLoggedIn(visitorName);
        return userController.getVisitor(visitorName).getCart();
    }

    public void editItem(Item newItem, String id) throws MarketException {
        String shopName = allItemsInMarketToShop.get(java.lang.Integer.parseInt(id));
        if (shopName == null)
            throw new MarketException("item does not exist in the market");
        Shop shop = shops.get(shopName);
        if (shop == null)
            throw new MarketException("shop does not exist in the market");
        shop.editItem(newItem, id);

    }

    public void buyShoppingCart(String visitorName, double expectedPrice, PaymentMethod paymentMethod,
                                        Address address) throws MarketException, JsonProcessingException {

        // If visitor exists under that name.
        alertIfNotLoggedIn(visitorName);
        //Import the visitor and try to get it's cart.
        Visitor visitor = userController.getVisitor(visitorName);
        ShoppingCart shoppingCart;
        Acquisition acquisition = null;

        try {
            shoppingCart = visitor.getCart();
            if (shoppingCart.isEmpty()) {
                throw new MarketException("Shopping cart is not exists for the user.");
            }
        } catch (Exception e) {
            ErrorLog errorLog = ErrorLog.getInstance();
            errorLog.Log("Shopping cart is not exists for the user.");
            throw new MarketException("Shopping cart is not exists for the user.");
        }

        if (supplyServiceProxy == null) {
            DebugLog.getInstance().Log("The supply service is not available right now.");
            throw new MarketException("The supply service is not available right now.");
        }
        if (paymentServiceProxy == null) {
            DebugLog.getInstance().Log("The payment service is not available right now.");
            throw new MarketException("The payment service is not available right now.");
        }
        //After  cart found, try to make the acquisition from each basket in the cart.
        try {
            acquisition = new Acquisition(shoppingCart, visitorName);
            visitor.getMember().addAcquisition(acquisition);
            acquisition.buyShoppingCart(notificationHandler, expectedPrice, paymentMethod, address, paymentServiceProxy, supplyServiceProxy);
            visitor.getMember().removeAcquisition(acquisition);
        } catch (Exception e) {
            if (acquisition!= null) {
                visitor.getMember().removeAcquisition(acquisition);
            }
            ErrorLog errorLog = ErrorLog.getInstance();
            errorLog.Log(e.getMessage());
            throw new MarketException(e.getMessage());
        }


    }

    private ShoppingCart validateCart(ShoppingCart currentCart) throws MarketException {
        ShoppingCart res = new ShoppingCart();
        double cartPrice = 0;
        Map<Shop, ShoppingBasket> baskets = currentCart.getCart();
        for (Map.Entry<Shop, ShoppingBasket> basketEntry : baskets.entrySet()) {
            ShoppingBasket updatedBasket = basketEntry.getKey().validateBasket(basketEntry.getValue());
            basketEntry.setValue(updatedBasket);
            cartPrice = cartPrice + basketEntry.getKey().getPriceOfShoppingBasket(updatedBasket);
        }
        currentCart.setCurrentPrice(cartPrice);
        return currentCart;
    }


    private void updateMarketOnDeleteItem(Item itemToDelete) {
        allItemsInMarketToShop.remove(itemToDelete.getID());
        itemByName.get(itemToDelete.getName()).remove(itemToDelete.getID());
    }

    private void updateMarketOnAddedItem(Item toAdd, String shopName) {
        allItemsInMarketToShop.put(toAdd.getID(), shopName);
        if (itemByName.get(toAdd.getName()) == null)
            itemByName.put(toAdd.getName(), new ArrayList<>());
        itemByName.get(toAdd.getName()).add(nextItemID.value() - 1);
    }

    private void removeClosedShopItemsFromMarket(Shop shopToClose) {
        for (Map.Entry<java.lang.Integer, Item> entry : shopToClose.getItemMap().entrySet()) {
            allItemsInMarketToShop.remove(entry.getKey());
            String itemName = entry.getValue().getName();
            if (itemByName.containsKey(itemName)) {
                itemByName.get(itemName).remove(entry.getKey());
            }
        }
        EventLog.getInstance().Log("Preparing to close the shop " + shopToClose.getShopName() + ". Removed all shop items from market.");
    }

    public Item getItemByID(java.lang.Integer id) throws MarketException {
        String itemShopName = allItemsInMarketToShop.get(id);
        if (itemShopName == null)
            throw new MarketException("no such item in market");
        Shop itemShop = shops.get(itemShopName);
        Item item = itemShop.getItemMap().get(id);
        return item;
    }

    //TODO delete
    public void reset(String systemManagerPass, List<String> questions, List<String> answers, boolean includeServices) throws MarketException {
        instance.shops = new HashMap<>();
        instance.allItemsInMarketToShop = new HashMap<>();
        instance.itemByName = new HashMap<>();
        instance.nextItemID.reset();
        Security.getInstance().reset();
        userController.reset();
        ClosedShopsHistory.getInstance().reset();
        if(includeServices){
            paymentServiceProxy = null;
            supplyServiceProxy = null;
        }
        userController.register(systemManagerName);
        Security.getInstance().addNewMember(systemManagerName, systemManagerPass, questions, answers);
    }

    public String getSystemManagerName() {
        return systemManagerName;
    }

    public void setShops(Map<String, Shop> shops) {
        this.shops = shops;
    }

    public int getNextItemID() {
        return nextItemID.increment();
    }

    public void removeShopOwnerAppointment(String boss, String firedAppointed, String shopName) throws MarketException {
        Shop shop = shops.get(shopName);
        if (shop == null) {
            DebugLog.getInstance().Log("There is no shop named:" + shopName + ". Removing shop owner has failed.");
            throw new MarketException("There is no shop named:" + shopName + ". Removing shop owner has failed.");
        }
        shop.removeShopOwnerAppointment(boss, firedAppointed);
        try {
            notificationHandler.sendAppointmentRemovedNotification(firedAppointed, shopName);
        } catch (Exception e) {
        }

    }


    public void removeMember(String manager, String memberToRemove) throws MarketException {
        if (manager.equals(memberToRemove)) {
            DebugLog.getInstance().Log("You cant remove yourself.");
            throw new MarketException("You cant remove yourself.");
        }
        if (!manager.equals(systemManagerName)) {
            DebugLog.getInstance().Log("You are not the market manager. You cant remove a member.");
            throw new MarketException("You are not the market manager. You cant remove a member.");
        }
        for (Map.Entry<String, Shop> entry : shops.entrySet()) {
            Shop shop = entry.getValue();
            if (shop.isShopOwner(memberToRemove) || shop.isManager(memberToRemove)) {
                DebugLog.getInstance().Log("You cant remove this member - He works in the market.");
                throw new MarketException("You cant remove this member - He works in the market.");
            }
        }
        if (userController.getVisitorsInMarket().containsKey(memberToRemove)) {
            DebugLog.getInstance().Log(memberToRemove + " is in market. You can't remove him");
            throw new MarketException(memberToRemove + " is in market. You can't remove him");
        }
        Security security = Security.getInstance();
        security.removeMember(memberToRemove);
        //send the notification
        NotificationHandler handler = NotificationHandler.getInstance();
        handler.setService(NotificationDispatcher.getInstance());
        RealTimeNotifications not = new RealTimeNotifications();
        not.createMembershipDeniedMessage();
        handler.sendNotification(memberToRemove, not, true);
        //
    }

    public Item getItemById(String name, int itemId) throws MarketException {
        alertIfNotLoggedIn(name);
        if (allItemsInMarketToShop.get(itemId) == null)
            throw new MarketException("there is no such an item with this id");
        return shops.get(allItemsInMarketToShop.get(itemId)).getItemMap().get(itemId);
    }

    private String getBuyingStats() {
        StringBuilder s = new StringBuilder("How many customers bought per shop:\n");
        for (Map.Entry<String, Integer> entry : this.numOfAcqsPerShop.entrySet()) {
            s.append(entry.getKey()).append(entry.getValue());
            s.append("\n");
        }
        s.append("----------------------------------------------");
        return s.toString();
    }

    public String getMarketInfo(String systemManagerName) throws MarketException {
        if (!systemManagerName.equals(this.systemManagerName)) {
            DebugLog.getInstance().Log("Only the system manager can get info about the market.");
            throw new MarketException("Only the system manager can get info about the market.");
        }
        StringBuilder s = new StringBuilder("Getting market info");
        s.append(getBuyingStats());
        s.append(userController.getUsersInfo());
        return s.toString();

    }

    public Item addItemToShopItem(String shopOwnerName, String itemName, Double productPrice, Item.Category electricity, String info, List<String> keyWords, double productAmount, String shopName) throws MarketException {
        addItemToShop(shopOwnerName, itemName, productPrice, electricity, info, keyWords, productAmount, shopName);
        Item itemAdded = getItemByID(nextItemID.value() - 1);
        return itemAdded;
    }

    public void addDiscountToShop(String visitorName, String shopName, DiscountType discountType) throws MarketException {
        alertIfNotLoggedIn(visitorName);
        Shop shop = shops.get(shopName);
        if (shop == null) {
            DebugLog.getInstance().Log("Tried to add item to a non existing shop.");
            throw new MarketException("shop does not exist in the market");
        }
        shop.addDiscountToShop(visitorName, discountType);
    }

    public void removeDiscountFromShop(String visitorName, String shopName, DiscountType discountType) throws MarketException {
        alertIfNotLoggedIn(visitorName);
        Shop shop = shops.get(shopName);
        if (shop == null) {
            DebugLog.getInstance().Log("Tried to add item to a non existing shop.");
            throw new MarketException("shop does not exist in the market");
        }
        shop.removeDiscountFromShop(visitorName, discountType);
    }

    public void addPurchasePolicyToShop(String visitorName, String shopName, PurchasePolicyType purchasePolicyType) throws MarketException {
        alertIfNotLoggedIn(visitorName);
        Shop shop = shops.get(shopName);
        if (shop == null) {
            DebugLog.getInstance().Log("Tried to add item to a non existing shop.");
            throw new MarketException("shop does not exist in the market");
        }
        shop.addPurchasePolicyToShop(visitorName, purchasePolicyType);
    }

    public void removePurchasePolicyFromShop(String visitorName, String shopName, PurchasePolicyType purchasePolicyType) throws MarketException {
        alertIfNotLoggedIn(visitorName);
        Shop shop = shops.get(shopName);
        if (shop == null) {
            DebugLog.getInstance().Log("Tried to add item to a non existing shop.");
            throw new MarketException("shop does not exist in the market");
        }
        shop.removePurchasePolicyFromShop(visitorName, purchasePolicyType);
    }

    public boolean isInit() throws MarketException {

        if (MarketConfig.USING_DATA) {
            readDataSourceConfig();
            readConfigurationFile(MarketConfig.SERVICES_FILE_NAME);
            readInitFile(MarketConfig.DATA_FILE_NAME);
            MarketConfig.USING_DATA=false;
            return true;
        }
        checkSystemInit();
        return this.systemManagerName != null && !this.systemManagerName.equals("");
    }

    private void readDataSourceConfig() throws MarketException {

        String path = getConfigDir() +MarketConfig.DATA_SOURCE_FILE_NAME;
        DataSourceConfigReader.getInstance(path);
    }

    public void loadDataFromFile(){

        if (MarketConfig.USING_DATA) {
            try {
                readInitFile(MarketConfig.DATA_FILE_NAME);
            } catch (MarketException e) {
                throw new RuntimeException(e);
            }
        }

    }
    private void readInitFile(String fileName) throws MarketException {


        File myObj = new File(getConfigDir() + fileName);
        if (!myObj.exists()) {
            throw new MarketException("Data file does not exists.");
        }
        Scanner myReader ;
        try {
            myReader = new Scanner(myObj);
        } catch (FileNotFoundException e) {
            throw new MarketException("Init data file not found.");
        }
        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            String[] vals = data.split("::");
            setData(vals);
        }

    }

    private void readConfigurationFile(String name) throws MarketException{


        File myObj = new File(getConfigDir() + name);
        if (!myObj.exists()) {
            DebugLog.getInstance().Log("Services configurations file does not exists. cannot init the market.");
            throw new MarketException("Services configurations file does not exists.");
        }
        try{
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if(data.isEmpty())
                    continue;
                String[] vals = data.split("::");
                setService(vals);
            }
        } catch (FileNotFoundException e){
            DebugLog.getInstance().Log("File not found while reading configuration file in market init");
            throw new MarketException("File not found while reading configuration file");
        }
        if (paymentServiceProxy == null || supplyServiceProxy == null) {
            DebugLog.getInstance().Log("A market initialization failed . Lack of payment / supply services ");
            throw new MarketException("Market needs payment and supply services for initialize");
        }
        if (publisher == null) {
            DebugLog.getInstance().Log("A market initialization failed . Lack of publisher services ");
            throw new MarketException("Market needs publisher services for initialize");

        }

    }

    private void setService(String[] vals) throws MarketException {

        if(vals.length==0){
            return;
        }
        if(vals.length<2){
            DebugLog.getInstance().Log(String.format("Missing init values for %s. Could not init the system services.",vals[0]));
            throw new MarketException(String.format("Missing init values for %s. Could not init the system services.",vals[0]));
        }
        if (vals[0].contains(MarketConfig.PAYMENT_SERVICE_NAME)) {
            initPaymentService(vals[1]);
        } else if (vals[0].contains(MarketConfig.SUPPLY_SERVICE_NAME)) {
            initSupplyService(vals[1]);
        } else if (vals[0].contains(MarketConfig.PUBLISHER_SERVICE_NAME)) {
            initNotificationService(vals[1]);
        }
    }

    private Map<String, String> readFromFile(String fileName) {
        Map<String,String> ret= new HashMap<>();
        try {

            File myObj = new File(getConfigDir() +fileName);
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                String[] vals = data.split("=");
                setData(vals[0], vals[1],ret);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ret;
    }

    private void initManager(String val, String val1) throws MarketException {
        register(val, val1);
        instance.systemManagerName = val;
    }

    private void initNotificationService(String val) throws MarketException {

        if (MarketConfig.IS_TEST_MODE) {
            publisher = TextDispatcher.getInstance();
        } else if (val.contains(MarketConfig.NOTIFICATIONS_PUBLISHER)) {
            publisher = NotificationDispatcher.getInstance();
        } else if (val.contains(MarketConfig.TEXT_PUBLISHER)) {
            publisher = TextDispatcher.getInstance();
        } else {
            DebugLog.getInstance().Log("Failed to init notification service.");
            throw new MarketException("Failed to init notification service");
        }
        notificationHandler = NotificationHandler.getInstance();
        notificationHandler.setService(publisher);
    }

    private void initSupplyService(String val) throws MarketException {

        if (val.contains(MarketConfig.WSEP_SERVICE)) {
            supplyServiceProxy = new SupplyServiceProxy(WSEPSupplyServiceAdapter.getInstance(), false);
        } else {
            DebugLog.getInstance().Log("Failed to init supply service.");
            throw new MarketException("Failed to init supply service");
        }
    }

    private void initPaymentService(String val) throws MarketException {

        if (val.contains(MarketConfig.WSEP_SERVICE)) {
            paymentServiceProxy = new PaymentServiceProxy(WSEPPaymentServiceAdapter.getinstance(), false);
        } else {
            DebugLog.getInstance().Log("Failed to init payment service.");
            throw new MarketException("Failed to init payment service");
        }
    }

    private void setData(String val, String val1,Map<String,String> data) {
        if(val.toLowerCase().contains("url")){
            if(!data.containsKey("url")){
                data.put("url",val1);
            }
        }
        else if(val.toLowerCase().contains("username")){
            if(!data.containsKey("username")){
                data.put("username",val1);
            }
        }
        else if(val.toLowerCase().contains("password")){
            if(!data.containsKey("password")){
                data.put("password",val1);
            }
        }
    }


    private void setData(String[] vals) throws MarketException {

        EventLog debugLog = EventLog.getInstance();
        String command = vals[0];
        if (command.contains(MarketConfig.SYSTEM_MANAGER_NAME)){
            if (systemManagerName == null || systemManagerName.isEmpty()) {
                if(vals.length!=3){
                    DebugLog.getInstance().Log("Failed to init system manager from data file.");
                    throw new MarketException("Failed to init system manager from data file.");
                }
                initManager(vals[1], vals[2]);
                statistics.setSystemManager(vals[1]);
            }
        }
        if (command.contains("Register")) {
            if (vals.length >= 3) {
                debugLog.Log("Method register from init file has called. Args are: " + vals[1] + " " + vals[2]);
                register(vals[1], vals[2]);
            } else {
                debugLog.Log("Method register from init file has called. \n Not enough args number. Number: " + vals.length);
            }
        } else if (command.contains("Login")) {
            if (vals.length >= 3) {
                debugLog.Log("Method login from init file has called. Args are: " + vals[1] + " " + vals[2]);
                Visitor vis = guestLogin();
                memberLogin(vals[1], vals[2]);
                validateSecurityQuestions(vals[1], new ArrayList<>(), vis.getName());
            } else {
                debugLog.Log("Method login from init file has called. \n Not enough args number. Number: " + vals.length);
            }

        } else if (command.contains("Logout")) {

            if (vals.length >= 2) {
                debugLog.Log("Method logout from init file has called. Args are: " + vals[1]);
                memberLogout(vals[1]);
            } else {
                debugLog.Log("Method logout from init file has called. \n Not enough args number. Number: " + vals.length);
            }

        } else if (command.contains("Open_Shop")) {

            if (vals.length >= 3) {
                debugLog.Log("Method open shop from init file has called. Args are: " + vals[1] + " " + vals[2]);
                openNewShop(vals[1], vals[2]);
            } else {
                debugLog.Log("Method open shop from init file has called. \n Not enough args number. Number: " + vals.length);
            }

        } else if (command.contains("Add_Item")) {
            if (vals.length >= 8) {
                debugLog.Log("Method add item from init file has called. Args are: " + vals[1] + " " + vals[2] + " " + vals[3] + " " + vals[4] + " " + vals[5] + " " + vals[6] + " " + vals[7]);
                addItemToShop(vals[1], vals[2], Double.parseDouble(vals[3]), Item.Category.valueOf(vals[4]), vals[5], new ArrayList<>(), Integer.parseInt(vals[6]), vals[7]);
            } else {
                debugLog.Log("Method add item from init file has called. \n Not enough args number. Number: " + vals.length);
            }
        } else if (command.contains("Appoint_Manager")) {
            if (vals.length >= 4) {
                debugLog.Log("Method appoint manager from init file has called. Args are: " + vals[1] + " " + vals[2] + " " + vals[3]);
                appointShopManager(vals[1], vals[2], vals[3]);
            } else {
                debugLog.Log("Method appoint manager from init file has called. \n Not enough args number. Number: " + vals.length);
            }
        } else if (command.contains("Appoint_Owner")) {
            if (vals.length >= 4) {
                debugLog.Log("Method appoint owner from init file has called. Args are: " + vals[1] + " " + vals[2] + " " + vals[3]);
                appointShopOwner(vals[1], vals[2], vals[3]);
            } else {
                debugLog.Log("Method appoint owner from init file has called. \n Not enough args number. Number: " + vals.length);
            }
        }
    }

    public boolean setPublishService(Publisher o, String memberName) throws MarketException {
        alertIfNotLoggedIn(memberName);
        if (!memberName.equals(systemManagerName)) {
            DebugLog.getInstance().Log("Only a system manager can change the supply service");
            throw new MarketException("Only a system manager can change the supply service");
        }

        this.notificationHandler.setService(o);
        return true;
    }


    public List<PurchasePolicyType> getPurchasePoliciesOfShop(String visitorName, String shopName) throws MarketException {
        alertIfNotLoggedIn(visitorName);
        Shop shop = shops.get(shopName);
        if (shop == null)
            throw new MarketException("shop does not exist in market");
        return shop.getPurchasePolicies();
    }

    public List<DiscountType> getDiscountTypesOfShop(String visitorName, String shopName) throws MarketException {
        alertIfNotLoggedIn(visitorName);
        Shop shop = shops.get(shopName);
        if (shop == null)
            throw new MarketException("shop does not exist in market");
        return shop.getDiscountTypes();
    }

    public boolean approveAppointment(String shopName,String appointedName,String ownerName) throws MarketException {
        Shop shop = shops.get(shopName);
        if (shop==null)
        {
            DebugLog.getInstance().Log("No such shop exist in the market.");
            throw new MarketException("No such shop exist in the market.");
        }
        boolean ret= shop.approveAppointment(appointedName,ownerName);
        if(ret && userController.isLoggedIn(appointedName)){
            statistics.incNumOfOwners(appointedName);
        }
        return ret;
    }
    public void rejectAppointment(String shopName,String appointedName,String ownerName) throws MarketException {
        Shop shop = shops.get(shopName);
        if (shop==null)
        {
            DebugLog.getInstance().Log("No such shop exist in the market.");
            throw new MarketException("No such shop exist in the market.");
        }
        shop.rejectAppointment(appointedName,ownerName);
    }
    public List<String> getMyPendingAppointmentsToApprove(String shopName,String ownerName) throws MarketException {
        Shop shop = shops.get(shopName);
        if (shop==null)
        {
            DebugLog.getInstance().Log("No such shop exist in the market.");
            throw new MarketException("No such shop exist in the market.");
        }
        EventLog.getInstance().Log(ownerName+" received his pending appointment.");
        return shop.getAllPendingForOwner(ownerName);
    }


    /**
     * check that all services are initialized from the config file.
     *
     * @throws MarketException
     */
    private void checkSystemInit() throws MarketException {
        int ans = 0;
        if (paymentServiceProxy == null) {
            EventLog eventLog = EventLog.getInstance();
            eventLog.Log("The market did not initialized properly. Missing payment service");

            ans = 1;
        }
        if (supplyServiceProxy == null) {
            EventLog eventLog = EventLog.getInstance();
            eventLog.Log("The market did not initialized properly. Missing supply service");

            ans = 2;

        }
        if (publisher == null | notificationHandler == null) {
            DebugLog eventLog = DebugLog.getInstance();
            eventLog.Log("The market did not initialized properly. Missing notifications service");

            ans = 3;

        }
        if (systemManagerName == null || systemManagerName.isEmpty()) {
            DebugLog eventLog = DebugLog.getInstance();
            eventLog.Log("The market did not initialized properly. Missing system manager");
            ans = 4;
        } else {
            EventLog eventLog = EventLog.getInstance();
            eventLog.Log("The market successfully initialized.");
        }
        if (ans > 0) {
            DebugLog eventLog = DebugLog.getInstance();
            eventLog.Log("The market did not initialized properly. Some of the services did not supplied.");
            throw new MarketException("The market did not initialized properly. Some of the services did not supplied.");
        }
    }

    //for debug purpose.
    public static void restartMarket() {
        instance = null;
    }

    private String getConfigDir() {
        String dir = System.getProperty("user.dir");
        if(!MarketConfig.IS_TEST_MODE){
            if(MarketConfig.IS_MAC){
                dir+="/server";
            }
            else{
                dir+="\\server";
            }
        }
        String additional_dir = "\\config\\";
        if (MarketConfig.IS_MAC) {
            additional_dir = "/config/";
        }
        dir += additional_dir;
        return dir;
    }

    private void alertIfNotLoggedIn(String visitorName) throws MarketException {
        if (!userController.isLoggedIn(visitorName)) {
            DebugLog debugLog = DebugLog.getInstance();
            debugLog.Log("you must be a visitor in the market in order to make actions");
            throw new MarketException("you must be a visitor in the market in order to make actions");
        }
    }

    public void addABid(String visitorName, String shopName, Integer itemId, Double price, Double amount) throws MarketException {
        alertIfNotLoggedIn(visitorName);
        Shop shop = shops.get(shopName);
        if (shop == null) {
            DebugLog.getInstance().Log("There is no shop named:" + shopName + ". Adding a new bid has failed.");
            throw new MarketException("There is no shop named:" + shopName + ". Adding a new bid has failed.");
        }
        Bid bid = shop.addABid(visitorName, itemId, price, amount);
        userController.getVisitor(visitorName).getCart().addABid(bid, shop);
    }

    public void approveABid(String approves, String shopName, String askedBy, Integer itemId) throws MarketException {
        alertIfNotLoggedIn(approves);
        Shop shop = shops.get(shopName);
        if (shop == null) {
            DebugLog.getInstance().Log("There is no shop named:" + shopName + ". Adding a new bid has failed.");
            throw new MarketException("There is no shop named:" + shopName + ". Adding a new bid has failed.");
        }
        boolean approved = shop.approveABid(approves, askedBy, itemId);
        if (approved) {
            ShoppingCart shoppingCart = userController.getVisitor(askedBy).getCart();
            shoppingCart.approveBid(itemId, shop);
        }

    }

    public void suggestNewOfferToBid(String suggester, String shopName, String askedBy, int itemId, double newPrice) throws MarketException {
        alertIfNotLoggedIn(suggester);
        Shop shop = shops.get(shopName);
        if (shop == null) {
            DebugLog.getInstance().Log("There is no shop named:" + shopName + ". Adding a new bid has failed.");
            throw new MarketException("There is no shop named:" + shopName + ". Adding a new bid has failed.");
        }
        shop.suggestNewOfferToBid(suggester, askedBy, itemId, newPrice);

    }

    public void rejectABid(String opposed, String shopName, String buyer, int itemId) throws MarketException {
        alertIfNotLoggedIn(opposed);
        Shop shop = shops.get(shopName);
        if (shop == null) {
            DebugLog.getInstance().Log("There is no shop named:" + shopName + ". Rejecting the bid failed.");
            throw new MarketException("There is no shop named:" + shopName + ". Rejecting the bid failed.");
        }
        shop.rejectABid(opposed, buyer, itemId);
        Member member = userController.getMember(buyer);
        ShoppingCart cart = member.getMyCart();
        ShoppingBasket basket = cart.getCart().get(shop);
        basket.removeBid(itemId);
    }

    public void cancelABid(String shopName, String buyer, int itemId) throws MarketException {
        alertIfNotLoggedIn(buyer);
        Shop shop = shops.get(shopName);
        if (shop == null) {
            DebugLog.getInstance().Log("There is no shop named:" + shopName + ". Cancelling the bid failed.");
            throw new MarketException("There is no shop named:" + shopName + ". Cancelling the bid failed.");
        }
        shop.cancelABid(buyer, itemId);
        Member member = userController.getMember(buyer);
        ShoppingCart cart = member.getMyCart();
        ShoppingBasket basket = cart.getCart().get(shop);
        basket.removeBid(itemId);
    }

    public void updateBidInLoggingOut(String visitorName) {
        for (Shop shop : shops.values()) {
            shop.updateBidInLoggingOut(visitorName);
        }
    }

    public boolean isSystemManager(String name) {
        return name.equals(this.systemManagerName);
    }

    public String resetSystemManager() {
        String ret= getSystemManagerName()+":"+Security.getInstance().getNamesToLoginInfo().get(getSystemManagerName()).getPassword();
        //todo ayala
        instance.systemManagerName="";
        return ret;
    }
    public String getSystemManager() {
        String ret= getSystemManagerName()+":"+Security.getInstance().getNamesToLoginInfo().get(getSystemManagerName()).getPassword();
        return ret;
    }
    public void restoreSystemManager(String uName, String password){
        systemManagerName=uName;
    }

    public List<Acquisition> getAcqsForMember(String memberName) throws MarketException {
        if (!userController.isMember(memberName)){
            DebugLog.getInstance().Log("There is no member with the name:"+memberName);
            throw new MarketException("There is no member with the name:"+memberName);
        }
        Member member = userController.getMember(memberName);
        return member.getAcquisitions();
    }

    public List<String> approveOrRejectBatch(String shopName, String ownerName, List<String> appointedNames, boolean approve) throws MarketException {
        Shop shop = shops.get(shopName);
        if (shop==null){
            DebugLog.getInstance().Log("Tried to access appointments of non existing shop.");
            throw new MarketException("Tried to access appointments of non existing shop.");
        }
        List<String> failed = new ArrayList<>();
        for (String name:appointedNames){
            if (approve){
                try {
                    shop.approveAppointment(name, ownerName);
                }
                catch (MarketException ex)
                {
                    failed.add(name);
                }
            }
            else {
                try {
                    shop.rejectAppointment(name, ownerName);
                }
                catch (MarketException ex)
                {
                    failed.add(name);
                }
            }
        }
        return failed;
    }
}

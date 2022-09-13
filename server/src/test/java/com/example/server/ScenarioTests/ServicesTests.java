package com.example.server.ScenarioTests;

import com.example.server.businessLayer.Market.Item;
import com.example.server.businessLayer.Market.Market;
import com.example.server.businessLayer.Market.ResourcesObjects.DataSourceConfigReader;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketConfig;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.businessLayer.Market.Users.UserController;
import com.example.server.businessLayer.Market.Users.Visitor;
import com.example.server.businessLayer.Payment.CreditCard;
import com.example.server.businessLayer.Payment.PaymentServiceProxy;
import com.example.server.businessLayer.Payment.WSEPPaymentServiceAdapter;
import com.example.server.businessLayer.Publisher.TextDispatcher;
import com.example.server.businessLayer.Supply.Address;
import com.example.server.businessLayer.Supply.SupplyServiceProxy;
import com.example.server.businessLayer.Supply.WSEPSupplyServiceAdapter;
import com.example.server.serviceLayer.Notifications.Notification;
import com.example.server.serviceLayer.Notifications.RealTimeNotifications;
import org.junit.jupiter.api.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ServicesTests {
    static PaymentServiceProxy paymentServiceProxy;
    static SupplyServiceProxy supplyServiceProxy;
    String userName = "u1";
    String password = "password";
    String systemManagerName = "u1";
    String systemManagerPassword = "password";
    String ItemName= "item1";
    Item itemAdded;
    int productAmount=20;
    Double productPrice=30.0;
    String shopOwnerName = "bar";
    String shopOwnerPassword = "password";
    String memberName = "bar1";
    String memberPassword = "password";
    String loggedInmemberName = "bar2";

    String loggedInmemberPassword = "password";
    String shopName = "store";
    TextDispatcher textDispatcher = TextDispatcher.getInstance();
    static CreditCard creditCard;
    static Address address;
    static Market market ;
    Visitor visitor;
    static boolean useData;
    static String ManName="u1";
    static String ManPass = "password";


    @BeforeAll
    public static void initBefore() throws MarketException {
        try {
            paymentServiceProxy = new PaymentServiceProxy(WSEPPaymentServiceAdapter.getinstance(), true);
            supplyServiceProxy = new SupplyServiceProxy(WSEPSupplyServiceAdapter.getInstance(), true);
            creditCard = new CreditCard("1234567890", "07", "2026", "205", "Bar Damri", "208915751");
            address = new Address("Bar Damri", "Atad 3", "Beer Shaba", "Israel", "8484403");
            market = Market.getInstance();
            useData = MarketConfig.USING_DATA;
            MarketConfig.USING_DATA = true;
            MarketConfig.IS_TEST_MODE = true;
            market.isInit();
            Visitor visitor = market.guestLogin();
            try {
                String[] dets = market.resetSystemManager().split(":");
                ManName = dets[0];
                ManPass = dets[1];
            } catch (Exception e) {
            }
            try {
                market.isInit();
            } catch (MarketException e) {
                System.out.println(e.getMessage());
            }
        }catch (Exception e){
          String str= e.getMessage();
        }
    }
    @AfterAll
    public static void setUseData(){

        MarketConfig.USING_DATA=useData;
        MarketConfig.IS_TEST_MODE=false;
        market.restoreSystemManager(ManName,ManPass);
    }
    @BeforeEach
    public void init() {

        try{
            market.memberLogin(systemManagerName, systemManagerPassword);
            market.validateSecurityQuestions(systemManagerName,new ArrayList<>(), visitor.getName());
        }catch (Exception e){}
    }


    @Test
    @DisplayName("Payment service- successful payment action")
    public void PaymentHandler() {
        try {
            int result = paymentServiceProxy.pay(creditCard);
            Assertions.assertNotEquals(result, -1);
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("Supply service- successful supply action")
    public void SupplyHandler() {
        try {
            int result = supplyServiceProxy.supply(address);
            Assertions.assertNotEquals(result, -1);
        } catch (Exception e) {
            assert false;
        }
    }


    @Test
    @DisplayName("Payment service- successful cancel pay action")
    public void PaymentHandlerCancel() {
        try {
            int result = paymentServiceProxy.pay(creditCard);
            if (result != -1) {
                result = paymentServiceProxy.cancelPay(result);
                Assertions.assertNotEquals(result, -1);
            } else {
                assert false;
            }
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("Supply service- successful cancel supply action")
    public void SupplyHandlerCancel() {
        try {
            int result = supplyServiceProxy.supply(address);
            if (result != -1) {
                result = supplyServiceProxy.cancelSupply(result);
                Assertions.assertNotEquals(result, -1);
            } else {
                assert false;
            }
        } catch (Exception e) {
            assert false;
        }
    }


    @Test
    @DisplayName("Payment service- check error message without crash when service falls")
    public void PaymentServiceFalls() throws MarketException {
        try {
            try {
                Visitor visitor = market.guestLogin();
                market.memberLogin(systemManagerName, systemManagerPassword);
                market.restoreSystemManager(systemManagerName, systemManagerPassword);
                market.validateSecurityQuestions(systemManagerName, new ArrayList<>(), visitor.getName());
            }catch (Exception e){
                String str= e.getMessage();
            }
            market.setPaymentServiceAddress("", systemManagerName);
            paymentServiceProxy.pay(creditCard);
            market.setPaymentServiceAddress(MarketConfig.WSEP_ADDRESS, systemManagerName);
            assert false;
        } catch (Exception e) {
            market.setPaymentServiceAddress(MarketConfig.WSEP_ADDRESS, systemManagerName);
            Assertions.assertEquals("Error2",e.getMessage());

        }
    }

    @Test
    @DisplayName("Supply service- check error message without crash when service falls")
    public void SupplyServiceFalls() throws MarketException {
        try {
            loginMember(systemManagerName,systemManagerPassword);
            market.setSupplyServiceAddress("", systemManagerName);
            supplyServiceProxy.supply(address);
            market.setSupplyServiceAddress(MarketConfig.WSEP_ADDRESS, market.getSystemManagerName());
            assert false;
         } catch (Exception e) {
            Assertions.assertEquals(e.getMessage(),"Error1");
            market.setSupplyServiceAddress(MarketConfig.WSEP_ADDRESS, market.getSystemManagerName());

        }
    }
    @Test
    @DisplayName("Dispatcher service- successful add action")
    public void textDispatcherAdd() {
        textDispatcher.clean();
        String name = "Bar";
        RealTimeNotifications not = new RealTimeNotifications();
        not.createMembershipDeniedMessage();
        Assertions.assertEquals(0, textDispatcher.getSessionNum());
        textDispatcher.add(name);
        Assertions.assertEquals(1, textDispatcher.getSessionNum());
    }

    @Test
    @DisplayName("Dispatcher service- try add twice a user should not allow.")
    public void textDispatcherAdd2() {
        textDispatcher.clean();
        String name = "Bar";
        RealTimeNotifications not = new RealTimeNotifications();
        not.createMembershipDeniedMessage();
        Assertions.assertEquals(0, textDispatcher.getSessionNum());
        textDispatcher.add(name);
        Assertions.assertEquals(1, textDispatcher.getSessionNum());
        try{
            textDispatcher.add(name);
            Assertions.assertEquals(1, textDispatcher.getSessionNum());
        }
        catch (Exception e){
            assert true;
        }
    }

    @Test
    @DisplayName("Dispatcher service- successful remove action")
    public void textDispatcherRemove(){
        textDispatcher.clean();
        String name = "Bar";
        RealTimeNotifications not = new RealTimeNotifications();
        not.createMembershipDeniedMessage();
        textDispatcher.add(name);
        Assertions.assertEquals(1, textDispatcher.getSessionNum());
        textDispatcher.remove(name);
        Assertions.assertEquals(0, textDispatcher.getSessionNum());
    }
    @Test
    @DisplayName("Dispatcher service- try to remove user without adding him before should not allow.")
    public void textDispatcherRemove2(){
        textDispatcher.clean();
        String name = "Bar";
       RealTimeNotifications not = new RealTimeNotifications();
        not.createMembershipDeniedMessage();
        Assertions.assertEquals(0, textDispatcher.getSessionNum());
        List<Notification> notifs=textDispatcher.remove(name);
        Assertions.assertEquals(0, notifs.size());
    }
    @Test
    @DisplayName("Dispatcher service- successful send new message action")
    public void textDispatcherAddMessage(){
        textDispatcher.clean();
        String name = "Bar";
        RealTimeNotifications not = new RealTimeNotifications();
        not.createMembershipDeniedMessage();
        textDispatcher.add(name);
        Assertions.assertTrue(textDispatcher.addMessgae(name,not));
        not.createShopPermissionDeniedMessage("some shop", "some permission");
        Assertions.assertTrue(textDispatcher.addMessgae(name,not));
    }

    @Test
    @DisplayName("System init from no existing file. should not continue the market init.")
    public void initFromNoFile(){
        try{
            MarketConfig.SERVICES_FILE_NAME="noName.txt";
            market.isInit();
            market.setPublishService(TextDispatcher.getInstance(), market.getSystemManagerName());
            market.memberLogout(systemManagerName);
            MarketConfig.SERVICES_FILE_NAME="config.txt";
            assert false;
        }
        catch(Exception e){
            MarketConfig.SERVICES_FILE_NAME="config.txt";
            assert true;
        }
    }
    @Test
    @DisplayName("System init from bas config file, no supply service. should not continue the market init.")
    public void initFromBadSupplyFile(){
        String name= MarketConfig.SERVICES_FILE_NAME;
        try{
            MarketConfig.SERVICES_FILE_NAME="badSupplyConfig.txt";
            market.isInit();
            market.setPublishService(TextDispatcher.getInstance(), market.getSystemManagerName());
            market.memberLogout(systemManagerName);
            MarketConfig.SERVICES_FILE_NAME=name;
            assert false;
        }
        catch(Exception e){
            MarketConfig.SERVICES_FILE_NAME=name;
            assert true;
        }
    }
    @Test
    @DisplayName("System init from bas config file, no supply service. should not continue the market init.")
    public void initFromBadPaymentFile(){
        String name= MarketConfig.SERVICES_FILE_NAME;
        try{
            MarketConfig.SERVICES_FILE_NAME="badPaymentConfig.txt";
            market.isInit();
            market.setPublishService(TextDispatcher.getInstance(), market.getSystemManagerName());
            market.memberLogout(systemManagerName);
            MarketConfig.SERVICES_FILE_NAME=name;
            assert false;
        }
        catch(Exception e){
            MarketConfig.SERVICES_FILE_NAME=name;
            Assertions.assertEquals("Missing init values for PaymentService . Could not init the system services.",e.getMessage());
        }
    }
    @Test
    @DisplayName("System init from bas config file, no supply service. should not continue the market init.")
    public void initFromBadPublisherFile(){
        String name= MarketConfig.SERVICES_FILE_NAME;
        try{
            MarketConfig.SERVICES_FILE_NAME="badPublisherConfig.txt";
            market.isInit();
            market.setPublishService(TextDispatcher.getInstance(), market.getSystemManagerName());
            market.memberLogout(systemManagerName);
            MarketConfig.SERVICES_FILE_NAME=name;
            assert false;
        }
        catch(Exception e){
            MarketConfig.SERVICES_FILE_NAME=name;
            Assertions.assertEquals("Missing init values for Publisher . Could not init the system services.",e.getMessage());
        }
    }
    @Test
    @DisplayName("System init from no existing data source file. should not continue the market init.")
    public void initFromDataSourceNoFile() {
        String name=MarketConfig.DATA_SOURCE_FILE_NAME;
        try{
            MarketConfig.USING_DATA=true;
            MarketConfig.DATA_SOURCE_FILE_NAME ="noName.txt";
            DataSourceConfigReader.resetInstance();
            market.isInit();
            MarketConfig.DATA_SOURCE_FILE_NAME=name;
            assert false;
        }
        catch(Exception e){
            MarketConfig.DATA_SOURCE_FILE_NAME=name;
            Assertions.assertEquals("Data source config file not found.",e.getMessage());
        }
    }
    @Test
    @DisplayName("System init from bad config file without supply service. should not continue the market init.")
    public void initFromBadConfig() {
        String name=MarketConfig.SERVICES_FILE_NAME;
        try{

            MarketConfig.SERVICES_FILE_NAME ="BadConfig.txt";
            market.isInit();
            MarketConfig.SERVICES_FILE_NAME=name;
            assert false;
        }
        catch(Exception e){
            MarketConfig.SERVICES_FILE_NAME=name;
            Assertions.assertEquals("Missing init values for SupplyService . Could not init the system services.",e.getMessage());
        }
    }

    @Test
    @DisplayName("System init from data source file with no username. should not continue the market init.")
    public void initFromDataSourceBadArgs() {
        String name=MarketConfig.DATA_SOURCE_FILE_NAME;
        try{

            MarketConfig.DATA_SOURCE_FILE_NAME =MarketConfig.MISS_NAME_DATA_SOURCE_FILE_NAME;
            DataSourceConfigReader.resetInstance();
            market.isInit();
            MarketConfig.DATA_SOURCE_FILE_NAME=name;
            assert false;
        }
        catch(Exception e){
            MarketConfig.DATA_SOURCE_FILE_NAME=name;
            Assertions.assertEquals("Missing username in data source config file.",e.getMessage());
        }
    }
    @Test
    @DisplayName("System init from data source file with no password. should not continue the market init.")
    public void initFromDataSourceBadArgsPass() {
        String name=MarketConfig.DATA_SOURCE_FILE_NAME;
        try{

            MarketConfig.DATA_SOURCE_FILE_NAME =MarketConfig.MISS_PASSWORD_DATA_SOURCE_FILE_NAME;
            DataSourceConfigReader.resetInstance();
            market.isInit();
            MarketConfig.DATA_SOURCE_FILE_NAME=name;
            assert false;
        }
        catch(Exception e){
            MarketConfig.DATA_SOURCE_FILE_NAME=name;
            Assertions.assertEquals("Missing password in data source config file.",e.getMessage());
        }
    }
   @Test
   @Order(17)
    @DisplayName("Notification test- successful close shop action")
    public void closeShop() {
       try {
           // shop manager register
           registerVisitor("notificationTestUser", shopOwnerPassword);
           loginMember("notificationTestUser", shopOwnerPassword);
           market.openNewShop("notificationTestUser", "notificationShop");
           itemAdded = market.addItemToShopItem("notificationTestUser", ItemName, productPrice, Item.Category.electricity, "", new ArrayList<>(), productAmount, "notificationShop");
           market.closeShop("notificationTestUser", "notificationShop");
           logoutMember("notificationTestUser");
           assert true;
       } catch (Exception e) {
           assert false;
       }
       try {
           logoutMember("notificationTestUser");
       } catch (MarketException ex) {
           System.out.println(ex.getMessage());
       }
   }


    @Test
    @DisplayName("Notification test- appoint owner with delayed notification, check message exists.")
    public void AppointOwnerNotificationTest() {
        try {

            String appointedName = "appointedNameTest1";
            List<String> nots= new ArrayList<>();
            RealTimeNotifications not= new RealTimeNotifications();
            setUpAppointOwner(appointedName,not);
            nots.addAll(readDelayedMessages(appointedName));
            boolean found = false;
            for(String message : nots){
                if(message.contains(not.getMessage().split("\n")[0])){
                    found=true;
                }
            }
            Assertions.assertTrue(found);
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("Notification test- close shop with delayed notification, check message exists.")
    public void closeShopDelayed() {
        try {

            String appointedName = "appointedNameTest2";
            String testShopName = "ShopName2";
            String owner = "ownerNameTest2";
            List<String> nots= new ArrayList<>();
            RealTimeNotifications not= new RealTimeNotifications();
            setUpCloseShop(owner,appointedName,not,testShopName);
            nots.addAll(readDelayedMessages(appointedName));
            boolean found = false;
            for(String message : nots){
                if(message.contains(not.getMessage().split("\n")[0])){
                    found=true;
                }
            }
            Assertions.assertTrue(found);
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("Notification test- close shop with real time notification, check message exists.")
    public void closeShopRealTime() {
        try {

            String appointedName = "appointedNameTest3";
            String testShopName = "ShopName3";
            String owner = "ownerNameTest3";
            List<String> nots= new ArrayList<>();
            RealTimeNotifications not= new RealTimeNotifications();
            setUpCloseShop(owner,appointedName,not,testShopName);
            nots.addAll(readRealTimeMessages(owner));
            boolean found = false;
            for(String message : nots){
                if(message.equals(not.getMessage().split("\n")[0])){
                    found=true;
                }
            }
            Assertions.assertTrue(found);
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("Notification test- close shop with real time notification, check message exists.")
    public void shopManagerStatistics() throws MarketException {

        try {
            String appointedName = "appointedNameTest4";
            String testShopName = "ShopName4";
            String owner = "ownerNameTest4";
            loginMember(systemManagerName,systemManagerPassword );
            List<String> nots= new ArrayList<>();
            RealTimeNotifications not= new RealTimeNotifications();
            setUpCloseShop(owner,appointedName,not,testShopName);
            nots.addAll(readRealTimeMessages(systemManagerName));
            boolean found = false;
            for(String message : nots){
                if(message.contains("numOfVisitors")){
                    found=true;
                    break;
                }
            }
            logoutMember(systemManagerName);
            Assertions.assertTrue(found);
        } catch (Exception e) {
            logoutMember(systemManagerName);
            assert false;
        }
    }
    @Test
    @DisplayName("Notification test- close shop with real time notification, not system manager")
    public void shopManagerStatisticsNoManager() {
        try {

            String appointedName = "appointedNameTest5";
            String testShopName = "ShopName5";
            String owner = "ownerNameTest5";
            List<String> prevNots= new ArrayList<>();
            prevNots.addAll(readRealTimeMessages(market.getSystemManagerName()));
            market.memberLogout(market.getSystemManagerName());
            List<String> nots= new ArrayList<>();
            RealTimeNotifications not= new RealTimeNotifications();
            setUpCloseShop(owner,appointedName,not,testShopName);
            nots.addAll(readRealTimeMessages(market.getSystemManagerName()));
            loginMember(systemManagerName, systemManagerPassword);
            boolean found ;
            found= (nots.size()==prevNots.size());
            Assertions.assertTrue(found);
        } catch (Exception e) {
            assert true;
        }
    }

    public void setUpCloseShop(String owner,String appointedName, RealTimeNotifications not,String testShopName) throws MarketException {

        not.createShopClosedMessage(testShopName);
        // shop manager register
        registerVisitor(owner, shopOwnerPassword);
        registerVisitor(appointedName, shopOwnerPassword);
        loginMember(owner, shopOwnerPassword);
        market.openNewShop(owner, testShopName);
        market.appointShopOwner(owner,appointedName,testShopName);
        market.closeShop(owner,testShopName);
        market.memberLogout(owner);
    }


    public void setUpAppointOwner(String appointedName, RealTimeNotifications not) throws MarketException {
        String owner="notificationTestUser3";
        String testShopName="testShopName3";
        not.createAppointmentApprovedMessage(owner,testShopName);
        registerVisitor(owner, shopOwnerPassword);
        registerVisitor(appointedName, shopOwnerPassword);
        loginMember(owner, shopOwnerPassword);
        market.openNewShop(owner, testShopName);
        market.appointShopOwner(owner,appointedName,testShopName);
        market.memberLogout(owner);
    }
    public void loginMember(String name, String password) throws MarketException {
        if(UserController.getInstance().isLoggedIn(name))
            return;
        visitor = market.guestLogin();
        market.memberLogin(name, password);
        market.validateSecurityQuestions(name, new ArrayList<>(), visitor.getName());
    }
    public void logoutMember(String name) throws MarketException {
        market.memberLogout(name);
    }
    public void registerVisitor(String name, String pass) throws MarketException {
        // shop manager register
        Visitor visitor = market.guestLogin();
        market.register(name, pass);
    }

    private List<String> readDelayedMessages(String name) {

        try {
            List<String> nots= new ArrayList<>();
            File myObj = new File(getConfigDir() + name+".txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if(data.isEmpty())
                    continue;
                nots.add(data);
            }
            return nots;

        } catch (Exception e) {
            return new ArrayList<>();
        }

    }
    private List<String> readRealTimeMessages(String name) {

        try {
            List<String> nots= new ArrayList<>();
            File myObj = new File(getConfigRealTimeDir() + name+".txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if(data.isEmpty())
                    continue;
                nots.add(data);
            }
            return nots;

        } catch (Exception e) {
            return new ArrayList<>();
        }

    }
    private String getConfigDir() {
        String dir = System.getProperty("user.dir");
        dir += "/notifications/Delayed/";
        return dir;
    }
    private String getConfigRealTimeDir() {
        String dir = System.getProperty("user.dir");
        dir += "/notifications/Real_Time/";
        return dir;
    }
}

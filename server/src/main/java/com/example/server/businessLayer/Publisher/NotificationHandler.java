package com.example.server.businessLayer.Publisher;

import com.example.server.businessLayer.Market.ResourcesObjects.MarketConfig;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.businessLayer.Market.Statistics;
import com.example.server.businessLayer.Market.Users.Member;
import com.example.server.businessLayer.Market.Users.UserController;
import com.example.server.serviceLayer.Notifications.DelayedNotifications;
import com.example.server.serviceLayer.Notifications.Notification;
import com.example.server.serviceLayer.Notifications.RealTimeNotifications;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class NotificationHandler {

    private static NotificationHandler instance=null;
    //holds notifications to send to each domain(by the member name)
    private Publisher dispatcher;

    //Map for sessionId-name pairs.
    private static Map<String, String> sessions;


    public static NotificationHandler getInstance(){
        if(instance==null){
            instance=new NotificationHandler(NotificationDispatcher.getInstance());
        }
        return instance;
    }
    private NotificationHandler(Publisher dispatcher) {
        this.dispatcher = dispatcher;
        sessions = new ConcurrentHashMap<>();
    }

    //Sends message to user who just logged in.
    public synchronized boolean sendMessageToLogged(String name, Notification notification){
        if(!sessions.containsKey(name)){
            return false;
        }
        String sessionId=sessions.get(name);
        return dispatcher.addMessgae(sessionId,notification);
    }

    /**
     * Add new session.
     * @param name name of the new visitor that logged in.
     * @param sessionId the sessionId of the connection.
     * @return
     */
    public synchronized boolean add(String name, String sessionId) {


        sessions.put(name,sessionId);
        if(dispatcher.add(sessionId)){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Callback method for user logged in.
     * @param name name of the new visitor that logged in.
     * @return
     */
    private synchronized boolean sendAllDelayedNotifications(List<Notification> nots, String name) {


        for(Notification not : nots){
            if(! sendMessageToLogged(name, not)){
                return false;
            }
        }

        deleteDelayed(name);
        return true;
    }

    private synchronized void deleteDelayed(String name) {
        try {
            File parentDir = new File(getConfigDir() );
            if(!parentDir.exists()) {
                parentDir.mkdir();
            }
            File file = new File(parentDir, name + ".txt");
            if(file.delete()){}
            else{
                throw new MarketException("could not delete file");
            }

        }
        catch (Exception e){}

    }

    /**
     * Remove a session in case of logout.
     * @param name the name of the visitor to remove.
     * @param sessionId the sessionId of the connection
     * @return
     */
    public synchronized boolean remove(String name, String sessionId) {

        List<Notification> nots= dispatcher.remove(sessionId);
        if(!nots.isEmpty()){
            for(Notification not : nots) {
                writeToText(not.getMessage(),name);
            }
        }
        sessions.remove(name);
        return true;
    }

    /**
     * Method in case visitor suddenly disconnected.
     * @param sessionId the sessionId to send to.
     * @return
     */
    public synchronized boolean removeErr(String sessionId) {
        String name="";
        for(Map.Entry<String ,String> set : sessions.entrySet()){
            if(set.getValue().equals(sessionId)){
                name=set.getKey();
            }
        }
        if(name.isEmpty()){
            return false;
        }
        List<Notification> nots= dispatcher.remove(sessionId);
        if(!nots.isEmpty()){
            for(Notification not : nots) {
                writeToText(not.getMessage(),name);
            }
        }
        sessions.remove(name);
        return true;
    }

    /**
     * The main method for sending a notification for a user.
     *
     * @param name         the name of the visitor to send the message
     * @param notification the notification needed to be sent.
     * @param isMember     bool field that say if the visitor is a member.
     * @return
     */
    public synchronized boolean sendNotification(String name , Notification notification, boolean isMember) {

        UserController userController = UserController.getInstance();
        String session;
        if ((MarketConfig.IS_TEST_MODE && userController.isLoggedIn(name))) {
            //if user logged in test.
            session = name;
            dispatcher.addMessgae(session, notification);
            return true;
        } else if ((MarketConfig.IS_TEST_MODE && !userController.isLoggedIn(name)) && isMember) {

            writeToText(notification.getMessage(), name);
        } else if (sessions.containsKey(name)) {
            //if user logged.
            session = sessions.get(name);
            dispatcher.addMessgae(session, notification);
            return true;
        } else {
            //if not logged in. save if member
            if (isMember) {
                //in case of a test, save the notification as a delayed message header.

                writeToText(notification.getMessage(), name);

            }
        }
        return true;
    }
    public boolean isConnected(String name){
        UserController userController = UserController.getInstance();
        String session;
        if ((MarketConfig.IS_TEST_MODE && userController.isLoggedIn(name))) {
            return true;
        } else if ((MarketConfig.IS_TEST_MODE && !userController.isLoggedIn(name))) {
            return false;
        } else if (sessions.containsKey(name)) {
            return true;
        } else {
            //if not logged in. save if member
            return true;
        }
    }

    /**
     * Sends to all owner that item is bought from shop.
     * @param buyer the man buyed the items.
     * @param names the names of the owners to send to.
     * @param shopName the shop name
     * @param itemsNames the baught items list.
     * @param prices the bought items prices.
     */
    public void sendItemBaughtNotificationsBatch(String buyer, ArrayList<String> names, String shopName, ArrayList<String> itemsNames, ArrayList<Double> prices ) {

        RealTimeNotifications not;
        for (String name : names) {
            for (int i = 0; i < itemsNames.size(); i++) {
                not = new RealTimeNotifications();
                not.createBuyingOfferMessage(buyer, shopName, itemsNames.get(i), prices.get(i));
                sendNotification(name,not,true);
            }
        }
    }

    public void sendReOpenedShopBatch(List<String> works, String founder, String shopName) {
        RealTimeNotifications not=new RealTimeNotifications();
        not.createReOpenedShopMessage(shopName,founder);
        for(String worker: works){
            sendNotification(worker,not,true);
        }
    }

    public void sendShopClosedBatchNotificationsBatch(ArrayList<String> strings, String shopName) {
        RealTimeNotifications not=new RealTimeNotifications();
        not.createShopClosedMessage(shopName);
        for(String name: strings){
            sendNotification(name,not,true);
        }
    }

    public void sendNewOfferOfBidNotification(String buyer, boolean isMember, double newPrice, String itemName, String shopName) {
        RealTimeNotifications not= new RealTimeNotifications();
        not.createNewOfferOfBidMessage(buyer, itemName, newPrice, shopName);
        sendNotification(buyer, not, isMember);
    }

    public void sendBidApprovedNotification(String buyer, boolean isMember, double price, String itemName, String shopName) {
        RealTimeNotifications not= new RealTimeNotifications();
        not.createBidApprovedMessage(buyer, itemName, price, shopName);
        sendNotification(buyer, not, isMember);
    }

    public void sendBidRejectedNotification(String buyer, boolean isMember, double price, String itemName, String shopName) {
        RealTimeNotifications not= new RealTimeNotifications();
        not.createBidRejectedMessage(buyer, itemName, price, shopName);
        sendNotification(buyer, not, isMember);
    }
    public void sendAppointmentRejectedNotification(String appointedName, String ownerName, String shopName) {
        RealTimeNotifications not= new RealTimeNotifications();
        not.createAppointmentRejectedMessage(appointedName, ownerName, shopName);
        sendNotification(appointedName, not, true);
    }

    public void sendAppointmentApproved(String appointedName, String ownerName, String shopName) {
        RealTimeNotifications not= new RealTimeNotifications();
        not.createAppointmentApprovedMessage(ownerName, shopName);
        sendNotification(appointedName, not, true);
    }
    public void sendNewAppointmentBatch(List<String> owners, Member appointed, Member superVisor, String shopName, String role) {
        RealTimeNotifications not= new RealTimeNotifications();
        not.createNewAppointmentMessage(appointed.getName(), superVisor.getName(), shopName,role);
        for(String employee : owners){
            sendNotification(employee,not,true);
        }
    }
    public void sendBidRejectedToApprovesNotificationBatch(List<String> approves, String buyer, double price, String itemName, String shopName) {
        RealTimeNotifications not= new RealTimeNotifications();
        not.createBidRejectedToApprovesMessage(buyer, price, itemName, shopName);
        for(String employee : approves){
            sendNotification(employee,not,true);
        }
    }

    public void sendNewOfferOfBidToApprovalOfApprovesNotificationBatch(List<String> approves, String buyer, double newPrice, String itemName, String shopName) {
        RealTimeNotifications not= new RealTimeNotifications();
        not.createNewOfferOfBidToApprovalOfApprovesMessage(buyer, newPrice, itemName, shopName);
        for(String employee : approves){
            sendNotification(employee,not,true);
        }
    }

    public void sendNewBidToApprovalOfApprovesNotificationBatch(List<String> approves, String buyer, double newPrice, String itemName, String shopName) {
        RealTimeNotifications not= new RealTimeNotifications();
        not.createNewBidToApprovalOfApprovesMessage(buyer, newPrice, itemName, shopName);
        for(String employee : approves){
            sendNotification(employee,not,true);
        }
    }
    public void sendBidCanceledToApprovesNotificationBatch(List<String> approves, String buyer, double price, String itemName, String shopName) {
        RealTimeNotifications not= new RealTimeNotifications();
        not.createNewBidCanceledToApprovesMessage(buyer, price, itemName, shopName);
        for(String employee : approves){
            sendNotification(employee,not,true);
        }
    }

    public void sendAppointmentRemovedNotification(String firedAppointed, String shopName) {
        RealTimeNotifications not= new RealTimeNotifications();
        not.createShopPermissionDeniedMessage(shopName,firedAppointed);
        sendNotification(firedAppointed,not,true);
    }

    public void setService(Publisher o) {
        dispatcher=o;
    }


    public void login(String name, String visitor, String sessionId) {

        List<Notification> nots = readDelayedMessages(name);
        sessions.remove(visitor);
        sessions.put(name,sessionId);
        sendAllDelayedNotifications(nots,name);
    }

    private boolean writeToText(String message, String name){
        try {
            File parentDir = new File(getConfigDir());
            if(!parentDir.exists()) {
                parentDir.mkdir();
            }
            final File file = new File(parentDir, name+".txt");
            file.createNewFile(); // Creates file crawl_html/abc.txt
            FileWriter myWriter = new FileWriter(file,true);
            myWriter.write(message);
            myWriter.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }
    private List<Notification> readDelayedMessages(String name) {

        try {
            List<Notification> nots= new ArrayList<>();
            File parentDir = new File(getConfigDir());
            File myObj = new File(parentDir + name+".txt");
            if(!parentDir.exists()) {
                parentDir.mkdir();
            }
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                if(data.isEmpty())
                    continue;
                DelayedNotifications del= new DelayedNotifications();
                del.createMessage(data);
                nots.add(del);
            }
            return nots;

        } catch (Exception e) {
            return new ArrayList<>();
        }

    }


    private String getConfigDir() {
        String dir = System.getProperty("user.dir");
        String additional_dir="\\notifications\\Delayed\\";
        if(MarketConfig.IS_MAC){
            additional_dir="/notifications/Delayed/";
        }
        dir += additional_dir;
        return dir;
    }

    public void sendNewShopManager(Member shopOwner, Member appointed, String shopName) {
        RealTimeNotifications not= new RealTimeNotifications();
        not.createNewManagerMessage(shopOwner.getName(),appointed.getName(),shopName);
        sendNotification(appointed.getName(),not,true);
    }

    public synchronized void sendStatistics(Statistics statistics, String systemManager) {
        RealTimeNotifications not = new RealTimeNotifications();
        not.createAnotherMessage(statistics.toString());
        UserController userController = UserController.getInstance();
        String session;
        if ((MarketConfig.IS_TEST_MODE && userController.isLoggedIn(systemManager))) {
            //if user logged in test.
            session = systemManager;
            dispatcher.addMessgae(session, not);
        } else if (sessions.containsKey(systemManager)) {
            //if user logged.
            session = sessions.get(systemManager);
            dispatcher.addMessgae(session, not);
        }
    }



}
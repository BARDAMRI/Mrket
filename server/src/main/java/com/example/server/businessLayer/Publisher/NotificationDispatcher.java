package com.example.server.businessLayer.Publisher;

import com.example.server.businessLayer.Market.ResourcesObjects.MarketConfig;
import com.example.server.serviceLayer.Notifications.Notification;
import com.example.server.serviceLayer.Notifications.RealTimeNotifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Service
@EnableScheduling
public class NotificationDispatcher extends Publisher {
    @Autowired
    private SimpMessagingTemplate template;
    private static NotificationDispatcher notificationDispatcher=null;

    //Set of the sessionis-messages;
    private static Map<String, List<Notification>> messages;

    public static NotificationDispatcher getInstance(){
        if (notificationDispatcher==null){
            notificationDispatcher=new NotificationDispatcher();
        }
        return notificationDispatcher;
    }
    private NotificationDispatcher() {
        messages=new ConcurrentHashMap<>();
    }

    //For each listener, send all real time notifications available.
    @Scheduled(fixedDelay = MarketConfig.SCHEDULE_MILSEC)
    public void dispatch() {
        for (Map.Entry<String, List<Notification>> messagesSet : messages.entrySet()) {
            try {
                for (Notification mess : messagesSet.getValue()) {

                    String sessionId = messagesSet.getKey();
                    SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
                    headerAccessor.setSessionId(sessionId);
                    headerAccessor.setLeaveMutable(true);
                    template.convertAndSendToUser(
                            sessionId,
                            "/notification/item",
                            mess.getMessage(),
                            headerAccessor.getMessageHeaders()) ;
                }

                //clear the notifications sent.
                messages.get(messagesSet.getKey()).clear();
            }
            catch (Exception e){}

        }
    }

    //removes a session by session.
    @Override
    public List<Notification> remove(String sessionId) {

            if (!messages.containsKey(sessionId)) {
                return new ArrayList<>();
            }
            List<Notification> nots = messages.get(sessionId);
            messages.remove(sessionId);
            return nots;

    }


    //Add new session to the map.
    @Override
    public boolean add( String sessionId) {

            if (messages.containsKey(sessionId)) {
                return false;
            }
            messages.put(sessionId, new ArrayList<>());
            RealTimeNotifications not = new RealTimeNotifications();
            not.createAnotherMessage("welcome to notifications service");
            messages.get(sessionId).add(not);
            return true;

    }


    //sends message to user who just logged in.
    @Override
    public boolean addMessgae(String sessionId, Notification notification) {

            if (!messages.containsKey(sessionId)) {
                return false;
            }
            messages.get(sessionId).add(notification);
            return true;
    }
}

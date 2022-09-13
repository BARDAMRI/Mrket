package com.example.server.serviceLayer.Notifications;

public class DelayedNotifications extends Notification{


    public DelayedNotifications(){
        super();
    }

    public DelayedNotifications(String mess){
        super(mess);
    }
    public void createOfferAcceptedMessage( String shopName ,String product, int price){
        message= "Your offer to buy "+product+" from "+ shopName+ " for the amount of "+price+ " accepted!"+"\n";
    }
    public void createMessage(String mess){
        message=mess+"\n";
    }
    public void createOfferDeclinedMessage( String shopName ,String product, int price){
        message= "Your offer to buy "+product+" from "+ shopName+ " for the amount of "+price+ " declined!"+"\n";
    }

}

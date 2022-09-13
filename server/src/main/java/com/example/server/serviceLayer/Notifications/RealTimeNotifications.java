package com.example.server.serviceLayer.Notifications;

public class RealTimeNotifications extends Notification{

    public RealTimeNotifications(){
        super();
    }

    public void createShopClosedMessage(String shopName){
        message= "The shop" + shopName+ " was closed"+"\n";
    }

    public void createShopClosedPermanentlyMessage(String shopName){
        message= "The shop" + shopName+ " was closed permanently by the market manager"+"\n";
    }

    public void createShopOpenedMessage(String shopName){
        message= "The shop" + shopName+ " was reopened"+"\n";
    }

    public void createBuyingOfferMessage(String offer , String shopName ,String product, double price){
        message= offer+ " submitted a buying offer in the shop "+ shopName+", to buy "+ product+ " for the amount of "+price+"\n";
    }
    public void createMembershipDeniedMessage(){
        message= "Unfortunately, your membership has been denied."+"\n";
    }
    public void createShopPermissionDeniedMessage(String shop,String permission){
        message= "Unfortunately, your appointment as "+ permission+" in shop "+shop +" has been canceled."+"\n";
    }
    public void createNewMessageMessage(){
        message= "You have a new message in your inbox."+"\n";
    }
    public void createAnotherMessage(String mess){
        message= mess+"\n";
    }


    public void createNewManagerMessage(String shopOwner, String appointed,String shop) {
        message= "you got appointed as manager to shop "+shop+ " by "+shopOwner+"\n";
    }

    public void createNewOwnerMessage(String shopOwner, String appointed, String shopName) {
        message= "you got appointed as owner to shop "+shopName+ " by "+shopOwner+"\n";
    }

    public void createNewOfferOfBidMessage(String buyer, String itemName, double newPrice, String shopName) {
        message = String.format ("Hello %s,\n Your bid for %s in shop %s has received a counter-bid on the price %f. ", buyer, itemName, shopName, newPrice);
    }

    public void createBidApprovedMessage(String buyer, String itemName, double price, String shopName) {
        message = String.format ("Hello %s,\n Your bid for %s in shop %s at a price of %f has been approved. ", buyer, itemName, shopName, price);
    }

    public void createBidRejectedMessage(String buyer, String itemName, double price, String shopName) {
        message = String.format ("Hello %s,\n Your bid for %s in shop %s at a price of %f has been rejected. ", buyer, itemName, shopName, price);
    }

    public void createBidRejectedToApprovesMessage(String buyer, double price, String itemName, String shopName) {
        message = String.format ("Hello,\n The bid for %s in shop %s at a price of %f from %s has been rejected. ", itemName, shopName, price, buyer);
    }

    public void createNewOfferOfBidToApprovalOfApprovesMessage(String buyer, double newPrice, String itemName, String shopName) {
        message = String.format ("Hello,\n The bid for %s in shop %s from %s got a counter-bid at a price of %f. ", itemName, shopName, buyer, newPrice);
    }

    public void createNewBidToApprovalOfApprovesMessage(String buyer, double newPrice, String itemName, String shopName) {
        message = String.format ("Hello,\n A new bid for %s in shop %s at a price of %f from %s has been created.", itemName, shopName, newPrice, buyer);
    }

    public void createNewBidCanceledToApprovesMessage(String buyer, double price, String itemName, String shopName) {
        message = String.format ("Hello,\n The bid for %s in shop %s from %s at a price of %f has been cancelled by the buyer.", itemName, shopName, buyer, price);
    }

    public void createUserLoggedIn(String name, int size) {
        message= String.format("Visitor %s entered the market.\nCurrently, there are:: %d visitors in the market.",name,size);
    }
    public void createUserLoggedout(String name, int size) {
        message= String.format("Visitor %s leaved the market.\nCurrently, there are:: %d visitors in the market.",name,size);
    }
    public void createMemberLoggedOut(String memberName,String visitorName) {
        message= String.format("Member %s logged out and and now identify as user %s.",memberName, visitorName);
    }

    public void createAppointmentRejectedMessage(String appointedName, String ownerName, String shopName) {
        message=String.format("Unfortunately, your appointment by %s to the shop %s has been rejected.",appointedName, ownerName,shopName);
    }

    public void createAppointmentApprovedMessage(String ownerName, String shopName) {
        message=String.format("Your appointment by %s to the shop %s has been approved.", ownerName,shopName);
    }

    public void createNewAppointmentMessage(String appointed, String owner, String shopName, String role) {
        message=String.format("There was a new appointment in shop %s, %s appointed %s for %s",shopName,owner,appointed,role);
    }

    public void createReOpenedShopMessage(String shopName, String founder) {
        message=String.format("The shop %s, was re-opened by its founder %s",shopName,founder);
    }
}

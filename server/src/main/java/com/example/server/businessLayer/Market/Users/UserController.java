package com.example.server.businessLayer.Market.Users;

import com.example.server.businessLayer.Market.Market;
import com.example.server.businessLayer.Market.ResourcesObjects.DebugLog;
import com.example.server.businessLayer.Market.ResourcesObjects.EventLog;
import com.example.server.businessLayer.Market.ResourcesObjects.SynchronizedCounter;
import com.example.server.businessLayer.Market.Item;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.businessLayer.Market.Shop;
import com.example.server.businessLayer.Market.ShoppingCart;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserController {
    private Map<String, Member> members;
    private Map<String, Visitor> visitorsInMarket;
    //TODO synchronized next
    private SynchronizedCounter nextUniqueNumber;
    private static UserController instance;
    private long registeredMembersAvg;
    private LocalDate openingDate;



    public synchronized static UserController getInstance() {
        if (instance == null)
            instance = new UserController();
        return instance;
    }

    private UserController() {
        members = new ConcurrentHashMap<>();
        visitorsInMarket = new ConcurrentHashMap<>();
        nextUniqueNumber = new SynchronizedCounter();
        this.registeredMembersAvg = 0;
        this.openingDate = LocalDate.now();
    }

    /**
     *
     * @return a new visitor with a unique name and null member
     * getting unique name is the only sync code in this method
     */
    public Visitor guestLogin() {
        String name = getNextUniqueName();
        Visitor res = new Visitor(name,null,new ShoppingCart());
        this.visitorsInMarket.put(res.getName(),res);
        EventLog.getInstance().Log("A new visitor entered the market.");
        return res;
    }
    public Member memberLogin(String userName, String userPassword){
        return null;
    }


    public void exitSystem(String visitorName) throws MarketException {
        if (this.visitorsInMarket.containsKey(visitorName)) {
            this.visitorsInMarket.remove(visitorName);
            Market.getInstance ().updateBidInLoggingOut(visitorName);
            EventLog.getInstance().Log("User left the market.");
        }
        else
        {
            DebugLog.getInstance().Log("Non visitor tried to leave - The only way to be out is to be in.");
            throw new MarketException(String.format("%s tried to exit system but never entered", visitorName));
        }
    }

    public boolean register(String userName) throws MarketException {
        members.put(userName,new Member(userName));
        EventLog.getInstance().Log("Welcome to our new member: "+userName);
        return true;
    }


    private synchronized String getNextUniqueName() {
        String name = "@visitor" + nextUniqueNumber.increment();
        return name;
    }

    public synchronized Map<String, Member> getMembers() {
        return members;
    }

    public void addMemberToMarket() {
        throw new UnsupportedOperationException();
    }

    public Visitor getVisitor(String visitorName) throws MarketException {
        Visitor visitor = this.visitorsInMarket.get(visitorName);
        if (visitor == null)
            throw new MarketException("no such visitor in the market");
        return visitor;
    }
    public Map<String, Visitor> getVisitorsInMarket() {
        return visitorsInMarket;
    }

    public void setVisitorsInMarket(Map<String, Visitor> visitorsInMarket) {
        this.visitorsInMarket = visitorsInMarket;
    }

    public String memberLogout(String member) throws MarketException {
        if (!members.containsKey(member)) {
            DebugLog.getInstance().Log("Non member tried to logout");
            throw new MarketException("no such member");
        }
        if (!visitorsInMarket.containsKey(member)) {
            DebugLog.getInstance().Log("member who is not visiting tried to logout");
            throw new MarketException("not currently visiting the shop");
        }
        visitorsInMarket.remove(member);
        String newVisitorName = getNextUniqueName();
        Visitor newVisitor = new Visitor(newVisitorName);
        visitorsInMarket.put(newVisitorName, newVisitor);
        EventLog.getInstance().Log("Our beloved member " + member + " logged out.");
        return newVisitorName;
    }
    public synchronized Member finishLogin(String userName, String visitorName) throws MarketException {
        Visitor newVisitorMember = new Visitor(userName,members.get(userName),members.get(userName).getMyCart());
        if(visitorsInMarket.containsKey(userName))
            throw new MarketException("member is already logged in");
        visitorsInMarket.put(userName,newVisitorMember);
        visitorsInMarket.remove ( visitorName );

        EventLog.getInstance().Log(userName+" logged in successfully.");
        return newVisitorMember.getMember();
    }


    //TODO - Going through members after visitors in market will result in Exception ( removed item for members who visiting twice)
    public void updateVisitorsInRemoveOfItem(Shop shop, Item itemToRemove) throws MarketException {
        for ( Visitor visitor : visitorsInMarket.values ()){
            visitor.getCart ().removeItem ( shop, itemToRemove);
        }
        for ( Member member: members.values ()){
            member.getMyCart().removeItem( shop, itemToRemove);
        }
        EventLog.getInstance().Log("Visitors cart has been updated due to item removal.");
    }
    public synchronized void setRegisteredAvg(){

    }

    public boolean isLoggedIn(String visitorName) {
        return visitorsInMarket.containsKey ( visitorName );
    }
    public boolean isMember(String visitorName) {
        return members.get ( visitorName ) != null;
    }

    public Member getMember(String visitorName) {
        return members.get ( visitorName );
    }

  
    public void setNextUniqueNumber(int nextUniqueNumber) {
        this.nextUniqueNumber.setValue(nextUniqueNumber);
    }

    public String getUsersInfo(){
        StringBuilder s = new StringBuilder("Numbers of avg new members per day:"+getRegisteredMembersAvg()+"\n");
        s.append("------------------------------------------");
        return s.toString();
    }
    private synchronized long getRegisteredMembersAvg() {
        int daysPassed = openingDate.until(LocalDate.now()).getDays();
        long newAvg = members.size()/ daysPassed;
        DecimalFormat format = new DecimalFormat("0.00");
        newAvg = Long.parseLong(format.format(newAvg));
        this.registeredMembersAvg = newAvg;
        return newAvg;
    }


    public void reset() {
        members = new HashMap<>();
        visitorsInMarket = new HashMap<>();
        nextUniqueNumber.reset();
    }

    public boolean allInMarket(List<String> list) {

        for(String name :list){
            if(!members.containsKey(name)){
                return false;
            }
        }
        return true;
    }
}

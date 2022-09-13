package com.example.server.businessLayer.Market;

import com.example.server.businessLayer.Publisher.NotificationHandler;
import com.google.gson.Gson;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
public class Statistics {

    public static class StatisticsData{

        //daily visitors.
        //daily members.
        //daily owners
        //daily managers who are not owners.
        // system managers.
        public int numOfVisitors;
        public int numOfRegularMembers;
        public int numOfShopsManagers;
        public int numOfOwners;
        public int numOfSystemManager;



        public StatisticsData(){
            numOfVisitors=0;
            numOfRegularMembers=0;
            numOfShopsManagers =0;
            numOfOwners=0;
            numOfSystemManager=0;

        }

        public void resetStatistics(){
            numOfVisitors=0;
            numOfRegularMembers=0;
            numOfShopsManagers =0;
            numOfOwners=0;
            numOfSystemManager=0;
        }

        public int getNumOfVisitors() {
            return numOfVisitors;
        }

        public void setNumOfVisitors(int numOfVisitors) {
            this.numOfVisitors = numOfVisitors;
        }

        public int getNumOfRegularMembers() {
            return numOfRegularMembers;
        }

        public void setNumOfRegularMembers(int numOfRegularMembers) {
            this.numOfRegularMembers = numOfRegularMembers;
        }

        public int getNumOfShopsManagers() {
            return numOfShopsManagers;
        }

        public void setNumOfShopsManagers(int numOfShopsManagers) {
            this.numOfShopsManagers = numOfShopsManagers;
        }

        public int getNumOfOwners() {
            return numOfOwners;
        }

        public void setNumOfOwners(int numOfOwners) {
            this.numOfOwners = numOfOwners;
        }

        public int getNumOfSystemManager() {
            return numOfSystemManager;
        }

        public void setNumOfSystemManager(int numOfSystemManager) {
            this.numOfSystemManager = numOfSystemManager;
        }
    }
    private final NotificationHandler notificationHandler;
    private final StatisticsData data;
    private String systemManager;
    public LocalDate currDate;
    public List<String> numOfRegularMembersID;
    public List<String> numOfShopsManagersID;
    public List<String> numOfOwnersID;
    public List<String> numOfSystemManagerID;
    private static Statistics instance=null;

    public synchronized static Statistics getInstance(){
        if(instance==null){
            instance=new Statistics();
        }
        return instance;
    }

    private Statistics( ){
        systemManager="";
        data= new StatisticsData();
        numOfRegularMembersID=new ArrayList<>();
        numOfShopsManagersID=new ArrayList<>();
        numOfOwnersID=new ArrayList<>();
        numOfSystemManagerID=new ArrayList<>();
        notificationHandler= NotificationHandler.getInstance();
        currDate= LocalDate.now();
    }
    public static void setInstance(Statistics instance) {
        Statistics.instance = instance;
    }


    public synchronized void incNumOfVisitors() {
        checkDate();
        this.data.numOfVisitors ++;
        if(hasManager() && managerLogged()) {
            notificationHandler.sendStatistics(this, systemManager);
        }
    }

    public synchronized void incNumOfMembers(String name) {
        checkDate();
        if(!numOfRegularMembersID.contains(name)) {
            numOfRegularMembersID.add(name);
            this.data.numOfRegularMembers++;
            if (hasManager() && managerLogged()) {
                notificationHandler.sendStatistics(this,systemManager);
            }
        }
    }

    public synchronized void incNumOfManagers(String name) {
        checkDate();
        if(!numOfShopsManagersID.contains(name)) {
            numOfShopsManagersID.add(name);
            this.data.numOfShopsManagers++;
            if (hasManager() && managerLogged()) {
                notificationHandler.sendStatistics(this, systemManager);
            }
        }
    }

    public synchronized void incNumOfOwners(String name) {
        checkDate();
        if(!numOfOwnersID.contains(name)) {
            numOfOwnersID.add(name);
            this.data.numOfOwners++;
            if (hasManager() && managerLogged()) {
                notificationHandler.sendStatistics(this, systemManager);
            }
        }
    }

    public synchronized void incNumOfSystemManagers(String name) {
        checkDate();
        if(!numOfSystemManagerID.contains(name)) {
            numOfSystemManagerID.add(name);
            this.data.numOfSystemManager++;
            if (hasManager() && managerLogged()) {
                notificationHandler.sendStatistics(this, systemManager);
            }
        }
    }

    private boolean hasManager(){
        return (systemManager!=null && !systemManager.isEmpty());
    }
    private boolean managerLogged(){
        return notificationHandler.isConnected(systemManager);
    }
    public void setSystemManager(String manager){
        if(manager!=null && !manager.isEmpty()){
            systemManager=manager;
        }
    }

    private void checkDate() {
        if(!dayUpdated()){
            data.resetStatistics();
            resetList();
        }
    }
    private void incDate(){
        currDate= currDate.plusDays(1);
    }

    private void resetList() {
        numOfOwnersID.clear();
        numOfRegularMembersID.clear();
        numOfShopsManagersID.clear();
        numOfSystemManagerID.clear();
    }
    public boolean dayUpdated(){
        return !(currDate.isBefore(LocalDate.now()));
    }
    @Override
    public String toString() {

        Gson gson =new Gson();
        return gson.toJson(data);
    }
}

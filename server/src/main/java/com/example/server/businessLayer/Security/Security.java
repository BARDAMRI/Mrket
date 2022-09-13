package com.example.server.businessLayer.Security;

import com.example.server.businessLayer.Market.ResourcesObjects.DebugLog;
import com.example.server.businessLayer.Market.ResourcesObjects.EventLog;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Security {
    private Map<String, LoginCard> namesToLoginInfo;

    private static Security instance;

    private Security() {
        this.namesToLoginInfo = new ConcurrentHashMap<>();
    }

    public static Security getInstance() {
        if (instance == null)
            instance = new Security();
        return instance;
    }

    public void validateRegister(String name, String password) throws MarketException {
        validateName(name);
        LoginCard card = new LoginCard(name, password, new ArrayList<>(), new ArrayList<>());
        namesToLoginInfo.put(name, card);
    }

    private void validateName(String name) throws MarketException {
        if (name == null || name.equals("") || name.charAt(0) == '@') {
            DebugLog.getInstance().Log("User tried to register with invalid name.");
            throw new MarketException("Name can't be null or empty string");
        }
        if (namesToLoginInfo.containsKey(name)) {
            DebugLog.getInstance().Log("User tried to register with taken name.");
            throw new MarketException("Name is already taken ,try to be a little more creative and choose another name. ");
        }

    }

    public Map<String, LoginCard> getNamesToLoginInfo() {
        return namesToLoginInfo;
    }

    public void addNewMember(String name, String password, List<String> questions,
                             List<String> answers) throws MarketException {
        LoginCard card = new LoginCard(name, password, questions, answers);
        this.namesToLoginInfo.put(name, card);
    }

    public List<String> getQuestionsByName(String name) {
        return this.namesToLoginInfo.get(name).getQuestions();
    }

    public List<String> validatePassword(String userName, String userPassword) throws MarketException {
        if (userName == null || !namesToLoginInfo.containsKey(userName)) {
            DebugLog debugLog = DebugLog.getInstance();
            debugLog.Log("Non member visitor tried to log in.");
            throw new MarketException("No such user name in the system - " + userName);
        }
        if (!namesToLoginInfo.get(userName).getPassword().equals(userPassword)) {
            DebugLog debugLog = DebugLog.getInstance();
            debugLog.Log("Member " + userName + " tried to log in but has password mismatch.");
            throw new MarketException("Password mismatch");
        }
        List<String> questions = new ArrayList<>();
        LoginCard card = namesToLoginInfo.get(userName);
        for (Map.Entry<String, String> entry : card.getQandA().entrySet()) {
            questions.add(entry.getKey());
        }
        EventLog eventLog = EventLog.getInstance();
        eventLog.Log("Prepared security questions for member.");
        return questions;

    }

  
    public void validateQuestions(String userName, List<String> answers) throws MarketException{
        LoginCard card = namesToLoginInfo.get(userName);
        Map<String, String> QsAndAns = card.getQandA();
        if(answers == null && (QsAndAns == null || QsAndAns.size() == 0))
            return;
        if (answers.size()!=QsAndAns.size()) {
            DebugLog debugLog = DebugLog.getInstance();
            debugLog.Log("Member didnt gave number of answers equal to questions number");
            throw new MarketException("Answers size different from questions size");
        }
        int index = 0;
        for (Map.Entry<String, String> entry : QsAndAns.entrySet()) {
            if (entry.getValue().equals(answers.get(index)))
                index++;
            else {
                DebugLog.getInstance().Log("Mismatch in validating security questions.");
                throw new MarketException(String.format("answer %s does not fit the answers", answers.get(index)));
            }
        }
    }


    public void addPersonalQuery(String userAdditionalQueries, String userAdditionalAnswers, String name) throws MarketException {
        if (!namesToLoginInfo.containsKey(name)) {
            DebugLog.getInstance().Log("Cannot add a personal query cause there is no such user in the system ");
            throw new MarketException("Cannot add a personal query cause there is no such user in the system ");
        }
        if (userAdditionalQueries==null||userAdditionalAnswers==null||userAdditionalAnswers.equals("")||userAdditionalQueries.equals(""))
        {
            DebugLog.getInstance().Log("User tried to add query with empty/null question/answer ");
            throw new MarketException("Question and answer cant be null or empty string");
        }
        else {
            LoginCard card = namesToLoginInfo.get(name);
            Map<String, String> QA = card.getQandA();
            QA.put(userAdditionalQueries, userAdditionalAnswers);
        }
    }

    public void setNamesToLoginInfo(Map<String, LoginCard> namesToLoginInfo) {
        this.namesToLoginInfo = namesToLoginInfo;
    }

    public void reset() {
        namesToLoginInfo = new HashMap<>();
    }

    public String encode(String str) {
        try {
            int mid;
            if(str.length()%2==0){
                mid = str.length() / 2;
            }
            else{
                mid = (str.length() / 2) -1;
            }

            char[] chars = str.toCharArray();
            for (int i = 0; i <= mid; i++) {
                char temp = chars[i];
                chars[i] = (char) (chars[chars.length-1 - i] + mid);
                chars[chars.length-1 - i] = (char) (temp + (mid));
            }
            char[] ch1 = new char[mid];
            char[] ch2 = new char[mid];
            for (int i = 0; i < mid; i++) {
                ch1[i] = chars[i];
            }
            for (int i = mid; i < str.length(); i++) {
               ch2[i-mid] = chars[i];
            }
            for (int i = 0; i < mid; i = i + 2) {
                char temp = ch1[i];
                ch1[i] = ch2[i];
                ch2[i] = temp;
            }
            char[] ret = new char[str.length()];
            for (int i = 0; i < mid; i++) {
                ret[i] = ch1[i];
                ret[i + mid] = ch2[i];
            }
            return String.valueOf(ret);
        } catch (Exception e) {
            return "";
        }
    }

    public String decode(String str) {
        try {
            int mid;
            if(str.length()%2==0){
                mid = str.length() / 2;
            }
            else{
                mid = (str.length() / 2) -1;
            }
            char[] chars = str.toCharArray();
            char[] ch1 = new char[mid];
            char[] ch2 = new char[mid];
            for (int i = 0; i < mid; i++) {
                ch1[i] = chars[i];
            }
            for (int i = mid; i < str.length(); i++) {
                ch2[i-mid] = chars[i];
            }

            for (int i = 0; i < mid; i = i + 2) {
                char temp = ch1[i];
                ch1[i] = ch2[i];
                ch2[i] = temp;
            }

            char[] ret = new char[str.length()];
            for (int i = 0; i < mid; i++) {
                ret[i] = ch1[i];
                ret[i + mid] = ch2[i];
            }
            for (int i = 0; i <= mid; i++) {
                char temp = ret[i];
                ret[i] = (char) (ret[ret.length-1 - i] - mid);
                ret[ret.length-1 - i] = (char) (temp - (mid));
            }
            return String.valueOf(ret);
        } catch (Exception e) {
            return "";
        }
    }


    //Method for notifications.
    //check if there is need to save the notification in case the visitor is member.
    public boolean isMember(String name){ return namesToLoginInfo.containsKey(name);}


    public void removeMember(String memberToRemove) {
        this.namesToLoginInfo.remove(memberToRemove);
    }
}

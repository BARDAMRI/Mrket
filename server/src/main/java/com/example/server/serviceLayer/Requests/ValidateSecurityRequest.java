package com.example.server.serviceLayer.Requests;

import java.util.List;

public class ValidateSecurityRequest {

    private String userName;
    private List<String> answers;
    private String visitorName;

    public ValidateSecurityRequest() {
    }

    public ValidateSecurityRequest(String userName, List<String> answers, String visitorName) {
        this.userName = userName;
        this.answers = answers;
        this.visitorName = visitorName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<String> getAnswers() {
        return answers;
    }

    public void setAnswers(List<String> answers) {
        this.answers = answers;
    }

    public String getVisitorName() {
        return visitorName;
    }

    public void setVisitorName(String visitorName) {
        this.visitorName = visitorName;
    }
}

package com.example.server.serviceLayer.Requests;

public class RemoveMemberRequest {
    String manager;
    String memberToRemove;

    public RemoveMemberRequest(String manager, String memberToRemove) {
        this.manager = manager;
        this.memberToRemove = memberToRemove;
    }
    public RemoveMemberRequest(){}

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }

    public String getMemberToRemove() {
        return memberToRemove;
    }

    public void setMemberToRemove(String memberToRemove) {
        this.memberToRemove = memberToRemove;
    }
}

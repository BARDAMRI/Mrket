package com.example.server.serviceLayer.Requests;

public class GetHistoryByMemberRequest {

    private String systemManagerName;
    private String memberName;

    public GetHistoryByMemberRequest() {
    }

    public String getSystemManagerName() {
        return systemManagerName;
    }

    public void setSystemManagerName(String systemManagerName) {
        this.systemManagerName = systemManagerName;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
}

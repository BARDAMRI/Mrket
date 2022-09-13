package com.example.server.serviceLayer.Requests;

public class getAcqsForMemberRequest {
    private  String memberName;

    public void getAcqsForMemberRequest(){}

    public getAcqsForMemberRequest(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
}

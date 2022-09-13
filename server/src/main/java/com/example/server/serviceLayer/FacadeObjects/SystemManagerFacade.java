package com.example.server.serviceLayer.FacadeObjects;

import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.businessLayer.Market.Users.Member;
import com.example.server.businessLayer.Market.Users.SystemManager;

public class SystemManagerFacade implements FacadeObject<SystemManager> {

    private MemberFacade member;

    public SystemManagerFacade(Member member) {
        this.member = new MemberFacade (member);
    }

    public MemberFacade getMember() {
        return member;
    }

    public void setMember(MemberFacade member) {
        this.member = member;
    }

    @Override
    public SystemManager toBusinessObject() throws MarketException {
        Member member = this.member.toBusinessObject ();
        return new SystemManager( member );
    }
}
package com.example.server.businessLayer.Market.Users;

import com.example.server.businessLayer.Market.Market;

public class SystemManager  {
    private Member member;

    public SystemManager(Member member){
        this.member = member;
    }

    public String getAllSystemPurchaseHistory(Market market) {
        return null;
    }


    public String getHistoryByShop() {
        return null;
    }


    public String getHistoryByMember() {
        return null;
    }

    public Member getMember() {
        return member;
    }

}

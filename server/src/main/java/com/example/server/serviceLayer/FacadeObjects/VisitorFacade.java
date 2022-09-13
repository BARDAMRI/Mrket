package com.example.server.serviceLayer.FacadeObjects;

import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.businessLayer.Market.ShoppingCart;
import com.example.server.businessLayer.Market.Users.Member;
import com.example.server.businessLayer.Market.Users.Visitor;

public class VisitorFacade implements FacadeObject<Visitor>{
    private String name;
    private MemberFacade member;
    private ShoppingCartFacade cart;

    public VisitorFacade(String name, MemberFacade member, ShoppingCartFacade cart) {
        this.name = name;
        this.member = member;
        this.cart = cart;
    }

    public VisitorFacade(Visitor visitor) {
        this.name = visitor.getName();
        this.member = null;
        if (visitor.getMember() != null){
            this.member = new MemberFacade(visitor.getMember());
        }
        this.cart = new ShoppingCartFacade(visitor.getCart());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MemberFacade getMember() {
        return member;
    }

    public void setMember(MemberFacade member) {
        this.member = member;
    }

    public ShoppingCartFacade getCart() {
        return cart;
    }

    public void setCart(ShoppingCartFacade cart) {
        this.cart = cart;
    }

    @Override
    public Visitor toBusinessObject() throws MarketException {
        Member mem = this.member.toBusinessObject();
        ShoppingCart shoppingCart = this.cart.toBusinessObject();
        return new Visitor(this.name,mem,shoppingCart);
    }
}

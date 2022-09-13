package com.example.server.serviceLayer.FacadeObjects;

import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.businessLayer.Market.Market;
import com.example.server.businessLayer.Market.Shop;
import com.example.server.businessLayer.Market.ShoppingBasket;
import com.example.server.businessLayer.Market.ShoppingCart;

import java.util.HashMap;
import java.util.Map;

public class ShoppingCartFacade implements FacadeObject<ShoppingCart> {
    Map<String, ShoppingBasketFacade> cart; // <Shop ,basket for the shop>
    double price;
    public ShoppingCartFacade(){}
    public ShoppingCartFacade(Map<String, ShoppingBasketFacade> cart,double price) {
        this.cart = cart;
        this.price = price;
    }

    public ShoppingCartFacade(ShoppingCart cart) {
        this.cart = new HashMap<>();
        price = cart.getCurrentPrice();
        if(cart.getCart() == null || cart.getCart().size() == 0)
            return;
        for (Map.Entry<Shop, ShoppingBasket> fromCart: cart.getCart().entrySet()){
            ShoppingBasketFacade toBasket = new ShoppingBasketFacade(fromCart.getValue());
            this.cart.put(fromCart.getKey().getShopName(),toBasket);
        }

    }

    public Map<String, ShoppingBasketFacade> getCart() {
        return cart;
    }

    public void setCart(Map<String, ShoppingBasketFacade> cart) {
        this.cart = cart;
    }

    @Override
    public ShoppingCart toBusinessObject() throws MarketException {
        Map<Shop,ShoppingBasket> baskets = new HashMap<>();
        for (Map.Entry<String,ShoppingBasketFacade> entry:this.cart.entrySet())
        {
            Shop shop = Market.getInstance().getShopByName(entry.getKey());
            baskets.put(shop,entry.getValue().toBusinessObject());
        }
        return new ShoppingCart(baskets,this.price);

    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
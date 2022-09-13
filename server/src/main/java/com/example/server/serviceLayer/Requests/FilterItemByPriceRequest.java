package com.example.server.serviceLayer.Requests;

import com.example.server.serviceLayer.FacadeObjects.ItemFacade;

import java.util.List;

public class FilterItemByPriceRequest {
    private List<ItemFacade> items;
    private double minPrice;
    private double maxPrice;

    public FilterItemByPriceRequest() {
        this.items = items;
    }

    public FilterItemByPriceRequest(List<ItemFacade> items, double minPrice, double maxPrice) {
        this.items = items;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public List<ItemFacade> getItems() {
        return items;
    }

    public void setItems(List<ItemFacade> items) {
        this.items = items;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(double minPrice) {
        this.minPrice = minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(double maxPrice) {
        this.maxPrice = maxPrice;
    }
}

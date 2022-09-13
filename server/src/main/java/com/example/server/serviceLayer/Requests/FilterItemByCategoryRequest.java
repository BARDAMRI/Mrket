package com.example.server.serviceLayer.Requests;

import com.example.server.businessLayer.Market.Item;
import com.example.server.serviceLayer.FacadeObjects.ItemFacade;

import java.util.List;

public class FilterItemByCategoryRequest {
    private List<ItemFacade> items;
    private Item.Category category;

    public FilterItemByCategoryRequest() {
    }

    public FilterItemByCategoryRequest(List<ItemFacade> items, Item.Category category) {
        this.items = items;
        this.category = category;
    }

    public List<ItemFacade> getItems() {
        return items;
    }

    public void setItems(List<ItemFacade> items) {
        this.items = items;
    }

    public Item.Category getCategory() {
        return category;
    }

    public void setCategory(Item.Category category) {
        this.category = category;
    }
}

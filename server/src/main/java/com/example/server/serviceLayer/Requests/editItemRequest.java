package com.example.server.serviceLayer.Requests;

import com.example.server.serviceLayer.FacadeObjects.ItemFacade;

public class editItemRequest {
    ItemFacade newItem;
    String id;

    public ItemFacade getNewItem() {
        return newItem;
    }

    public void setNewItem(ItemFacade newItem) {
        this.newItem = newItem;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public editItemRequest(ItemFacade newItem, String id) {
        this.newItem = newItem;
        this.id = id;
    }
}

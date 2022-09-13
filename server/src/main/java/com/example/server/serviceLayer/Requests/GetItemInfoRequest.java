package com.example.server.serviceLayer.Requests;

public class GetItemInfoRequest {
    String name;
    int itemId;

    public GetItemInfoRequest(String name, int itemId) {
        this.name = name;
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
}

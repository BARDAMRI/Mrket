package com.example.server.serviceLayer;

import com.example.server.serviceLayer.FacadeObjects.ShoppingCartFacade;

public class ResponseT<T> extends Response {
    private T value;

    public ResponseT(String message){
        super(message);
    }
    public ResponseT(T value){
        this.value = value;
    }

    public ResponseT(String msg, T value) {
        super(msg);
        this.value = value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }
}

package com.example.server.serviceLayer.FacadeObjects;

public class OutputMessage {

     private String text;
    public OutputMessage( String text) {
        this.text = text;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

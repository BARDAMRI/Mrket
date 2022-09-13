package com.example.server.businessLayer.Market.ResourcesObjects;


public class MarketException extends Exception{

    private final String message;
    private final Object addOn;

    public MarketException(String mess, Object add){
        message=mess;
        addOn=add;
    }

    public MarketException(String mess){
        message=mess;
        addOn=null;
    }
    public MarketException( Object add){
        message="";
        addOn=add;
    }
    public boolean hasObj(){ return addOn!=null;}

    public String getMessage() {return message;}

    public Object getObj() {return addOn;}
}

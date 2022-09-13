package com.example.server.serviceLayer.Requests;

public class InitMarketRequest {
//    private PaymentService paymentService;
//    private ProductsSupplyService supplyService;
    private String userName;
    private String password;

    public InitMarketRequest() {
    }

    public InitMarketRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

//    public PaymentService getPaymentService() {
//        return paymentService;
//    }
//
//    public void setPaymentService(PaymentService paymentService) {
//        this.paymentService = paymentService;
//    }
//
//    public ProductsSupplyService getSupplyService() {
//        return supplyService;
//    }
//
//    public void setSupplyService(ProductsSupplyService supplyService) {
//        this.supplyService = supplyService;
//    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

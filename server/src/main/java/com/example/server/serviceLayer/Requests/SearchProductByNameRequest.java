package com.example.server.serviceLayer.Requests;

public class SearchProductByNameRequest {
    private String productName;
    public SearchProductByNameRequest(){}

    public SearchProductByNameRequest(String productName) {
        this.productName = productName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }
}

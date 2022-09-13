package com.example.server.businessLayer.Payment;

import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;

import java.io.IOException;
import java.util.List;

public interface PaymentService {

    RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(30 * 1000).build();
    int pay(List<NameValuePair> request) throws MarketException, IOException;
    int cancelPayment(List<NameValuePair> request) throws MarketException, IOException;
    String handShake(List<NameValuePair> request) throws Exception;

    void setAddress(String address);
}
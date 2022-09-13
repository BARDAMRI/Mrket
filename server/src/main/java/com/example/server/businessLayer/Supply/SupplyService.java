package com.example.server.businessLayer.Supply;

import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;

import java.io.IOException;
import java.util.List;

public interface SupplyService {

    String TypeSupply = "supply";
    String TypeCancel_supply = "cancel_supply";
    RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(30 * 1000).build();
    int supply(List<NameValuePair> request) throws MarketException, IOException;

    int cancelSupply(List<NameValuePair> request) throws Exception;

    void setAddress(String address);
}
package com.example.server.businessLayer.Supply;

import com.example.server.businessLayer.Market.ResourcesObjects.MarketConfig;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeoutException;

public class WSEPSupplyServiceAdapter implements SupplyService {

    private static WSEPSupplyServiceAdapter instance;
    private String url;

    public static WSEPSupplyServiceAdapter getInstance(){
        if(instance==null){
            instance=new WSEPSupplyServiceAdapter();
        }
        return instance;
    }
    private WSEPSupplyServiceAdapter()
    {
        url = MarketConfig.WSEP_ADDRESS;
    }

    @Override
    public int supply(List<NameValuePair> request) throws MarketException, IOException {
        try {
            return sendRequest(request);
        }
        catch (Exception e){
            throw e;
        }
    }

    @Override
    public int cancelSupply(List<NameValuePair> request) throws MarketException, IOException {
        try {
            return sendRequest(request);
        }
        catch (Exception e){
            throw e;
        }
    }

    /**
     * Sends the request to the given url.
     * @param requestBody the dictionary of values.
     * @return the request result.
     * @throws IOException
     * @throws InterruptedException
     */
    public int sendRequest(List<NameValuePair> requestBody) throws IOException, MarketException {
        HttpClient httpClient ;
        HttpPost request;
        try {
            httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
            request = new HttpPost(url);

            // adding the form data
            request.setEntity(new UrlEncodedFormEntity(requestBody));
        }
        catch (Exception e){
            throw new MarketException("Error0");
        }
        if(httpClient==null | request==null){
            throw new MarketException("Error0");
        }
        int res;
        try{
        //send the request to the service server.
        HttpResponse response= httpClient.execute(request);
        HttpEntity entity = response.getEntity();

        // String of the response
        String responseString = EntityUtils.toString(entity);


            res =Integer.parseInt(responseString);
        }
        catch (Exception e ){
            throw new MarketException("Error1");

        }
        return  res;
    }


    @Override
    public void setAddress(String address) {
        this.url=address;
    }



}

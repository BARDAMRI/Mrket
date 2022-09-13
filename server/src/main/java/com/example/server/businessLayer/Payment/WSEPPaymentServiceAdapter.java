package com.example.server.businessLayer.Payment;

import com.example.server.businessLayer.Market.ResourcesObjects.MarketConfig;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.businessLayer.Supply.WSEPSupplyServiceAdapter;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.util.List;

public class WSEPPaymentServiceAdapter implements PaymentService {

    private static WSEPPaymentServiceAdapter instance= null;
    private String url;


    public static WSEPPaymentServiceAdapter getinstance(){
        if(instance==null){
            instance=new WSEPPaymentServiceAdapter();
        }
        return instance;
    }
    private WSEPPaymentServiceAdapter() {

        url = MarketConfig.WSEP_ADDRESS;
    }

    @Override
    public int pay(List<NameValuePair> requestBody) throws MarketException, IOException {

        try {
            int result;
            result = sendRequest(requestBody);
            return result;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public int cancelPayment(List<NameValuePair> request) throws MarketException, IOException {
        try {
            int result;
            result = sendRequest(request);
            return result;
        } catch (Exception e) {
           throw e;
        }
    }


    /**
     * Makes handshake for payment.
     *
     * @param requestBody the request string.
     * @return "OK" if success. empty if not.
     */
    @Override
    public String handShake(List<NameValuePair> requestBody) throws Exception {
        try {
            String result = "";
            result = sendRequestHandshake(requestBody);
            return result;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void setAddress(String address) {
        this.url=address;
    }

    /**
     * The main method for sending handshake request.
     *
     * @param requestBody the request string.
     * @return the response of the request.
     * @throws IOException
     * @throws InterruptedException
     */
    private String sendRequestHandshake(List<NameValuePair> requestBody) throws Exception {
        HttpClient httpClient;
        HttpPost request;
        try {

            httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
            request = new HttpPost(url);

            // adding the form data
            request.setEntity(new UrlEncodedFormEntity(requestBody));
        } catch (Exception e) {
            throw new MarketException("Error2");
        }
        if (httpClient == null | request == null) {
            throw new MarketException("Error2");
        }
        try {
            //send the request to the service server.
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            // String of the response
            return EntityUtils.toString(entity);
        } catch (Exception e) {
            throw new MarketException("Error2");
        }

    }


    /**
     * Sends the request to the given url.
     *
     * @param requestBody the dictionary of values.
     * @return the request result.
     * @throws IOException
     * @throws InterruptedException
     */
    public int sendRequest(List<NameValuePair> requestBody) throws IOException, MarketException {
        HttpClient httpClient;
        HttpPost request;
        try {
            httpClient = HttpClientBuilder.create().setDefaultRequestConfig(requestConfig).build();
            request = new HttpPost(url);

            // adding the form data
            request.setEntity(new UrlEncodedFormEntity(requestBody));
        } catch (Exception e) {
            throw new MarketException("Error2");
        }
        if (httpClient == null | request == null) {
            throw new MarketException("Error2");
        }
        int res = 0;
        try {
            //send the request to the service server.
            HttpResponse response = httpClient.execute(request);
            HttpEntity entity = response.getEntity();

            // String of the response
            String responseString = EntityUtils.toString(entity);
            res = Integer.parseInt(responseString);
            return res;
        }
        catch (Exception e) {
            throw new MarketException("Error3");
        }

    }
}

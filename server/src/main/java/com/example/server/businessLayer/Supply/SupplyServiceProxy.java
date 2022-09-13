package com.example.server.businessLayer.Supply;

import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SupplyServiceProxy implements SupplyService {

    private final boolean testRequest;
    private SupplyService supplyService;

    public SupplyServiceProxy(SupplyService supplyService1, boolean test) {
        super();
        testRequest = test;
        this.supplyService = supplyService1;
    }

    public SupplyServiceProxy() {
        super();
        testRequest = true;
    }

    /**
     * set supply to a customer.
     *
     * @param address the address to send to.
     * @return the transaction id
     */
    public int supply(Address address) throws MarketException, IOException {
        if(address==null){
            throw new MarketException("Address not supplied");
        }
        if(!address.isLegal()){
            throw new MarketException("Address details are illegal");
        }

        int ret= this.supply(addressToString(address));
        if (ret>100000 | ret<10000){
            throw new MarketException("Error1");
        }
        return ret;

    }

    /**
     * Cancel a supply.
     *
     * @param transactionId the supply transaction is.
     * @return
     */
    public int cancelSupply(int transactionId) throws Exception {

        if (transactionId == -1) {
            return -1;
        }
        int ret= this.cancelSupply(transactionToString(transactionId));
        if (ret==-1){
            throw new MarketException("Error1");
        }
        return ret;
    }

    /**
     * creates a string request from the transaction id and the transaction type.
     *
     * @param transactionId the transaction id to cancel.
     * @return the request string to send.
     * @throws JsonProcessingException
     */
    public List<NameValuePair> transactionToString(int transactionId) {
        List<NameValuePair> params = new ArrayList<NameValuePair>() {
            {

                add(new BasicNameValuePair("action_type", TypeCancel_supply));
                add(new BasicNameValuePair("transaction_id", String.valueOf(transactionId)));
            }
        };

        return params;
    }

    @Override
    public int supply(List<NameValuePair> request) throws MarketException, IOException {
        return supplyService.supply(request);
    }

    @Override
    public int cancelSupply(List<NameValuePair> request) throws Exception {
        return supplyService.cancelSupply(request);
    }

    @Override
    public void setAddress(String address) {
        this.supplyService.setAddress(address);
    }

    /**
     * creates request string out of an address.
     *
     * @param address the address of the customer.
     * @return the request string.
     * @throws JsonProcessingException
     */
    public List<NameValuePair> addressToString(Address address) {
        List<NameValuePair> params = new ArrayList<NameValuePair>() {
            {

                add(new BasicNameValuePair("action_type", TypeSupply));
                add(new BasicNameValuePair("name", address.getName()));
                add(new BasicNameValuePair("address", address.getAddress()));
                add(new BasicNameValuePair("city", address.getCity()));
                add(new BasicNameValuePair("country", address.getCountry()));
                add(new BasicNameValuePair("zip", address.getZip()));
            }
        };

        return params;
    }

    /**
     * sets new supply service to the handler.
     *
     * @param supplyService1
     */
    public void setService(SupplyService supplyService1) {
        this.supplyService = supplyService1;
    }
}

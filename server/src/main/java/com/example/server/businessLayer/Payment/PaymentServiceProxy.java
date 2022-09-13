package com.example.server.businessLayer.Payment;

import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PaymentServiceProxy implements PaymentService{

    private final boolean testRequest;
    private final String okayMessage="OK";
    private final String TypeHandshake="handshake";
    private final String TypePay="pay";
    private final String TypeCancel_pay="cancel_pay";

    private PaymentService paymentService;

    public PaymentServiceProxy(PaymentService paymentService1, boolean test) {

        super();
        this.testRequest=test;
        this.paymentService = paymentService1;
    }

    public PaymentServiceProxy() {

        super();
        this.testRequest=true;
    }


    /**
     * Send the payment post if the handshake works.
     * @param method the payment method. The credit card in this version.
     * @return the transaction id.
     */
    public int pay(PaymentMethod method) throws  Exception {

        try {
            int ret=0;
        if(handshake().equals(okayMessage)){
            if(method instanceof CreditCard){
                ret= payWithCreditCard((CreditCard) method);
            }
            if (ret>100000 | ret<10000){
                throw new MarketException("Error1");
            }
            return ret;
        }
        else
        {
            return -1;
        }

        }
        catch (Exception io){
            throw io;
        }

    }


    /**
     * Cancel a payment.
     * @param transactionId the transaction id to cancel.
     * @return 1 if succeed and -1 if not.
     */
    public int cancelPay(int transactionId) throws Exception {

        try{
            if(transactionId==-1){
                return -1;
            }
            List<NameValuePair> request= transactionToString(transactionId);
            int ret=  this.cancelPayment(request);
            if (ret==-1){
                throw new MarketException("Error2");
            }
            return ret;
        }
        catch(Exception e){
            throw e;
        }
    }


    /**
     * do handshake before payment.
     * @return "OK" if succeed. empty if not.
     */
    private String handshake() throws Exception {

        try {
            String ret= this.handShake(handshakeString());
            if (!ret.equals("OK")){
                throw new MarketException("Error0");
            }
            return ret;
        }
        catch (Exception e){
            throw e;
        }
    }


    /**
     * make payment withcredit card as a payment method.
     * @param card the payment method as parameter.
     * @return the transaction number.
     * @throws Exception
     */
    public int payWithCreditCard(CreditCard card) throws Exception {
        if(card==null){
            throw new MarketException("Credit card not supplied.");
        }
        if(!card.isLegal()){
            throw new MarketException("Credit card details are illegal.");
        }
        List<NameValuePair> request= CreditCardToString(card );
        return this.pay(request);
    }

    /**
     * creates a string request from the credit cart details,address and the transaction type.
     * @param paymentMethod
     * @return the string request.
     * @throws JsonProcessingException
     */
    private List<NameValuePair> CreditCardToString(CreditCard paymentMethod) throws JsonProcessingException {
        List<NameValuePair> params = new ArrayList<NameValuePair>(){
            {

                add(new BasicNameValuePair("action_type", TypePay));
                add(new BasicNameValuePair("card_number", paymentMethod.getNumber()));
                add(new BasicNameValuePair("month", paymentMethod.getMonth()));
                add(new BasicNameValuePair("year",paymentMethod.getYear()));
                add(new BasicNameValuePair("holder", paymentMethod.getHolder()));
                add(new BasicNameValuePair("ccv", paymentMethod.getCvv()));
                add(new BasicNameValuePair("id", paymentMethod.getId()));
            }
        };

        return params;
    }

    /**
     * creates a string request from the transaction id and the transaction type.
     *
     * @param transactionId the transaction id to cancel.
     * @return the request string to send.
     * @throws JsonProcessingException
     */
    private List<NameValuePair> transactionToString(int transactionId) throws JsonProcessingException {

        List<NameValuePair> params = new ArrayList<NameValuePair>(){
            {

                add(new BasicNameValuePair("action_type", TypeCancel_pay ));
                add(new BasicNameValuePair("transaction_id", String.valueOf(transactionId)));
            }
        };

        return params;
    }

    /**
     * Creates request string for handshake.
     *
     * @return the request.
     */
    private List<NameValuePair> handshakeString(){
        List<NameValuePair> params = new ArrayList<NameValuePair>(){
            {

                add(new BasicNameValuePair("action_type", TypeHandshake ));
            }
        };

        return params;
    }


    /**
     * sets new payment service to the handler.
     * @param paymentService1
     */
    public void setService(PaymentService paymentService1) {
        this.paymentService=paymentService1;
    }

    @Override
    public int pay(List<NameValuePair> request) throws MarketException, IOException {
        return paymentService.pay(request);
    }

    @Override
    public int cancelPayment(List<NameValuePair> request) throws MarketException, IOException {
        return paymentService.cancelPayment(request);
    }

    @Override
    public String handShake(List<NameValuePair> request) throws Exception {
        return paymentService.handShake(request);
    }

    @Override
    public void setAddress(String address) {
        this.paymentService.setAddress(address);
    }
}

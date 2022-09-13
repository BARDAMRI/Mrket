package com.example.server.AcceptanceTest;

import com.example.server.serviceLayer.Requests.InitMarketRequest;
import com.example.server.serviceLayer.Response;
import com.example.server.serviceLayer.Service;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class SystemAcceptanceTests extends AcceptanceTests {


    @BeforeAll
    public static void setup() {
//        initMarket();
    }


    @Before
    public void reset() {
        try {
            setItemCurrentAmount(shopOwnerName, yogurt, productAmount, shopName);
        } catch (Exception e) {
            String msg = e.getMessage();
        }
    }

    @Test
    @DisplayName("init market twice test - fail")
    public void initTwice() {
        try {
            Response result = initMarket();
            assert result.isErrorOccurred();
        } catch (Exception e) {
            // should return as result fail and not as exception
            assert false;
        }
        ;
    }

    public static Response initMarket(){
        InitMarketRequest request = new InitMarketRequest(systemManagerName, systemManagerPassword);
        String methodCall = "/firstInitMarket";
        Response res = Service.getInstance().firstInitMarket(request);
        return res;
    }
}

package com.example.server.UnitTests;

import com.example.server.businessLayer.Security.LoginCard;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.businessLayer.Security.Security;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.HashMap;
import java.util.Map;

public class SecurityTests {
    Security security = Security.getInstance();
    @Mock
    LoginCard loginCard;

    @BeforeEach
    public void init(){
        Map<String,LoginCard> emptyMap = new HashMap<>();
        security.setNamesToLoginInfo(emptyMap);
    }

    @Test
    @DisplayName("Valid register")
    public void ValidRegisterTest(){
        Map<String,LoginCard> nameToLoginInfo = new HashMap<>();
        try {
            security.validateRegister("raz","123");
            security.validateRegister("ido","123");
            Assertions.assertEquals(security.getNamesToLoginInfo().size(),2);
            Assertions.assertTrue(security.getNamesToLoginInfo().containsKey("raz"));

        }
        catch (MarketException e)
        {
            assert false;
        }

    }
    @Test
    @DisplayName("Invalid register - Name Taken")
    public void InvalidRegisterTestNameTaken(){
        Map<String,LoginCard> nameToLoginInfo = new HashMap<>();
        try {
            security.validateRegister("raz","123");
            security.validateRegister("raz","123");
            assert false;
        }
        catch (MarketException e)
        {
            assert true;
        }
    }

    @Test
    @DisplayName("Invalid register -Invalid Name - Empty String")
    public void InvalidRegisterTestEmptyName(){
        try {
            security.validateRegister("","123");
            assert false;
        }
        catch (MarketException e)
        {
            assert true;
        }
    }
    @Test
    @DisplayName("Invalid register -Invalid Name - Starting with @")
    public void InvalidRegisterTestInvalidSign(){
        try {
            security.validateRegister("@raz","");
            assert false;
        }
        catch (MarketException e1)
        {
            assert true;
        }
    }
    @Test
    @DisplayName("Invalid register -Invalid Name - Null")
    public void InvalidRegisterTestNullName(){
        try {
            security.validateRegister(null,"pass");
            assert false;
        }
        catch (MarketException e2)
        {
            assert true;
        }
    }
    @Test
    @DisplayName("Validate password test - Good test")
    public void ValidatePasswordShouldPass(){
        try {
            security.validateRegister("raz", "123");
            security.validatePassword("raz","123");
            assert true;
        }
        catch (Exception e){
            assert false;
        }
    }
    @Test
    @DisplayName("Validate password test - Fail test")
    public void ValidatePasswordShouldNotPass(){
        try {
            security.validateRegister("raz", "123");
            security.validatePassword("raz ","123");
            assert false;
        }
        catch (MarketException e)
        {
            try {
                security.validatePassword("raz","12");
                assert false;
            }
            catch (MarketException e1)
            {
                assert true;
            }
        }
    }

    @Test
    @DisplayName("Add personal query and validate answer- Good test")
    public void AddQueryAndValidateAnswerShouldPass(){
        try {
            security.validateRegister("raz", "123");
            String q = "Whats my name?";
            String a = "raz";
            Assertions.assertEquals(1,security.getNamesToLoginInfo().size());
            security.addPersonalQuery(q,a,"raz");
            loginCard = security.getNamesToLoginInfo().get("raz");
            Assertions.assertEquals(1,loginCard.getQandA().size());
            Assertions.assertEquals("raz",loginCard.getQandA().get("Whats my name?"));
        }
        catch (MarketException e)
        {
            assert false;
        }
    }
    @Test
    @DisplayName("Add personal query and validate answer- Fail test")
    public void AddQueryAndValidateAnswerShouldNotPass(){
        try {
            security.validateRegister("raz", "123");
            String q = "Whats my name?";
            String a = "raz";
            Assertions.assertEquals(1,security.getNamesToLoginInfo().size());
            security.addPersonalQuery(q,a,"raz");
            loginCard = security.getNamesToLoginInfo().get("raz");
            Assertions.assertNotEquals("ido",loginCard.getQandA().get("Whats my name?"));
        }
        catch (MarketException e)
        {
            assert true;
        }
    }
    @Test
    @DisplayName("encode - decode test")
    public void EncoderAndDecoder(){
        try {
            String str = "Hello world! its a test.";
            String res1= security.encode(str);
            Assertions.assertNotEquals(res1,str);
            String res2= security.decode(res1);
            Assertions.assertNotEquals(res1,res2);
            Assertions.assertEquals(str,res2);
        }
        catch (Exception e){
            assert false;
        }
    }
}

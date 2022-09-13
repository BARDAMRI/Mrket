package com.example.server.AcceptanceTest;

import com.example.server.businessLayer.Market.Market;
import com.example.server.serviceLayer.FacadeObjects.MemberFacade;
import com.example.server.serviceLayer.FacadeObjects.VisitorFacade;
import com.example.server.serviceLayer.Response;
import com.example.server.serviceLayer.ResponseT;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class SystemManagerAcceptanceTests extends AcceptanceTests {

    static VisitorFacade currentVisitor;
    static MemberFacade currentMember;
    static String currentMemberPassword;
    static int memberCounter;

    @BeforeAll
    public static void systemManagerSetup() {
        memberCounter = 0;
    }

    @Before
    public void reset() {

    }

    @Test
    @DisplayName("remove regular member")
    public void removeRegularMember() {
        try {
            registerAndLoginNewUser();
            String memberName = currentMember.getName();
            exitMarket(memberName);
            systemManagerName = Market.getInstance ( ).getSystemManagerName ();
            Response response = removeMember(systemManagerName, memberName);
            assert !response.isErrorOccurred();
            response = loginToExistingMember(memberName, currentMemberPassword);
            assert response.isErrorOccurred();
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("remove appointed member")
    public void removeAppointedMember() {
        try {
            registerAndLoginNewUser();
            String memberName = currentMember.getName();
            String shopName = "removeAppointedMember";
            Response openShopRes = openShop(memberName, shopName);
            assert !openShopRes.isErrorOccurred();
            exitMarket(memberName);
            Response response = removeMember(systemManagerName, memberName);
            assert response.isErrorOccurred();
            currentMember = null;
            response = loginToExistingMember(memberName, currentMemberPassword);
            assert !response.isErrorOccurred() && currentMember != null && currentMember.getName().equals(memberName);
        } catch (Exception e) {
            assert false;
        }
    }

    protected String generateMemberName() {
        memberCounter++;
        return "SystemManagerMember" + memberCounter;
    }

    protected void registerAndLoginNewUser() throws Exception {
        currentVisitor = guestLogin();
        currentMemberPassword = "password";
        String name = generateMemberName();
        register(name, currentMemberPassword);
        List<String> questions = memberLogin(name, currentMemberPassword).getValue();
        currentMember = validateSecurityQuestions(name, new ArrayList<>(), currentVisitor.getName()).getValue();

    }

    protected Response loginToExistingMember(String memberName, String memberPassword) throws Exception {
        currentVisitor = guestLogin();
        ResponseT<List<String>> response = memberLogin(memberName, memberPassword);
        if (response.isErrorOccurred()){
            return response;
        }
        List<String> questions = response.getValue();
        ResponseT<MemberFacade> response1 = validateSecurityQuestions(memberName, new ArrayList<>(), currentVisitor.getName());
        currentMember = response1.getValue();
        return response1;
    }


}

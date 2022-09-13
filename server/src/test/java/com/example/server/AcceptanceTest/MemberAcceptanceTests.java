package com.example.server.AcceptanceTest;

import com.example.server.serviceLayer.FacadeObjects.*;
import com.example.server.serviceLayer.Response;
import com.example.server.serviceLayer.ResponseT;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MemberAcceptanceTests extends AcceptanceTests {
    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    class Member {
        MemberFacade testMember;
        MemberFacade testMemberToSaveTest;
        static String testMemberPassword;
        static String testMemberName;
        static String testMemberNameToSaveTest;

        @BeforeAll
        public static void setUpMember() {
            try {
                testMemberName = "managerTest";
                testMemberNameToSaveTest = "ayalaTest";
                testMemberPassword = "password";

            } catch (Exception ignored) {

            }
        }

        @BeforeEach
        public void resetMember() {
            try {
                VisitorFacade visitor = guestLogin();
                register(testMemberName, testMemberPassword);
                register(testMemberNameToSaveTest, testMemberPassword);
                List<String> questions = memberLogin(testMemberName, testMemberPassword).getValue();
                testMember = validateSecurityQuestions(testMemberName, new ArrayList<>(), visitor.getName()).getValue();
                questions = memberLogin(testMemberNameToSaveTest, testMemberPassword).getValue();
                testMemberToSaveTest = validateSecurityQuestions(testMemberNameToSaveTest, new ArrayList<>(), visitor.getName()).getValue();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }

        @AfterEach
        public void endOfMemberTest() throws Exception {
            logout(testMember.getName());
        }

        @Test
        @Order(1)
        @DisplayName("open a new shop")
        public void openNewShop() {
            try {
                String shopTest = "shopTest";
                Response result = openShop(testMember.getName(), shopTest);
                assert !result.isErrorOccurred();
            } catch (Exception e) {
                assert false;
            }
        }

        @Test
        @DisplayName("open shop with visitor - fails")
        public void visitorOpenShop() {
            // open with visitor
            try {
                VisitorFacade visitor = guestLogin();
                Response result = openShop(visitor.getName(), "testHere");
                assert result.isErrorOccurred();
            } catch (Exception e) {
                assert false;
            }
        }

        @Test
        @DisplayName("member opens multiple shops")
        public void openMultipleShops() {
            try {
                String shopTest = "shopTest2";
                Response result = openShop(testMember.getName(), shopTest);
                assert !result.isErrorOccurred();
            } catch (Exception e) {
                assert false;
            }
        }

        @Test
        @DisplayName("open shop with no name  - fails")
        public void openShopWithNoName() {
            try {
                String shopTest = "";
                Response result = openShop(testMember.getName(), shopTest);
                assert result.isErrorOccurred();
            } catch (Exception e) {
                assert false;
            }

        }


        @Test
        @Order(2)
        @DisplayName("open shop with used name")
        public void usedNameOpenShop() {
            try {
                String shopTest = "shopTest";
                Response result = openShop(testMember.getName(), shopTest);
                assert result.isErrorOccurred();
            } catch (Exception e) {
                assert false;
            }
        }

        @Test
        @DisplayName("logout - check member saved")
        public void checkMemberSaved() {
            try {
                addItemToCart(yogurt, productAmount - 1,  testMemberNameToSaveTest);
                testMemberToSaveTest = getMember(testMemberNameToSaveTest);
                ShoppingCartFacade prevCart = getMember(testMemberToSaveTest.getName()).getMyCart();
                VisitorFacade visitor = logout(testMemberToSaveTest.getName());
                assert visitor.getCart().getCart().isEmpty();
                List<String> questions = memberLogin(testMemberToSaveTest.getName(), testMemberPassword).getValue();
                MemberFacade returnedMember = validateSecurityQuestions(testMemberToSaveTest.getName(), new ArrayList<>(),
                        visitor.getName()).getValue();
                Assertions.assertEquals(returnedMember.getAppointedByMe(), testMemberToSaveTest.getAppointedByMe());
                Assertions.assertEquals(returnedMember.getName(), testMemberToSaveTest.getName());
                Assertions.assertEquals(returnedMember.getMyAppointments(), testMemberToSaveTest.getMyAppointments());
                if (testMemberToSaveTest.getMyCart() != returnedMember.getMyCart()) {
                    assert testMemberToSaveTest.getMyCart().getCart().size() == returnedMember.getMyCart().getCart().size();
                    // for each shop - check equals
                    ShoppingCartFacade shoppingCartFacade = testMemberToSaveTest.getMyCart();
                    for(Map.Entry<String,ShoppingBasketFacade> entry : shoppingCartFacade.getCart().entrySet()){
                        assert returnedMember.getMyCart().getCart().containsKey(entry.getKey());
                        ShoppingBasketFacade shoppingBasket = returnedMember.getMyCart().getCart().get(entry.getKey());
                        assert shoppingBasket.getItems().size() == entry.getValue().getItems().size();
                        for(Map.Entry<Integer, Double> entry1 : entry.getValue().getItems().entrySet()){
                            assert shoppingBasket.getItems().containsKey(entry1.getKey());
                            assert shoppingBasket.getItems().get(entry1.getKey()).equals(entry1.getValue());
                        }
                    }
//                    prevCart.getCart().forEach((shop, prevBasket) -> {
//                        assert returnedMember.getMyCart().getCart().containsKey(shop);
//                        ShoppingBasketFacade newBasket = returnedMember.getMyCart().getCart().get(shop);
//                        // for each item in shopping basket
//                        prevBasket.getItems().forEach((item, amount) -> {
//                            assert newBasket.getItems().size() == prevBasket.getItems().size();
//                            assert newBasket.getItems().containsKey(item);
//                            assert newBasket.getItems().get(item).equals(amount);
//                        });
//                    });
                }
                // for each shopping basket - checks equals
            } catch (Exception e) {
                assert false;
            }

        }

        @Test
        @DisplayName("login twice")
        public void loginTwice() {
            try {
                VisitorFacade visitor = guestLogin();
                List<String> questions = memberLogin(testMemberName, testMemberPassword).getValue();
                ResponseT<MemberFacade> responseT = validateSecurityQuestions(testMemberName, new ArrayList<>(), visitor.getName());
                assert responseT.isErrorOccurred();
            } catch (Exception e) {
                assert false;
            }
        }

        @Test
        @DisplayName("member exit system logs out")
        public void memberExitSystem() {
            try {
                Response response = exitMarket(testMember.getName());
                assert !response.isErrorOccurred();
                VisitorFacade visitor = guestLogin();
                List<String> questions = memberLogin(testMemberName, testMemberPassword).getValue();
                testMember = validateSecurityQuestions(testMemberName, new ArrayList<>(), visitor.getName()).getValue();
                assert testMember != null;
            } catch (Exception e) {
                assert false;
            }

        }

        @Test
        @DisplayName("add question")
        public void addQuestion() {
            try {
                VisitorFacade visitor = guestLogin();
                String currName = "questionsName";
                String password = "password";
                ResponseT<Boolean> response = register(currName, password);
                assert !response.isErrorOccurred();
                List<String> questions = memberLogin(currName, password).getValue();
                assert questions.size() == 0;
                MemberFacade member = validateSecurityQuestions(currName, new ArrayList<>(), visitor.getName()).getValue();
                Response queryResponse = addPersonalQuery("whats your mother's name?", "idk", member.getName());
                assert !queryResponse.isErrorOccurred();
                visitor = logout(member.getName());
                questions = memberLogin(currName, password).getValue();
                assert questions.size() == 1;
                assert questions.contains("whats your mother's name?");
                ArrayList<String> answers = new ArrayList<>();
                answers.add("idk");
                member = validateSecurityQuestions(currName, answers, member.getName()).getValue();
                assert member != null;

            } catch (Exception e) {
                assert false;
            }
        }

        @Test
        @DisplayName("invalid authentication")
        public void invalidAuthentication() {
            try {
                VisitorFacade visitor = guestLogin();
                String currName = "questionsName2";
                String password = "password";
                ResponseT<Boolean> response = register(currName, password);
                ResponseT<List<String>> responseQuestions = memberLogin(currName, password);
                ResponseT<MemberFacade> responseMember = validateSecurityQuestions(currName, new ArrayList<>(), visitor.getName());
                MemberFacade member = responseMember.getValue();
                Response queryResponse = addPersonalQuery("whats your mother's name?", "idk", member.getName());
                visitor = logout(member.getName());
                memberLogin(currName, "123");
                responseQuestions = memberLogin(currName, "123");
                assert responseQuestions.isErrorOccurred();
                responseQuestions = memberLogin(currName, password);
                ArrayList<String> answers = new ArrayList<>();
                answers.add("idk1");
                responseMember = validateSecurityQuestions(currName, answers, member.getName());
                assert responseMember.isErrorOccurred();
                answers.clear();
                answers.add("idk");
                responseMember = validateSecurityQuestions(currName, answers, member.getName());
                member = responseMember.getValue();
                assert !responseMember.isErrorOccurred();
                assert member != null;
            } catch (Exception e) {
                assert false;
            }
        }
    }
}

package com.example.server.AcceptanceTest;

import com.example.server.businessLayer.Payment.CreditCard;
import com.example.server.businessLayer.Supply.Address;
import com.example.server.businessLayer.Market.Item;
import com.example.server.serviceLayer.Requests.AppointmentShopManagerRequest;
import com.example.server.serviceLayer.FacadeObjects.*;
import com.example.server.serviceLayer.FacadeObjects.PolicyFacade.Wrappers.DiscountTypeWrapper;
import com.example.server.serviceLayer.FacadeObjects.PolicyFacade.Wrappers.PurchasePolicyTypeWrapper;
import com.example.server.serviceLayer.Requests.*;
import com.example.server.serviceLayer.Response;
import com.example.server.serviceLayer.ResponseT;
import com.example.server.serviceLayer.Service;
import com.google.gson.Gson;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;
import java.util.List;

public class AcceptanceTests {

    static String systemManagerName = "ido";
    static String systemManagerPassword = "password";
    static String shopOwnerName = "shaked";
    static String shopOwnerPassword = "password";
    static String shopName = "Shufersal";
    static Double productAmount;
    static Double productPrice;
    static CreditCard creditCard;
    static Address address;
    static ItemFacade yogurt;

    static double appleAmount;
    static String appleName;
    static Item.Category appleCategory;
    static double applePrice;
    static ArrayList<String> appleKeywords;
    static String appleInfo;
    static ItemFacade apple;

    static double onePlusAmount;
    static String onePlusName;
    static Item.Category onePlusCategory;
    static double onePlusPrice;
    static List<String> onePlusKeywords;
    static String onePlusInfo;
    static ItemFacade onePlus;

    @BeforeAll
    public static void setup() {
        try {
            initMarket();
            // shop manager register
            VisitorFacade visitor = guestLogin();
            register(shopOwnerName, shopOwnerPassword);
            List<String> questions = memberLogin(shopOwnerName, shopOwnerPassword).getValue();
            validateSecurityQuestions(shopOwnerName, new ArrayList<>(), visitor.getName());
            // open shop
            openShop(shopOwnerName, shopName);
            productAmount = 3.0;
            productPrice = 1.2;
            addItemToShop(shopOwnerName, "yogurt", productPrice, Item.Category.general, "soy", new ArrayList<>(), productAmount, shopName);
            List<ItemFacade> res = searchProductByName("yogurt");
            yogurt = res.get(0);
            appleAmount = 4.0;
            appleName = "apple";
            appleCategory = Item.Category.fruit;
            applePrice = 10.0;
            appleKeywords = new ArrayList<>();
            appleKeywords.add("tasty");
            appleKeywords.add("in sale");
            appleInfo = "pink lady";
            addItemToShop(shopOwnerName, appleName, applePrice, appleCategory, appleInfo, appleKeywords, appleAmount, shopName);
            apple = searchProductByName("apple").get(0);

            onePlusAmount = 2.0;
            onePlusName = "onePlus";
            onePlusCategory = Item.Category.cellular;
            onePlusPrice = 1000;
            onePlusKeywords = new ArrayList<>();
            onePlusKeywords.add("best seller");
            onePlusKeywords.add("in sale");
            onePlusInfo = "9-5g";
            addItemToShop(shopOwnerName, onePlusName, onePlusPrice, onePlusCategory, onePlusInfo, onePlusKeywords, onePlusAmount, shopName);
            onePlus = searchProductByName(onePlusName).get(0);
            creditCard = new CreditCard("1234567890", "5", "2024", "555", "Ido livne", "204534839");
            address = new Address("Bar Damri", "Ben Gurion 3", "Tel Aviv", "Israel", "1234");
        } catch (Exception e) {
            String msg = e.getMessage();
        }
    }

    @BeforeEach
    public void reset() {
        try {
            setItemCurrentAmount(shopOwnerName, yogurt, productAmount, shopName);
        } catch (Exception e) {
            String msg = e.getMessage();
        }
    }

    protected static ResponseT<MemberFacade> validateSecurityQuestions(String userName, List<String> answers, String visitorName) {
        ValidateSecurityRequest request = new ValidateSecurityRequest(userName, answers, visitorName);
        ResponseT<MemberFacade> result = Service.getInstance().validateSecurityQuestions(request);
        return result;
    }

    protected Response addItemToCart(ItemFacade itemToInsert, double amount, String visitorName) throws Exception {
        AddItemToShoppingCartRequest request = new AddItemToShoppingCartRequest(itemToInsert, amount,  visitorName);
        Response result = Service.getInstance().addItemToShoppingCart(request);
        return result;
    }
    protected ResponseT<ShoppingCartFacade> showShoppingCart(String visitorName){
        return Service.getInstance().showShoppingCart(new RequestVisitorName(visitorName));
    }
    protected Response buyShoppingCart(String visitorName, double expectedPrice, CreditCard paymentMethod, Address address) throws Exception {
        BuyShoppingCartRequest request = new BuyShoppingCartRequest(visitorName, expectedPrice, paymentMethod, address);
        Response res = Service.getInstance().buyShoppingCart(request);
        return res;
    }

    protected static ResponseT<ShopFacade> addItemToShop(String shopOwnerName, String name, double price, Item.Category category, String info, List<String> keywords, double amount, String shopName) throws Exception {
        AddItemToShopRequest request = new AddItemToShopRequest(shopOwnerName, name, price, category, info, keywords, amount, shopName);
        ResponseT<ShopFacade> result = Service.getInstance().addItemToShop(request);
        return result;
    }

    protected static ResponseT<ShopFacade> getShopInfo(String name, String shopName) {
        TwoStringRequest request = new TwoStringRequest(name, shopName);
        ResponseT<ShopFacade> result = Service.getInstance().getShopInfo(request);
        return result;
    }

    protected ItemFacade getItemInfo(String name, int itemId) {
        GetItemInfoRequest request = new GetItemInfoRequest(name, itemId);
        ResponseT<ItemFacade> result = Service.getInstance().getItemInfo(request);
        return result.getValue();
    }

    protected static VisitorFacade guestLogin() throws Exception {
        ResponseT<VisitorFacade> result = Service.getInstance().guestLogin();
        VisitorFacade visitor = result.getValue();
        return visitor;
    }

    protected Response setItemCurrentAmount(String shopOwnerName, ItemFacade item, double amount, String shopName) throws Exception {
        SetItemCurrentAmountRequest request = new SetItemCurrentAmountRequest(shopOwnerName, item, amount, shopName);
        Response res = Service.getInstance().setItemCurrentAmount(request);
        return res;
    }


    protected static List<ItemFacade> searchProductByName(String productName) throws Exception {
        SearchProductByNameRequest request = new SearchProductByNameRequest(productName);
        ResponseT<List<ItemFacade>> result = Service.getInstance().searchProductByName(request);
        return result.getValue();
    }

    protected List<ItemFacade> searchProductByCategory(Item.Category category) throws Exception {
        Item.Category request = category;
        ResponseT<List<ItemFacade>> result = Service.getInstance().searchProductByCategory(request);
        return result.getValue();
    }

    protected List<ItemFacade> searchProductByKeyword(String keyword) throws Exception {
        SearchProductByNameRequest request = new SearchProductByNameRequest(keyword);
        ResponseT<List<ItemFacade>> res = Service.getInstance().searchProductByKeyword(request);
        return res.getValue();
    }

    protected static ResponseT<Boolean> register(String name, String password) throws Exception {
        NamePasswordRequest request = new NamePasswordRequest(name, password);
        ResponseT<Boolean> result = Service.getInstance().register(request);
        return result;
    }

    protected static Response initMarket() {
        InitMarketRequest request = new InitMarketRequest(systemManagerName, systemManagerPassword);
        Response res = Service.getInstance().firstInitMarket(request);
        return res;
    }

    protected Response exitMarket(String name) throws Exception {
        ExitSystemRequest request = new ExitSystemRequest(name);
        Response result = Service.getInstance().exitSystem(request);
        return result;
    }

    protected static Response openShop(String managerName, String shopName) throws Exception {
        OpenNewShopRequest request = new OpenNewShopRequest(managerName, shopName);
        Response result = Service.getInstance().openNewShop(request);
        return result;
    }

    protected static ResponseT<List<String>> memberLogin(String name, String password) throws Exception {
        NamePasswordRequest request = new NamePasswordRequest(name, password);
        ResponseT<List<String>> result = Service.getInstance().memberLogin(request);
        return result;
    }

    protected Response closeShop(String shopOwnerName, String shopName) throws Exception {
        CloseShopRequest request = new CloseShopRequest(shopOwnerName, shopName);
        Response result = Service.getInstance().closeShop(request);
        return result;
    }

    protected Response removeItemFromShop(String shopOwnerName, ItemFacade item, String shopName) throws Exception {
        RemoveItemFromShopRequest request = new RemoveItemFromShopRequest(shopOwnerName, item, shopName);
        Response result = Service.getInstance().removeItemFromShop(request);
        return result;

    }

    protected VisitorFacade logout(String name) throws Exception {
        RequestVisitorName request = new RequestVisitorName(name);
        ResponseT<VisitorFacade> result = Service.getInstance().logout(request);
        return result.getValue();
    }


    protected Response addPersonalQuery(String userAdditionalQueries, String userAdditionalAnswers, String member) throws Exception {
        AddPersonalQueryRequest request = new AddPersonalQueryRequest(userAdditionalQueries, userAdditionalAnswers, member);
        Response result = Service.getInstance().addPersonalQuery(request);
        return result;
    }

    protected Response changeShopItemInfo(String shopOwnerName, String info, ItemFacade oldItem, String shopName) throws Exception {
        ChangeShopItemInfoRequest request = new ChangeShopItemInfoRequest(shopOwnerName, info, oldItem, shopName);
        Response result = Service.getInstance().changeShopItemInfo(request);
        return result;
    }


    protected Response appointShopOwner(String shopOwnerName, String appointedShopOwner, String shopName) throws Exception {
        AppointmentShopOwnerRequest request = new AppointmentShopOwnerRequest(shopOwnerName, appointedShopOwner, shopName);
        Response result = Service.getInstance().appointShopOwner(request);
        return result;
    }

    protected Response appointShopManager(String shopOwnerName, String appointedShopManager, String shopName) throws Exception {
        AppointmentShopManagerRequest request = new AppointmentShopManagerRequest(shopOwnerName, appointedShopManager, shopName);
        Response result = Service.getInstance().appointShopManager(request);
        return result;
    }

    protected List<AppointmentFacade> getShopEmployeesInfo(String shopManagerName, String shopName) throws Exception {
        GetShopEmployeesRequest request = new GetShopEmployeesRequest(shopManagerName, shopName);
        ResponseT<List<AppointmentFacade>> result = Service.getInstance().getShopEmployeesInfo(request);
        return result.getValue();
    }

    protected MemberFacade getMember(String memberName) {
        return Service.getInstance().getMember(memberName).getValue();
    }

    protected VisitorFacade getVisitor(String name) {
        return Service.getInstance().getVisitor(name).getValue();
    }

    protected ItemFacade getItemById(int id) {
        return Service.getInstance().getItemById(id).getValue();
    }

    protected Response addDiscountToShop(DiscountTypeWrapper discount, String shopName, String visitorName) {
        AddDiscountToShopRequest request = new AddDiscountToShopRequest(discount, shopName, visitorName);
        return Service.getInstance().addDiscountToShop(request);
    }

    protected Response removeDiscountFromShop(DiscountTypeWrapper discount, String shopName, String visitorName) {
        RemoveDiscountFromShopRequest request = new RemoveDiscountFromShopRequest(discount, shopName, visitorName);
        return Service.getInstance().removeDiscountFromShop(request);
    }

    protected Response addPurchasePolicyToShop(PurchasePolicyTypeWrapper purchasePolicyTypeFacade, String shopName, String visitorName) {
        AddPurchasePolicyToShopRequest request = new AddPurchasePolicyToShopRequest(purchasePolicyTypeFacade, shopName, visitorName);
        return Service.getInstance().addPurchasePolicyToShop(request);
    }

    protected Response removePurchasePolicyFromShop(PurchasePolicyTypeWrapper purchasePolicyTypeFacade, String shopName, String visitorName){
        RemovePurchasePolicyFromShopRequest request = new RemovePurchasePolicyFromShopRequest(purchasePolicyTypeFacade, shopName, visitorName);
        return Service.getInstance().removePurchasePolicyFromShop(request);
    }

    protected ResponseT<List<PurchasePolicyTypeWrapper>> getPurchasePoliciesOfShop(String visitorName, String shopName){
        GetPoliciesRequest request = new GetPoliciesRequest(visitorName,shopName);
        return Service.getInstance().getPurchasePoliciesOfShop(request);
    }

    protected  ResponseT<List<DiscountTypeWrapper>> getDiscountTypesOfShop(String visitorName, String shopName) {
        GetPoliciesRequest request = new GetPoliciesRequest(visitorName, shopName);
        return Service.getInstance().getDiscountTypesOfShop(request);
    }


    public Response removeMember(String manager, String memberToRemove) {
        RemoveMemberRequest request = new RemoveMemberRequest(manager, memberToRemove);
        return Service.getInstance().removeMember(request);
    }

    public Response removeShopOwnerAppointment(String boss, String firedAppointed, String shopName) {
        RemoveAppointmentRequest request = new RemoveAppointmentRequest(boss, firedAppointed,shopName);
        return Service.getInstance().removeShopOwnerAppointment(request);
    }

    public Response approveAppointment(String shopOwnerName,String appointed,String shopName)
    {
        ApproveAppointmentRequest request = new ApproveAppointmentRequest(shopName,appointed,shopOwnerName);
        return Service.getInstance().approveAppointment(request);
    }


}

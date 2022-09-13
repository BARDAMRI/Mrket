package com.example.server.AcceptanceTest;

import com.example.server.businessLayer.Market.Item;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.CompositeDiscount.MaxCompositeDiscount;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.Condition.PriceCondition;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.ConditionalDiscount;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.DiscountState.ItemLevelState;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.DiscountState.ShopLevelState;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.DiscountType;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.SimpleDiscount;
import com.example.server.businessLayer.Market.Policies.PurchasePolicy.AtLeastPurchasePolicyType;
import com.example.server.businessLayer.Market.Policies.PurchasePolicy.AtMostPurchasePolicyType;
import com.example.server.businessLayer.Market.Policies.PurchasePolicy.OrCompositePurchasePolicyType;
import com.example.server.businessLayer.Market.Policies.PurchasePolicy.PurchasePolicyState.CategoryPurchasePolicyLevelState;
import com.example.server.businessLayer.Market.Policies.PurchasePolicy.PurchasePolicyState.ItemPurchasePolicyLevelState;
import com.example.server.businessLayer.Market.Policies.PurchasePolicy.PurchasePolicyState.PurchasePolicyLevelState;
import com.example.server.businessLayer.Market.Policies.PurchasePolicy.PurchasePolicyState.ShopPurchasePolicyLevelState;
import com.example.server.businessLayer.Market.Policies.PurchasePolicy.PurchasePolicyType;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.serviceLayer.FacadeObjects.*;
import com.example.server.serviceLayer.FacadeObjects.PolicyFacade.Wrappers.DiscountTypeWrapper;
import com.example.server.serviceLayer.FacadeObjects.PolicyFacade.Wrappers.PurchasePolicyTypeWrapper;
import com.example.server.serviceLayer.Response;
import com.example.server.serviceLayer.ResponseT;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ShopOwnerAcceptanceTests extends AcceptanceTests {
    static private String steakName;
    static int steakPrice = 10;
    static private Item.Category steakCategory;
    static private ArrayList<String> steakKeywords;
    static private String steakInfo;

    static private DiscountType simpleDiscountYogurt;
    static private DiscountType maxCompositeDiscount;
    // atleast 1 of yogurt's category
    static private PurchasePolicyType atLeastOnePolicy;
    // atmost 1 yogurt (item), atleast 1 of steak category.
    // or composite
    static private PurchasePolicyType compositePolicy;

    static boolean setupFailed;
    static private MemberFacade ownerA;
    static private MemberFacade ownerB;
    static private MemberFacade ownerC;
    static private ShopFacade removingShop;


    @BeforeAll
    public static void shopSetup() throws MarketException {
        steakName = "steak";
        steakCategory = Item.Category.meat;
        steakKeywords = new ArrayList<>();
        steakInfo = " best in town";

        ////////// DISCOUNTS //////////
        // composite
        DiscountType simpleDiscount = new SimpleDiscount(25, new ItemLevelState(yogurt.getId()));
        DiscountType simpleDiscount2 = new SimpleDiscount(25, new ItemLevelState(apple.getId()));
        List<DiscountType> discountTypeList = new ArrayList<>();
        discountTypeList.add(simpleDiscount);
        discountTypeList.add(simpleDiscount2);
        DiscountType condDisc = new ConditionalDiscount(30, new ShopLevelState(), new PriceCondition(3 * applePrice));
        discountTypeList.add(condDisc);
        maxCompositeDiscount = new MaxCompositeDiscount(discountTypeList);
        // simple
        simpleDiscountYogurt = new SimpleDiscount(50, new ShopLevelState());

        // POLICIES
        // simple
        CategoryPurchasePolicyLevelState categoryPurchasePolicyLevelState = new CategoryPurchasePolicyLevelState(yogurt.getCategory());
        atLeastOnePolicy = new AtLeastPurchasePolicyType(categoryPurchasePolicyLevelState, 1);
        // composite
        PurchasePolicyLevelState shopLevelState = new ShopPurchasePolicyLevelState ();
        PurchasePolicyType atLeastTwoPolicy2 = new AtLeastPurchasePolicyType(shopLevelState, 2);
        PurchasePolicyLevelState yogurtAtMost = new ItemPurchasePolicyLevelState(yogurt.getId());
        PurchasePolicyType atMostPolicy = new AtMostPurchasePolicyType(yogurtAtMost, 1);
        List<PurchasePolicyType> policies = new ArrayList<>();
        policies.add(atLeastTwoPolicy2);
        policies.add(atMostPolicy);
        compositePolicy = new OrCompositePurchasePolicyType(policies);

        // REMOVE APPOINTMENTS
        try {
            setupFailed = false;
            String removerA = "removerA";
            register(removerA, "1");
            VisitorFacade visitor = guestLogin();
            ResponseT<List<String>> questions1 = memberLogin(removerA, "1");
            ownerA = validateSecurityQuestions(removerA, new ArrayList<>(),visitor.getName() ).getValue();
            String removerB = "removerB";
            register(removerB, "1");
            visitor = guestLogin();
            ResponseT<List<String>> questions2 = memberLogin(removerB, "1");
            ownerB = validateSecurityQuestions(removerB, new ArrayList<>(), visitor.getName()).getValue();
            String removerC = "removerC";
            register(removerC, "1");
            visitor = guestLogin();
            ResponseT<List<String>> questions3 = memberLogin(removerC, "1");
            ownerC = validateSecurityQuestions(removerC, new ArrayList<>(),visitor.getName() ).getValue();
            String shopName = "removingShop";
            openShop(removerA, shopName);
            removingShop = getShopInfo(ownerA.getName(), shopName).getValue();
        } catch (Exception e) {
            setupFailed = true;
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

    @Test
    @Order(1)
    @DisplayName("add new item")
    public void addNewItem() {
        try {

            ResponseT<ShopFacade> response = addItemToShop(shopOwnerName, steakName, steakPrice, steakCategory,
                    steakInfo, steakKeywords, 5.0, shopName);
            assert !response.isErrorOccurred();
            ShopFacade shop = getShopInfo(shopOwnerName, shopName).getValue();
            boolean found = false;
            for (Map.Entry<java.lang.Integer, Double> itemAmount : shop.getItemsCurrentAmount().entrySet()) {
                ItemFacade item = getItemById(itemAmount.getKey());
                Double amount = itemAmount.getValue();
                if (item.getName().equals("steak")) {
                    found = amount.equals(5.0);
                    break;
                }
            }
            assert found;

        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @Order(2)
    @DisplayName("add existing item")
    public void addExistingItem() {
        try {
            ResponseT<ShopFacade> response = addItemToShop(shopOwnerName, "yogurt", productPrice, Item.Category.general,
                    "soy", new ArrayList<>(), 10, shopName);
            assert response.isErrorOccurred();
            ShopFacade shop = getShopInfo(shopOwnerName, shopName).getValue();
            boolean found = false;
            for (Map.Entry<java.lang.Integer, Double> itemAmount : shop.getItemsCurrentAmount().entrySet()) {
                ItemFacade item = getItemById(itemAmount.getKey());
                Double amount = itemAmount.getValue();
                if (item.getName().equals("yogurt")) {
                    found = amount.equals(productAmount);
                    break;
                }
            }
            assert found;

        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @Order(3)
    @DisplayName("set item amount - valid")
    public void setValidItemAmount() {
        try {

            Response response = setItemCurrentAmount(shopOwnerName, yogurt, 8.0, shopName);
            assert !response.isErrorOccurred();
            ShopFacade shop = getShopInfo(shopOwnerName, shopName).getValue();
            boolean found = false;
            for (Map.Entry<java.lang.Integer, Double> itemToAmount : shop.getItemsCurrentAmount().entrySet()) {
                ItemFacade item = getItemById(itemToAmount.getKey());
                Double amount = itemToAmount.getValue();
                found = item.getName().equals("yogurt") && amount.equals(8.0);
                if (found) break;
            }
            assert found;

        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @Order(4)
    @DisplayName("set item amount 0")
    public void setValidItemAmountZero() {
        try {

            Response response = setItemCurrentAmount(shopOwnerName, yogurt, 0, shopName);
            assert !response.isErrorOccurred();
            ShopFacade shop = getShopInfo(shopOwnerName, shopName).getValue();
            boolean found = false;
            for (Map.Entry<java.lang.Integer, Double> itemToAmount : shop.getItemsCurrentAmount().entrySet()) {
                ItemFacade item = getItemById(itemToAmount.getKey());
                Double amount = itemToAmount.getValue();
                if (found) break;
                found = item.getName().equals("yogurt") && amount.equals(0.0);
            }
            assert found;

        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @Order(5)
    @DisplayName("remove item from shop")
    public void removeItemFromShopTest() {
        try {
            ShopFacade shop = getShopInfo(shopOwnerName, shopName).getValue();
            ItemFacade steak = null;
            for (Map.Entry<java.lang.Integer, ItemFacade> items : shop.getItemMap().entrySet()) {
                if (items.getValue().getName().equals(steakName)) {
                    steak = items.getValue();
                }
            }
            assert steak != null;
            Response response = removeItemFromShop(shopOwnerName, steak, shopName);
            assert !response.isErrorOccurred();
            shop = getShopInfo(shopOwnerName, shopName).getValue();
            for (Map.Entry<java.lang.Integer, ItemFacade> items : shop.getItemMap().entrySet()) {
                assert !items.getValue().getName().equals(steakName);
            }


        } catch (Exception e) {
            assert false;
        }

    }

    @Test
    @DisplayName("set item amount negative")
    public void setValidItemAmountNegative() {
        try {
            Response response = setItemCurrentAmount(shopOwnerName, yogurt, -1, shopName);
            assert response.isErrorOccurred();
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("change item info")
    public void setItemInfo() {
        try {
            String newInfo = "out of Date!";
            Response response = changeShopItemInfo(shopOwnerName, newInfo, yogurt, shopName);
            assert !response.isErrorOccurred();
            ShopFacade newShop = getShopInfo(shopOwnerName, shopName).getValue();
            boolean found = false;
            for (Map.Entry<java.lang.Integer, ItemFacade> idToItem : newShop.getItemMap().entrySet()) {
                ItemFacade item = idToItem.getValue();
                if (item.getName().equals(yogurt.getName())) {
                    found = item.getInfo().equals(newInfo);
                    break;
                }
            }
            assert found;

        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("change item info to empty")
    public void changeToEmptyInfo() {
        try {
            String newInfo = "";
            Response response = changeShopItemInfo(shopOwnerName, newInfo, yogurt, shopName);
            assert !response.isErrorOccurred();
            ShopFacade newShop = getShopInfo(shopOwnerName, shopName).getValue();
            boolean found = false;
            for (Map.Entry<java.lang.Integer, ItemFacade> idToItem : newShop.getItemMap().entrySet()) {
                ItemFacade item = idToItem.getValue();
                if (item.getName().equals(yogurt.getName())) {
                    found = item.getInfo().equals(newInfo);
                    break;
                }
            }
            assert found;

        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("appoint new shop owner")
    public void appointOwner() {
        try {
            VisitorFacade nextOwner = guestLogin();
            String nextOwnerName = "raz1";
            String nextOwnerPass = "123";
            Response response = register(nextOwnerName, nextOwnerPass);
            ShopFacade shop = getShopInfo(shopOwnerName, shopName).getValue();
            int prevAppoints = shop.getEmployees().size();
            response = appointShopOwner(shopOwnerName, nextOwnerName, shopName);
            assert !response.isErrorOccurred();
            shop = getShopInfo(shopOwnerName, shopName).getValue();
            assert shop.getEmployees().size() - 1 == prevAppoints;
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("appoint new shop manager")
    public void appointShopManagerTest() {
        try {
            VisitorFacade nextOwner = guestLogin();
            String nextOwnerName = "raz2";
            String nextOwnerPass = "123";
            Response response = register(nextOwnerName, nextOwnerPass);
            ShopFacade shop = getShopInfo(shopOwnerName, shopName).getValue();
            int prevAppoints = shop.getEmployees().size();
            response = appointShopManager(shopOwnerName, nextOwnerName, shopName);
            assert !response.isErrorOccurred();
            shop = getShopInfo(shopOwnerName, shopName).getValue();
            assert shop.getEmployees().size() - 1 == prevAppoints;
        } catch (Exception e) {
            assert false;
        }
    }


    @Test
    @DisplayName("close shop")
    public void closeShopTest() {
        try {
            VisitorFacade visitor = guestLogin();
            String password = "password";
            String name = "razBam";
            register(name, password);
            List<String> questions = memberLogin(name, password).getValue();
            validateSecurityQuestions(name, new ArrayList<>(), visitor.getName());
            // open shop
            String shopName = "RealMadrid_Fuckers";
            openShop(name, shopName);
            ShopFacade shop = getShopInfo(name, shopName).getValue();
            Response response = closeShop(name, shopName);
            assert !response.isErrorOccurred();
            ResponseT<ShopFacade> responseShop = getShopInfo(name, shopName);
            Assertions.assertTrue(responseShop.isErrorOccurred());
        } catch (Exception e) {
            assert false;
        }
    }

    /// DISCOUNT TESTS

    @Test
    @DisplayName("add simple discount")
    @Order(6)
    public void addSimpleDiscount() {
        ResponseT<List<DiscountTypeWrapper>> discounts = this.getDiscountTypesOfShop(shopOwnerName, shopName);
        assert !discounts.isErrorOccurred();
        int curNum = discounts.getValue().size();
        DiscountTypeWrapper discountTypeWrapper = DiscountTypeWrapper.createDiscountTypeWrapper(simpleDiscountYogurt);
        this.addDiscountToShop(discountTypeWrapper, shopName, shopOwnerName);
        discounts = this.getDiscountTypesOfShop(shopOwnerName, shopName);
        assert !discounts.isErrorOccurred();
        assert curNum + 1 == discounts.getValue().size();
    }

    @Test
    @DisplayName("add composite discount")
    @Order(7)
    public void addCompositeDiscount() {
        DiscountTypeWrapper discountTypeWrapper = DiscountTypeWrapper.createDiscountTypeWrapper(maxCompositeDiscount);
        ResponseT<List<DiscountTypeWrapper>> discounts = this.getDiscountTypesOfShop(shopOwnerName, shopName);
        assert !discounts.isErrorOccurred();
        int curNum = discounts.getValue().size();
        this.addDiscountToShop(discountTypeWrapper, shopName, shopOwnerName);
        discounts = this.getDiscountTypesOfShop(shopOwnerName, shopName);
        assert !discounts.isErrorOccurred();
        assert curNum + 1 == discounts.getValue().size();
    }


    @Test
    @DisplayName("delete composite discount")
    @Order(8)
    public void deleteCompositeDiscount() {
        ResponseT<List<DiscountTypeWrapper>> discounts = this.getDiscountTypesOfShop(shopOwnerName, shopName);
        assert !discounts.isErrorOccurred();

        // save current amount
        int currAmount = discounts.getValue().size();
        Response response = this.removeDiscountFromShop(DiscountTypeWrapper.createDiscountTypeWrapper(this.maxCompositeDiscount), shopName
                , shopOwnerName);
        assert !response.isErrorOccurred();
        discounts = this.getDiscountTypesOfShop(shopOwnerName, shopName);
        assert !discounts.isErrorOccurred();
        // should only delete 1
        Assertions.assertEquals( currAmount - 1, discounts.getValue().size());
        //return to prev state
        Response returnState = this.addDiscountToShop(DiscountTypeWrapper.createDiscountTypeWrapper(this.maxCompositeDiscount), shopName
                , shopOwnerName);
        assert !returnState.isErrorOccurred();
        discounts = this.getDiscountTypesOfShop(shopOwnerName, shopName);
        assert !discounts.isErrorOccurred();
        // return to prev status
        assert discounts.getValue().size() == currAmount;
        response = this.removeDiscountFromShop(DiscountTypeWrapper.createDiscountTypeWrapper(this.maxCompositeDiscount), shopName
                , shopOwnerName);
        assert !response.isErrorOccurred();
    }

    @Test
    @DisplayName("check decrease price by discount")
    @Order(9)
    public void discountPriceValid() {
        // price is already 50% on yogurt by discount
        try {
            VisitorFacade visitor = guestLogin();
            addItemToCart(yogurt, 4, visitor.getName());
            ResponseT<ShoppingCartFacade> cart = this.showShoppingCart(visitor.getName());
            double expected = yogurt.getPrice() * 2;
            assert expected == cart.getValue().getPrice();
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("add policy - simple")
    @Order(10)
    public void addPolicyTest() {
        ResponseT<List<PurchasePolicyTypeWrapper>> policies = this.getPurchasePoliciesOfShop(shopOwnerName, shopName);
        assert !policies.isErrorOccurred();
        PurchasePolicyTypeWrapper purchasePolicyWrapper = PurchasePolicyTypeWrapper.createPurchasePolicyWrapper(atLeastOnePolicy);
        Response response = this.addPurchasePolicyToShop(purchasePolicyWrapper, shopName, shopOwnerName);
        int curNum = policies.getValue().size();
        assert !response.isErrorOccurred();
        ResponseT<List<PurchasePolicyTypeWrapper>> newPolicies = this.getPurchasePoliciesOfShop(shopOwnerName, shopName);
        assert !newPolicies.isErrorOccurred();
        assert curNum + 1 == newPolicies.getValue().size();
    }

    @Test
    @DisplayName("add policy - composite")
    @Order(11)
    public void addPolicyCompositeTest() {
        ResponseT<List<PurchasePolicyTypeWrapper>> policies = this.getPurchasePoliciesOfShop(shopOwnerName, shopName);
        assert !policies.isErrorOccurred();
        PurchasePolicyTypeWrapper purchasePolicyWrapper = PurchasePolicyTypeWrapper.createPurchasePolicyWrapper(compositePolicy);
        Response response = this.addPurchasePolicyToShop(purchasePolicyWrapper, shopName, shopOwnerName);
        int curNum = policies.getValue().size();
        assert !response.isErrorOccurred();
        ResponseT<List<PurchasePolicyTypeWrapper>> newPolicies = this.getPurchasePoliciesOfShop(shopOwnerName, shopName);
        assert !newPolicies.isErrorOccurred();
        assert curNum + 1 == newPolicies.getValue().size();
    }

    @Test
    @DisplayName("remove composite policy ")
    @Order(12)
    public void removePolicyCompositeTest() {
        ResponseT<List<PurchasePolicyTypeWrapper>> policies = this.getPurchasePoliciesOfShop(shopOwnerName, shopName);
        assert !policies.isErrorOccurred();
        PurchasePolicyTypeWrapper purchasePolicyWrapper = PurchasePolicyTypeWrapper.createPurchasePolicyWrapper(compositePolicy);
        Response response = this.removePurchasePolicyFromShop(purchasePolicyWrapper, shopName, shopOwnerName);
        int curNum = policies.getValue().size();
        assert !response.isErrorOccurred();
        ResponseT<List<PurchasePolicyTypeWrapper>> newPolicies = this.getPurchasePoliciesOfShop(shopOwnerName, shopName);
        assert !newPolicies.isErrorOccurred();
        assert curNum - 1 == newPolicies.getValue().size();
        //return to prev state
        Response returnState = this.addPurchasePolicyToShop(purchasePolicyWrapper, shopName, shopOwnerName);
        assert !returnState.isErrorOccurred();
        policies = this.getPurchasePoliciesOfShop(shopOwnerName, shopName);
        assert !policies.isErrorOccurred();
        assert policies.getValue().size() == curNum;
    }

    @Test
    @DisplayName("check policy prevents")
    @Order(13)
    public void policyPreventTest() {
        // already set as demanding for atleast one
        try {
            VisitorFacade visitor = guestLogin();
            addItemToCart(apple, 1, visitor.getName());
            ResponseT<ShoppingCartFacade> cart = showShoppingCart(visitor.getName());
            // check it doesn't prevent from adding to cart'
            assert cart.getValue().getCart().size() > 0;
            Response response = buyShoppingCart(visitor.getName(), cart.getValue().getPrice() , creditCard, address);
            // should fail cause no yogurt.category has been added
            assert response.isErrorOccurred();
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("check policy allows")
    @Order(14)
    public void policyAllowsTest() {
        // already set as demanding for atleast one
        try {
            VisitorFacade visitor = guestLogin();
            addItemToCart(yogurt, 1, visitor.getName());
            addItemToCart ( apple, 1, visitor.getName () );
            ResponseT<ShoppingCartFacade> cart = showShoppingCart(visitor.getName());
            // check it doesn't prevent from adding to cart'
            assert cart.getValue().getCart().size() > 0;
            Response response = buyShoppingCart(visitor.getName(), cart.getValue().getPrice(), creditCard, address);
            assert !response.isErrorOccurred();
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("check override works")
    @Order(15)
    public void overridePolicy() {
        try {
            // setting new shop
            VisitorFacade visitor = guestLogin();
            String memberName = "idoPolicyTest";
            String password = "password";
            register(memberName, password);
            ResponseT<List<String>> questions = memberLogin(memberName, password);
            MemberFacade member = validateSecurityQuestions(memberName, new ArrayList<>(), visitor.getName()).getValue();
            String shopName = "policyTest";
            openShop(memberName, shopName);
            addItemToShop(memberName, "computer", 50, Item.Category.electricity, "good", new ArrayList<>(), 3, shopName);
            String kotegName = "Koteg";
            addItemToShop(memberName, kotegName, 30, Item.Category.general, "good", new ArrayList<>(), 3, shopName);
            ItemFacade koteg = searchProductByName(kotegName).get(0);
            CategoryPurchasePolicyLevelState categoryPurchasePolicyLevelState = new CategoryPurchasePolicyLevelState(Item.Category.electricity);
            AtLeastPurchasePolicyType atLeastPurchasePolicyType = new AtLeastPurchasePolicyType(categoryPurchasePolicyLevelState, 2);
            // add simple  policy
            addPurchasePolicyToShop(PurchasePolicyTypeWrapper.createPurchasePolicyWrapper(atLeastPurchasePolicyType), shopName, memberName);
            ResponseT<List<PurchasePolicyTypeWrapper>> purchasePoliciesOfShop = getPurchasePoliciesOfShop(memberName, shopName);
            assert purchasePoliciesOfShop.getValue().size() == 1;
            // create composite policy
            PurchasePolicyLevelState kotegAtMost = new ItemPurchasePolicyLevelState(koteg.getId());
            PurchasePolicyType atMostPolicy = new AtMostPurchasePolicyType(kotegAtMost, 2);
            List<PurchasePolicyType> policies = new ArrayList<>();
            policies.add(atLeastPurchasePolicyType);
            policies.add(atMostPolicy);
            PurchasePolicyType composite = new OrCompositePurchasePolicyType(policies);
            // add composite policy
            addPurchasePolicyToShop(PurchasePolicyTypeWrapper.createPurchasePolicyWrapper(composite), shopName, memberName);
            purchasePoliciesOfShop = getPurchasePoliciesOfShop(memberName, shopName);
            assert purchasePoliciesOfShop.getValue().size() == 1;
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("check discount condition works")
    @Order(16)
    public void conditionalDiscountTest() {
        // remove the simple discount, stays only with the composite discount
        try {
            removeDiscountFromShop(DiscountTypeWrapper.createDiscountTypeWrapper(simpleDiscountYogurt), shopName, shopOwnerName);
            addDiscountToShop ( DiscountTypeWrapper.createDiscountTypeWrapper (maxCompositeDiscount), shopName, shopOwnerName );
            VisitorFacade visitor = guestLogin();
            addItemToCart(apple, 1, visitor.getName());
            ShoppingCartFacade cart = showShoppingCart(visitor.getName()).getValue();
            // check whether condition discount didn't count (must have atleast 2 apples)
            Assertions.assertEquals(cart.getPrice(), applePrice * 0.75);
            addItemToCart(apple, 2, visitor.getName());
            cart = showShoppingCart(visitor.getName()).getValue();
            Assertions.assertEquals (cart.getPrice(), applePrice * 3 * 0.7);
            // return to base state
            Response response = addDiscountToShop(DiscountTypeWrapper.createDiscountTypeWrapper(simpleDiscountYogurt), shopName, shopOwnerName);
            assert !response.isErrorOccurred();
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("remove appointment base")
    @Order(17)
    public void removeAppointmentBase() {
        try {
            assert !setupFailed;
            // A appoints B.
            int curNum = getShopEmployeesInfo(ownerA.getName(), removingShop.getShopName()).size();
            assert !appointShopOwner(ownerA.getName(), ownerB.getName(), removingShop.getShopName()).isErrorOccurred();
            assert curNum + 1 == getShopEmployeesInfo(ownerA.getName(), removingShop.getShopName()).size();
            // A removing B
            assert !removeShopOwnerAppointment(ownerA.getName(), ownerB.getName(), removingShop.getShopName()).isErrorOccurred();
            assert curNum == getShopEmployeesInfo(ownerA.getName(), removingShop.getShopName()).size();
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("remove not self appointment")
    @Order(18)
    public void removeOtherAppointed() {
        try {
            assert !setupFailed;
            // A appoints B.
            int curNum = getShopEmployeesInfo(ownerA.getName(), removingShop.getShopName()).size();
            assert !appointShopOwner(ownerA.getName(), ownerB.getName(), removingShop.getShopName()).isErrorOccurred();
            assert curNum + 1 == getShopEmployeesInfo(ownerA.getName(), removingShop.getShopName()).size();
            // B appoints C
            assert !appointShopOwner(ownerB.getName(), ownerC.getName(), removingShop.getShopName()).isErrorOccurred();
            approveAppointment(ownerA.getName(),ownerC.getName(),removingShop.getShopName());
            assert curNum + 2 == getShopEmployeesInfo(ownerA.getName(), removingShop.getShopName()).size();

            // C removing B
            assert removeShopOwnerAppointment(ownerC.getName(), ownerB.getName(), removingShop.getShopName()).isErrorOccurred();
            assert curNum + 2 == getShopEmployeesInfo(ownerA.getName(), removingShop.getShopName()).size();
        } catch (Exception e) {
            assert false;
        }
    }

    @Test
    @DisplayName("remove recursive appointment")
    @Order(19)
    public void removeRecAppointment() {
        try {
            assert !setupFailed;
            // B and C already appointed
            // A removing B-> should remove B and C as well
            assert !removeShopOwnerAppointment(ownerA.getName(), ownerB.getName(), removingShop.getShopName()).isErrorOccurred();
            Assertions.assertEquals(1 , getShopEmployeesInfo(ownerA.getName(), removingShop.getShopName()).size());
        } catch (Exception e) {
            assert false;
        }
    }
}

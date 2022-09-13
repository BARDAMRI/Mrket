package com.example.server.serviceLayer;


import com.example.server.businessLayer.Market.Acquisition;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.CompositeDiscount.MaxCompositeDiscount;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.Condition.AmountOfItemCondition;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.Condition.CompositionCondition.AndCompositeCondition;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.Condition.CompositionCondition.OrCompositeCondition;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.Condition.Condition;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.Condition.PriceCondition;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.ConditionalDiscount;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.DiscountState.*;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.DiscountType;
import com.example.server.businessLayer.Market.Policies.PurchasePolicy.*;
import com.example.server.businessLayer.Market.Policies.PurchasePolicy.PurchasePolicyState.*;
import com.example.server.businessLayer.Market.ResourcesObjects.ErrorLog;
import com.example.server.businessLayer.Market.Appointment.Appointment;
import com.example.server.businessLayer.Payment.PaymentService;
import com.example.server.businessLayer.Publisher.Publisher;
import com.example.server.businessLayer.Supply.SupplyService;
import com.example.server.businessLayer.Market.Item;
import com.example.server.businessLayer.Market.Market;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.businessLayer.Market.Shop;
import com.example.server.serviceLayer.FacadeObjects.*;
import com.example.server.serviceLayer.FacadeObjects.PolicyFacade.*;
import com.example.server.serviceLayer.FacadeObjects.PolicyFacade.Wrappers.*;
import org.springframework.boot.ansi.Ansi8BitColor;


import java.util.ArrayList;
import java.util.List;

public class MarketService {
    private static MarketService marketService = null;
    private Market market;

    private MarketService() {
        market = Market.getInstance();
    }

    public synchronized static MarketService getInstance() {
        if (marketService == null) {
            marketService = new MarketService();
        }
        return marketService;
    }

    public Response firstInitMarket(String userName, String password) {
        try {
            market.firstInitMarket(userName, password);
            return new Response();
        } catch (MarketException e) {
            return new Response(e.getMessage());
        } catch (Exception e) {
            ErrorLog.getInstance().Log(e.getMessage());
            return new Response(e.getMessage());
        }
    }

    public ResponseT<List<ItemFacade>> searchProductByName(String name) {
        ResponseT<List<ItemFacade>> toReturn;
        try {
            List<Item> items = market.getItemByName(name);
            List<ItemFacade> facadeItems = new ArrayList<>();
            for (Item item : items) {
                facadeItems.add(new ItemFacade(item));
            }
            toReturn = new ResponseT<>(facadeItems);
        } catch (Exception e) {
            ErrorLog.getInstance().Log(e.getMessage());
            toReturn = new ResponseT<>(e.getMessage());
        }


        return toReturn;
    }

    public ResponseT<List<ItemFacade>> searchProductByCategory(Item.Category category) {
        ResponseT<List<ItemFacade>> toReturn;
        try {
            List<Item> items = market.getItemByCategory(category);
            List<ItemFacade> facadeItems = new ArrayList<>();
            for (Item item : items) {
                facadeItems.add(new ItemFacade(item));
            }
            toReturn = new ResponseT<>(facadeItems);
        } catch (Exception e) {
            ErrorLog.getInstance().Log(e.getMessage());
            toReturn = new ResponseT<>(e.getMessage());
        }
        return toReturn;
    }

    public ResponseT<List<ItemFacade>> searchProductByKeyword(String keyWord) {
        ResponseT<List<ItemFacade>> toReturn;
        try {
            List<Item> items = market.getItemsByKeyword(keyWord);
            List<ItemFacade> facadeItems = new ArrayList<>();
            for (Item item : items) {
                facadeItems.add(new ItemFacade(item));
            }
            toReturn = new ResponseT<>(facadeItems);
        } catch (Exception e) {
            ErrorLog.getInstance().Log(e.getMessage());
            toReturn = new ResponseT<>(e.getMessage());
        }
        return toReturn;
    }

    public ResponseT<List<ItemFacade>> filterItemByPrice(List<ItemFacade> items, double minPrice, double maxPrice) {
        List<Item> businessItems = new ArrayList<>();
        for (ItemFacade item : items) {
            try {
                businessItems.add(item.toBusinessObject());
            } catch (MarketException e) {
                return new ResponseT<>(e.getMessage());

            }
        }
        ResponseT<List<ItemFacade>> toReturn;
        try {
            List<Item> filteredItems = market.filterItemsByPrice(businessItems, minPrice, maxPrice);
            List<ItemFacade> facadeItems = new ArrayList<>();
            for (Item item : filteredItems) {
                facadeItems.add(new ItemFacade(item));
            }
            toReturn = new ResponseT<>(facadeItems);
        } catch (Exception e) {
            ErrorLog.getInstance().Log(e.getMessage());
            toReturn = new ResponseT<>(e.getMessage());
        }
        return toReturn;
    }

    public ResponseT<List<ItemFacade>> filterItemByCategory(List<ItemFacade> items, Item.Category category) {
        List<Item> businessItems = new ArrayList<>();
        for (ItemFacade item : items) {
            try {
                businessItems.add(item.toBusinessObject());
            } catch (MarketException e) {
                return new ResponseT<>(e.getMessage());
            }
        }
        ResponseT<List<ItemFacade>> toReturn;
        try {
            List<Item> filteredItems = market.filterItemsByCategory(businessItems, category);
            List<ItemFacade> facadeItems = new ArrayList<>();
            for (Item item : filteredItems) {
                facadeItems.add(new ItemFacade(item));
            }
            toReturn = new ResponseT<>(facadeItems);
        } catch (Exception e) {
            ErrorLog.getInstance().Log(e.getMessage());
            toReturn = new ResponseT<>(e.getMessage());
        }
        return toReturn;
    }

    public Response openNewShop(String visitorName, String shopName) {
        try {
            market.openNewShop(visitorName, shopName);
            return new Response();
        } catch (MarketException marketException) {
            return new Response(marketException.getMessage());
        } catch (Exception e) {
            ErrorLog.getInstance().Log(e.getMessage());
            return new Response(e.getMessage());
        }
    }

    public Response updateShopItemAmount(String shopOwnerName, ItemFacade item, double amount, String shopName) {
        try {
            Item itemBL = new Item(item.getId(), item.getName(), item.getPrice(), item.getInfo(), item.getCategory(), item.getKeywords());
            market.setItemCurrentAmount(shopOwnerName, itemBL, amount, shopName);
            return new Response();
        } catch (MarketException e) {
            return new Response(e.getMessage());
        } catch (Exception e) {
            ErrorLog.getInstance().Log(e.getMessage());
            return new Response(e.getMessage());
        }
    }


    public Response removeItemFromShop(String shopOwnerName, ItemFacade item, String shopName) {
        try {
            market.removeItemFromShop(shopOwnerName, item.getId(), shopName);
            return new Response();
        } catch (MarketException e) {
            return new Response(e.getMessage());
        } catch (Exception e) {
            ErrorLog.getInstance().Log(e.getMessage());
            return new Response(e.getMessage());
        }
    }


    public ResponseT<ShopFacade> addItemToShop(String shopOwnerName, String name, double price, Item.Category category, String info,
                                               List<String> keywords, double amount, String shopName) {
        ResponseT<ShopFacade> response;
        try {
            Shop shop = market.addItemToShop(shopOwnerName, name, price, category, info, keywords, amount, shopName);
            response = new ResponseT(new ShopFacade(shop));
        } catch (MarketException e) {
            response = new ResponseT(e.getMessage());
        } catch (Exception e) {
            ErrorLog.getInstance().Log(e.getMessage());
            response = new ResponseT<>(e.getMessage());
        }
        return response;

    }

    public Response setItemCurrentAmount(String shopOwnerName, ItemFacade item, double amount, String shopName) {
        try {
            Item itemBL = new Item(item.getId(), item.getName(), item.getPrice(), item.getInfo(), item.getCategory(), item.getKeywords());
            market.setItemCurrentAmount(shopOwnerName, itemBL, amount, shopName);
            return new Response();
        } catch (MarketException e) {
            return new Response(e.getMessage());
        } catch (Exception e) {
            ErrorLog.getInstance().Log(e.getMessage());
            return new Response(e.getMessage());
        }
    }

    public Response changeShopItemInfo(String shopOwnerName, String info, ItemFacade oldItem, String shopName) {
        try {
            market.changeShopItemInfo(shopOwnerName, info, oldItem.getId(), shopName);
            return new Response();
        } catch (MarketException e) {
            return new Response(e.getMessage());
        } catch (Exception e) {
            ErrorLog.getInstance().Log(e.getMessage());
            return new Response(e.getMessage());
        }
    }

    public Response editItem(ItemFacade newItem, String id) {
        try {
            market.editItem(newItem.toBusinessObject(), id);
            return new Response();
        } catch (MarketException e) {
            return new Response(e.getMessage());
        } catch (Exception e) {
            ErrorLog.getInstance().Log(e.getMessage());
            return new Response(e.getMessage());
        }
    }

    public Response closeShop(String shopOwnerName, String shopName) {
        Response response;
        try {
            market.closeShop(shopOwnerName, shopName);
            response = new Response();
        } catch (MarketException e) {
            response = new Response(e.getMessage());
        } catch (Exception e) {
            ErrorLog.getInstance().Log(e.getMessage());
            response = new Response(e.getMessage());
        }
        return response;
    }

    public ResponseT<List<AppointmentFacade>> getShopEmployeesInfo(String shopManagerName, String shopName) {
        ResponseT<List<AppointmentFacade>> toReturn;
        try {
            List<Appointment> employees = market.getShopEmployeesInfo(shopManagerName, shopName).values().stream().toList();
            List<AppointmentFacade> employeesFacadeList = new ArrayList<>();
            for (Appointment appointment : employees) {
                AppointmentFacade employeeFacade;
                if (appointment.isOwner()) {
                    //employeeFacade = new ShopOwnerAppointmentFacade((ShopOwnerAppointment) appointment);
                    employeeFacade = new ShopOwnerAppointmentFacade();
                    employeeFacade = employeeFacade.toFacade(appointment);
                } else {
                    employeeFacade = new ShopManagerAppointmentFacade();
                    employeeFacade = employeeFacade.toFacade(appointment);
                }
                employeesFacadeList.add(employeeFacade);
            }
            return new ResponseT<>(employeesFacadeList);
        } catch (Exception e) {
            toReturn = new ResponseT<>(e.getMessage());
        }
        return toReturn;
    }

    /**
     * relevant to shop manager
     *
     * @param shopManagerName
     * @param shopName
     * @return
     */
    public ResponseT<String> getShopPurchaseHistory(String shopManagerName, String shopName) {
        try {
            String history = market.getShopPurchaseHistory(shopManagerName, shopName).toString();
            ResponseT<String> res = new ResponseT<>(null);
            res.setValue(history);
            return res;

        } catch (Exception e) {
            ErrorLog.getInstance().Log(e.getMessage());
            return new ResponseT<>(e.getMessage());
        }
    }


    /**
     * relevant to system manager
     *
     * @param systemManagerName
     * @return
     */
    public ResponseT<String> getAllSystemPurchaseHistory(String systemManagerName) {
        try {
            String history = market.getAllSystemPurchaseHistory(systemManagerName).toString();
            ResponseT<String> res = new ResponseT<>(null);
            res.setValue(history);
            return res;
        } catch (Exception e) {
            ErrorLog.getInstance().Log(e.getMessage());
            return new ResponseT<>(e.getMessage());
        }
    }


    /**
     * relevant to system manager
     *
     * @param systemManagerName
     * @param shopName
     * @return
     */
    public ResponseT<String> getHistoryByShop(String systemManagerName, String shopName) {
        try {
            String history = market.getHistoryByShop(systemManagerName, shopName).toString();
            ResponseT<String> res =  new ResponseT<>(null);
            res.setValue(history);
            return res;
        } catch (Exception e) {
            ErrorLog.getInstance().Log(e.getMessage());
            return new ResponseT<>(e.getMessage());
        }
    }


    public ResponseT<String> getHistoryByMember(String systemManagerName, String memberName) {
        try {
            String history = market.getHistoryByMember(systemManagerName, memberName).toString();
            ResponseT<String> res =  new ResponseT<>(null);
            res.setValue(history);
            return res;
        } catch (Exception e) {
            ErrorLog.getInstance().Log(e.getMessage());
            return new ResponseT<>(e.getMessage());
        }
    }

    public ResponseT<ShopFacade> getShopInfo(String member, String shopName) {
        ResponseT<ShopFacade> toReturn;
        try {
            Shop shop = market.getShopInfo(member, shopName);
            toReturn = new ResponseT<>(new ShopFacade(shop));
        } catch (Exception e) {
            ErrorLog.getInstance().Log(e.getMessage());
            toReturn = new ResponseT<>(e.getMessage());
        }
        return toReturn;
    }

    public Response removeShopOwnerAppointment(String boss, String firedAppointed, String shopName) {
        Response response;
        try {
            market.removeShopOwnerAppointment(boss, firedAppointed, shopName);
            response = new Response();
        } catch (Exception e) {
            ErrorLog.getInstance().Log(e.getMessage());
            response = new Response(e.getMessage());
        }
        return response;

    }

    public Response removeMember(String manager, String memberToRemove) {
        Response response;
        try {
            market.removeMember(manager, memberToRemove);
            response = new Response();
        } catch (MarketException e) {
            ErrorLog.getInstance().Log(e.getMessage());
            response = new Response(e.getMessage());
        }
        return response;
    }

    public ResponseT<ItemFacade> getItemInfo(String name, int itemId) {
        try {
            Item item = market.getItemById(name, itemId);
            return new ResponseT<>(new ItemFacade(item));
        } catch (Exception e) {
            ErrorLog.getInstance().Log(e.getMessage());
            return new ResponseT<>(e.getMessage());
        }
    }

    public ResponseT<ItemFacade> getItemById(int id) {
        try {
            Item item = market.getItemByID(id);
            return new ResponseT<>(new ItemFacade(item));
        } catch (Exception e) {
            ErrorLog.getInstance().Log(e.getMessage());
            return new ResponseT<>(e.getMessage());
        }
    }

    public ResponseT<String> getMarketInfo(String sysManager) {
        try {
            String marketInfo = market.getMarketInfo(sysManager);
            ResponseT<String> res = new ResponseT<>(null);
            res.setValue(marketInfo);
            return res;

        } catch (Exception e) {
            ErrorLog.getInstance().Log(e.getMessage());
            return new ResponseT<>(e.getMessage());
        }
    }

    public Response addDiscountToShop(String visitorName, String shopName, DiscountTypeWrapper discountTypeWrapper) {
        try {
            DiscountType discountType = discountTypeWrapper.toBusinessObject();
            market.addDiscountToShop(visitorName, shopName, discountType);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response removeDiscountFromShop(String visitorName, String shopName, DiscountTypeWrapper discountTypeWrapper) {
        try {
            DiscountType discountType = discountTypeWrapper.toBusinessObject();
            market.removeDiscountFromShop(visitorName, shopName, discountType);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response addPurchasePolicyToShop(String visitorName, String shopName, PurchasePolicyTypeWrapper purchasePolicyTypeWrapper) {
        try {
            PurchasePolicyType purchasePolicyType = purchasePolicyTypeWrapper.toBusinessObject();
            market.addPurchasePolicyToShop(visitorName, shopName, purchasePolicyType);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response removePurchasePolicyFromShop(String visitorName, String shopName, PurchasePolicyTypeWrapper purchasePolicyTypeWrapper) {
        try {
            PurchasePolicyType purchasePolicyType = purchasePolicyTypeWrapper.toBusinessObject();
            market.removePurchasePolicyFromShop(visitorName, shopName, purchasePolicyType);
            return new Response();
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response isServerInit() {
        try {
            if (market.isInit()) {
                return new Response();
            } else {
                return new Response("server has not yet been initialized ");
            }
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response setPaymentService(PaymentService o, String managerName) {
        try {
            if (market.setPaymentService(o, managerName)) {
                return new Response();
            } else {
                return new Response("Failed to set service");
            }
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }
    public Response setPaymentServiceAddress(String o, String managerName) {
        try {
            if (market.setPaymentServiceAddress(o, managerName)) {
                return new Response();
            } else {
                return new Response("Failed to set service");
            }
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }
    public Response setSupplyService(SupplyService o, String managerName) {
        try {
            if (market.setSupplyService(o, managerName)) {
                return new Response();
            } else {
                return new Response("Failed to set service");
            }
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public Response setPublishService(Publisher o, String managerName) {
        try {
            if (market.setPublishService(o, managerName)) {
                return new Response();
            } else {
                return new Response("Failed to set service");
            }
        } catch (Exception e) {
            return new Response(e.getMessage());
        }
    }

    public ResponseT<List<PurchasePolicyTypeWrapper>> getPurchasePoliciesOfShop(String visitorName, String shopName) {
        try {
            List<PurchasePolicyType> purchasePolicyTypes = market.getPurchasePoliciesOfShop(visitorName, shopName);
            List<PurchasePolicyTypeWrapper> purchasePolicyTypeWrappers = new ArrayList<>();
            for (PurchasePolicyType purchasePolicyType : purchasePolicyTypes)
                purchasePolicyTypeWrappers.add(PurchasePolicyTypeWrapper.createPurchasePolicyWrapper(purchasePolicyType));
            return new ResponseT(purchasePolicyTypeWrappers);
        } catch (Exception e) {
            return new ResponseT(e.getMessage());
        }
    }
    public ResponseT<List<String>> getMyPendingApps(String ownerName, String shopName) {
        try {
            List<String> res = market.getMyPendingAppointmentsToApprove(shopName,ownerName);
            return new ResponseT<>(res);
        } catch (MarketException e) {
            return new ResponseT<>(e.getMessage());
        }
    }


    public ResponseT<List<DiscountTypeWrapper>> getDiscountTypesOfShop(String visitorName, String shopName) {
        try {
            List<DiscountType> discountTypeList = market.getDiscountTypesOfShop(visitorName, shopName);
            List<DiscountTypeWrapper> discountTypeWrappers = new ArrayList<>();
            for (DiscountType discountType : discountTypeList)
                discountTypeWrappers.add(DiscountTypeWrapper.createDiscountTypeWrapper(discountType));
            return new ResponseT(discountTypeWrappers);
        } catch (Exception e) {
            return new ResponseT(e.getMessage());
        }
    }



    public Response addABid(String visitorName, String shopName, Integer itemId, Double price, Double amount) {
        try {
            market.addABid(visitorName, shopName, itemId, price, amount);
            return new Response ();
        }catch (Exception e){
            return new Response ( e.getMessage () );
        }
    }

    public Response approveABid(String approves, String shopName, String askedBy, Integer itemId) {
        try {
            market.approveABid(approves, shopName, askedBy, itemId);
            return new Response ();
        }catch (Exception e){
            return new Response ( e.getMessage () );
        }
    }

    public Response suggestNewOfferToBid(String suggester, String shopName, String askedBy, int itemId, double newPrice) {
        try {
            market.suggestNewOfferToBid(suggester, shopName, askedBy, itemId, newPrice);
            return new Response ();
        }catch (Exception e){
            return new Response ( e.getMessage () );
        }
    }

    public Response rejectABid(String opposed, String shopName, String buyer, int itemId) {
        try {
            market.rejectABid(opposed, shopName,buyer, itemId);
            return new Response ();
        }catch (Exception e){
            return new Response ( e.getMessage () );
        }
    }

    public Response cancelABid(String shopName, String buyer, int itemId) {
        try {
            market.cancelABid (shopName, buyer, itemId);
            return new Response ();
        }catch (Exception e){
            return new Response ( e.getMessage () );
        }
    }

    public Response approveAppointment(String ownerName, String appointedName, String shopName) {
        try {
            market.approveAppointment(shopName,appointedName,ownerName);
            return new Response();
        } catch (MarketException e) {
            return new Response(e.getMessage());
        }
    }

    public Response rejectAppointment(String ownerName, String appointedName, String shopName) {
        try {
            market.rejectAppointment(shopName,appointedName,ownerName);
            return new Response();
        } catch (MarketException e) {
            return new Response(e.getMessage());
        }
    }


    public ResponseT<Boolean> isSystemManager(String name) {
        if (market.isSystemManager(name))
            return new ResponseT<>(true);
            else return new ResponseT<>(false);
    }

    public Response reOpenClosedShop(String shopName, String ownerName) {
        try{
            this.market.reopenClosedShop(shopName,ownerName);
            return new Response();
        } catch (MarketException e) {
            return new Response(e.getMessage());
        }catch (Exception e){
            return new Response("server error has occurred, please try again later");
        }
    }

    public ResponseT<List<String>> approveOrRejectBatch(String shopName, String ownerName, List<String> appointedNames, boolean approve) {
        ResponseT<List<String>> res;
        try {
            List<String> failed = market.approveOrRejectBatch(shopName,ownerName,appointedNames,approve);
            res= new ResponseT<>(failed);
        } catch (MarketException e) {
            return new ResponseT<>(e.getMessage());
        }
        return res;

    public ResponseT<List<AcquisitionFacade>> getAcqsForMember(String memberName) {
        List<Acquisition> acqs= null;
        try {
            acqs = market.getAcqsForMember(memberName);
        } catch (MarketException e) {
            return new ResponseT(e.getMessage());
        }
        List<AcquisitionFacade> acquisitionFacades = new ArrayList<>();
        for (Acquisition acq:acqs)
        {
            AcquisitionFacade acquisitionFacade = new AcquisitionFacade(acq);
            acquisitionFacades.add(acquisitionFacade);
        }
        return new ResponseT<>(acquisitionFacades);
    }
}

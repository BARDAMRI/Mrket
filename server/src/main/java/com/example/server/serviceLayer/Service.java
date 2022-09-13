package com.example.server.serviceLayer;

import com.example.server.businessLayer.Market.Item;
import com.example.server.serviceLayer.FacadeObjects.*;
import com.example.server.serviceLayer.FacadeObjects.PolicyFacade.Wrappers.DiscountTypeWrapper;
import com.example.server.serviceLayer.FacadeObjects.PolicyFacade.Wrappers.PurchasePolicyTypeWrapper;
import com.example.server.serviceLayer.Requests.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
public class Service implements IService {
    private static Service service = null;
    MarketService marketService;
    PurchaseService purchaseService;
    UserService userService;


    protected Service() {
        marketService = MarketService.getInstance();
        purchaseService = PurchaseService.getInstance();
        userService = UserService.getInstance();
    }

    public synchronized static Service getInstance() {
        if (service == null)
            service = new Service();
        return service;
    }

    @Override
    @RequestMapping(value = "/firstInitMarket")
    @CrossOrigin
    public Response firstInitMarket(@RequestBody InitMarketRequest request) {
        return marketService.firstInitMarket(request.getUserName(), request.getPassword());
    }

    @Override
    @RequestMapping(value = "/guestLogin")
    @CrossOrigin
    public ResponseT<VisitorFacade> guestLogin() {
        return this.userService.guestLogin();
    }


    @Override
    @RequestMapping(value = "/exitSystem")
    @CrossOrigin
    public Response exitSystem(@RequestBody ExitSystemRequest request) {
        return this.userService.exitSystem(request.getVisitorName());
    }

    @Override
    @RequestMapping(value = "/register")
    @CrossOrigin
    public ResponseT<Boolean> register(@RequestBody NamePasswordRequest request) {
        return userService.register(request.getName(), request.getPassword());
    }

    @Override
    @RequestMapping(value = "/addPersonalQuery")
    @CrossOrigin
    public Response addPersonalQuery(@RequestBody AddPersonalQueryRequest request) {
        return userService.addPersonalQuery(request.getUserAdditionalQueries(), request.getUserAdditionalAnswers(), request.getMember());
    }


    @Override
    @RequestMapping(value = "/searchProductByName")
    @CrossOrigin
    public ResponseT<List<ItemFacade>> searchProductByName(@RequestBody SearchProductByNameRequest request) {
        return marketService.searchProductByName(request.getProductName());
    }

    @Override
    @RequestMapping(value = "/searchProductByCategory")
    @CrossOrigin
    public ResponseT<List<ItemFacade>> searchProductByCategory(@RequestBody Item.Category category) {
        return marketService.searchProductByCategory(category);
    }

    @Override
    @RequestMapping(value = "/searchProductByKeyword")
    @CrossOrigin
    public ResponseT<List<ItemFacade>> searchProductByKeyword(@RequestBody SearchProductByNameRequest request) {
        return marketService.searchProductByKeyword(request.getProductName());
    }

    @Override
    @RequestMapping(value = "/filterItemByCategory")
    @CrossOrigin
    public ResponseT<List<ItemFacade>> filterItemByCategory(@RequestBody FilterItemByCategoryRequest request) {
        return marketService.filterItemByCategory(request.getItems(), request.getCategory());
    }

    @Override
    @RequestMapping(value = "/filterItemByPrice")
    @CrossOrigin
    public ResponseT<List<ItemFacade>> filterItemByPrice(@RequestBody FilterItemByPriceRequest request) {
        return marketService.filterItemByPrice(request.getItems(), request.getMinPrice(), request.getMaxPrice());
    }

    @Override
    @RequestMapping(value = "/addItemToShoppingCart")
    @CrossOrigin
    public Response addItemToShoppingCart(@RequestBody AddItemToShoppingCartRequest request) {
        return purchaseService.addItemToShoppingCart(request.getItemToInsert(), request.getAmount(), request.getVisitorName());
    }
    @Override
    @RequestMapping(value = "/showShoppingCart")
    @CrossOrigin
    public ResponseT<ShoppingCartFacade> showShoppingCart(@RequestBody RequestVisitorName request) {
        return purchaseService.showShoppingCart(request.getName());
    }

    @Override
    @RequestMapping(value = "/editItemFromShoppingCart")
    @CrossOrigin
    public Response editItemFromShoppingCart(@RequestBody EditItemFromShoppingCartRequest request) {
        return purchaseService.editItemFromShoppingCart(request.getAmount(), request.getItemFacade(),
                request.getShopName(), request.getVisitorName());
    }


    @Override
    @RequestMapping(value = "/calculateShoppingCart")
    @CrossOrigin
    public ResponseT<ShoppingCartFacade> calculateShoppingCart(@RequestBody RequestVisitorName request) {
        return purchaseService.calculateShoppingCart(request.getName());
    }

    @Override
    @RequestMapping(value = "/buyShoppingCart")
    @CrossOrigin
    public Response buyShoppingCart(@RequestBody BuyShoppingCartRequest request) {
        return this.purchaseService.buyShoppingCart(request.getVisitorName(), request.getExpectedPrice(),
                request.getPaymentMethod(), request.getAddress());
    }

    @Override
    @RequestMapping(value = "/memberLogin")
    @CrossOrigin
    public ResponseT<List<String>> memberLogin(@RequestBody NamePasswordRequest request) {
        return userService.memberLogin(request.getName(), request.getPassword());
    }

    @Override
    @RequestMapping(value = "/validateSecurityQuestions")
    @CrossOrigin
    public ResponseT<MemberFacade> validateSecurityQuestions(@RequestBody ValidateSecurityRequest request) {
        return userService.validateSecurityQuestions(request.getUserName(), request.getAnswers(), request.getVisitorName());
    }//TODO- where are we getting visitorName from


    @Override
    @RequestMapping(value = "/logout")
    @CrossOrigin
    public ResponseT<VisitorFacade> logout(@RequestBody RequestVisitorName request) {
        return userService.logout(request.getName());
    }

    @Override
    @RequestMapping(value = "/openNewShop")
    @CrossOrigin
    public Response openNewShop(@RequestBody OpenNewShopRequest request) {
        return marketService.openNewShop(request.getMemberName(), request.getShopName());
    }

    @Override
    @RequestMapping(value = "/updateShopItemAmount")
    @CrossOrigin
    public Response updateShopItemAmount(@RequestBody UpdateShopItemAmountRequest request) {

        return marketService.updateShopItemAmount(request.getShopOwnerName(), request.getItem(),
                request.getAmount(), request.getShopName());
    }


    @Override
    @RequestMapping(value = "/removeItemFromShop")
    @CrossOrigin
    public Response removeItemFromShop(@RequestBody RemoveItemFromShopRequest request) {
        return marketService.removeItemFromShop(request.getShopOwnerName(), request.getItem(), request.getShopName());
    }

    @Override
    @RequestMapping(value = "/addItemToShop")
    @CrossOrigin
    public ResponseT<ShopFacade> addItemToShop(@RequestBody AddItemToShopRequest request) {
        return marketService.addItemToShop(request.getShopOwnerName(), request.getName(), request.getPrice(),
                request.getCategory(), request.getInfo(), request.getKeywords(), request.getAmount(), request.getShopName());
    }

    @Override
    @RequestMapping(value = "/setItemCurrentAmount")
    @CrossOrigin
    public Response setItemCurrentAmount(@RequestBody SetItemCurrentAmountRequest request) {
        return marketService.setItemCurrentAmount(request.getShopOwnerName(), request.getItem(),
                request.getAmount(), request.getShopName());
    }


    @Override
    @RequestMapping(value = "/changeShopItemInfo")
    @CrossOrigin
    public Response changeShopItemInfo(@RequestBody ChangeShopItemInfoRequest request) {
        return marketService.changeShopItemInfo(request.getShopOwnerName(), request.getUpdatedInfo(),
                request.getOldItem(), request.getShopName());
    }

    @Override
    @RequestMapping(value = "/editItemRequest")
    @CrossOrigin
    public Response editItem(@RequestBody editItemRequest request) {
        return marketService.editItem(request.getNewItem(), request.getId());
    }


    @Override
    @RequestMapping(value = "/appointShopOwner")
    @CrossOrigin
    public Response appointShopOwner(@RequestBody AppointmentShopOwnerRequest request) {
        return userService.appointShopOwner(request.getShopOwnerName(), request.getAppointedShopOwner(), request.getShopName());
    }

    @Override
    @RequestMapping(value = "/appointShopManager")
    @CrossOrigin
    public Response appointShopManager(@RequestBody AppointmentShopManagerRequest request) {
        return userService.appointShopManager(request.getShopOwnerName(), request.getAppointedShopManager(), request.getShopName());
    }

    @Override
    @RequestMapping(value = "/editShopManagerPermissions")
    @CrossOrigin
    public Response editShopManagerPermissions(@RequestBody EditShopManagerPermissionsRequest request) {
        return this.userService.editShopManagerPermissions(
                request.getShopOwnerName(), request.getManagerName(), request.getRelatedShop(), request.getUpdatedAppointment());
    }

    @Override
    @RequestMapping(value = "/getManagerPermission")
    @CrossOrigin
    public ResponseT<AppointmentFacade> getManagerPermission(@RequestBody GetManagerPermissionRequest request) {
        return this.userService.getManagerAppointment(request.getShopOwnerName(),
                request.getManagerName(), request.getRelatedShop());
    }

    @Override
    @RequestMapping(value = "/closeShop")
    @CrossOrigin
    public Response closeShop(@RequestBody CloseShopRequest request) {
        return this.marketService.closeShop(request.getShopOwnerName(), request.getShopName());
    }
    @Override
    @RequestMapping(value = "/reOpenClosedShop")
    @CrossOrigin
    public Response reOpenClosedShop(@RequestBody TwoStringRequest request){
        return this.marketService.reOpenClosedShop(request.getShopName(), request.getName());
    }
    @Override
    @RequestMapping(value = "/getShopEmployeesInfo")
    @CrossOrigin
    public ResponseT<List<AppointmentFacade>> getShopEmployeesInfo(@RequestBody GetShopEmployeesRequest request) {
        return marketService.getShopEmployeesInfo(request.getShopManagerName(), request.getShopName());
    }

    @Override
    @RequestMapping(value = "/getShopInfo")
    @CrossOrigin
    public ResponseT<ShopFacade> getShopInfo(@RequestBody TwoStringRequest request) {
        return marketService.getShopInfo(request.getName(), request.getShopName());
    }

    @Override
    @RequestMapping(value = "/getShopPurchaseHistory")
    @CrossOrigin
    public ResponseT<String> getShopPurchaseHistory(@RequestBody TwoStringRequest request) {
        return marketService.getShopPurchaseHistory(request.getName(), request.getShopName());
    }

    @Override
    @RequestMapping(value = "/getAllSystemPurchaseHistory")
    @CrossOrigin
    public ResponseT<String> getAllSystemPurchaseHistory(@RequestBody GetAllSystemPurchaseHistoryRequest request) {
        return marketService.getAllSystemPurchaseHistory(request.getSystemManagerName());
    }

    @Override
    @RequestMapping(value = "/getHistoryByShop")
    @CrossOrigin
    public ResponseT<String> getHistoryByShop(@RequestBody TwoStringRequest request) {
        return marketService.getHistoryByShop(request.getName(), request.getShopName());
    }

    @Override
    @RequestMapping(value = "/getHistoryByMember")
    @CrossOrigin
    public ResponseT<String> getHistoryByMember(@RequestBody GetHistoryByMemberRequest request) {
        return marketService.getHistoryByMember(request.getSystemManagerName(), request.getMemberName());
    }

    @Override
    @RequestMapping(value = "/removeShopOwnerAppointment")
    @CrossOrigin
    public Response removeShopOwnerAppointment(@RequestBody RemoveAppointmentRequest request) {
        return marketService.removeShopOwnerAppointment(request.getBoss(), request.getFiredAppointed(), request.getShopName());
    }

    @Override
    @RequestMapping(value = "/removeMember")
    @CrossOrigin
        public Response removeMember(@RequestBody RemoveMemberRequest request) {
        return marketService.removeMember(request.getManager(), request.getMemberToRemove());
    }

    @Override
    @RequestMapping(value = "/getItemInfo")
    @CrossOrigin
    public ResponseT<ItemFacade> getItemInfo(@RequestBody GetItemInfoRequest request) {
        return marketService.getItemInfo(request.getName(), request.getItemId());
    }

    @Override
    @RequestMapping(value = "/getMarketInfo")
    @CrossOrigin
    public ResponseT<String> getMarketInfo(@RequestBody GetMarketInfoRequest request) {
        return marketService.getMarketInfo(request.getSysManager());
    }

    @Override
    @RequestMapping(value = "/addDiscountToShop")
    @CrossOrigin
    public Response addDiscountToShop(@RequestBody AddDiscountToShopRequest request) {
        return marketService.addDiscountToShop(request.getVisitorName(), request.getShopName(), request.getDiscount());
    }

    @Override
    @RequestMapping(value = "/removeDiscountFromShop")
    @CrossOrigin
    public Response removeDiscountFromShop(@RequestBody RemoveDiscountFromShopRequest request) {
        return marketService.removeDiscountFromShop(request.getVisitorName(), request.getShopName(), request.getDiscount());
    }

    @Override
    @RequestMapping(value = "/addPurchasePolicyToShop")
    @CrossOrigin
    public Response addPurchasePolicyToShop(@RequestBody AddPurchasePolicyToShopRequest request) {
        return marketService.addPurchasePolicyToShop(request.getVisitorName(), request.getShopName(), request.getPolicy());
    }

    @Override
    @RequestMapping(value = "/removePurchasePolicyFromShop")
    @CrossOrigin
    public Response removePurchasePolicyFromShop(@RequestBody RemovePurchasePolicyFromShopRequest request) {
        return marketService.removePurchasePolicyFromShop(request.getVisitorName(), request.getShopName(), request.getPolicy());
    }

    @Override
    @RequestMapping(value = "/getPurchasePoliciesOfShop")
    @CrossOrigin
    public ResponseT<List<PurchasePolicyTypeWrapper>> getPurchasePoliciesOfShop(@RequestBody GetPoliciesRequest request) {
        return marketService.getPurchasePoliciesOfShop(request.getVisitorName(), request.getShopName());
    }

    //TODO change request body- should be discount request, may be TwoStringRequest.
    @Override
    @RequestMapping(value = "/getDiscountTypesOfShop")
    @CrossOrigin
    public ResponseT<List<DiscountTypeWrapper>> getDiscountTypesOfShop(@RequestBody GetPoliciesRequest request) {
        return marketService.getDiscountTypesOfShop(request.getVisitorName(), request.getShopName());
    }


    @Override
    @RequestMapping(value = "/isServerInit")
    @CrossOrigin
    public Response isServerInit() {
        return marketService.isServerInit();
    }

    @Override
    @RequestMapping(value = "/addABid")
    @CrossOrigin
    public Response addABid(@RequestBody AddABidRequest request) {
        return marketService.addABid(request.getVisitorName (), request.getShopName (), request.getItemId (), request.getPrice (), request.getAmount());
    }

    @Override
    @RequestMapping(value = "/approveABid")
    @CrossOrigin
    public Response approveABid(@RequestBody ApproveABidRequest request) {
        return marketService.approveABid(request.getApproves (), request.getShopName (), request.getAskedBy (), request.getItemId ());
    }

    @Override
    @RequestMapping(value = "/suggestNewOfferToBid")
    @CrossOrigin
    public Response suggestNewOfferToBid(@RequestBody SuggestNewOfferToBidRequest request) {
        return marketService.suggestNewOfferToBid(request.getSuggester (), request.getShopName (), request.getAskedBy (), request.getItemId (), request.getNewPrice ());
    }

    @Override
    @RequestMapping(value = "/rejectABid")
    @CrossOrigin
    public Response rejectABid(@RequestBody RejectABidRequest request) {
        return marketService.rejectABid(request.getOpposed (), request.getShopName (), request.getBuyer (), request.getItemId ());
    }

    @Override
    @RequestMapping(value = "/cancelABid")
    @CrossOrigin
    public Response cancelABid(@RequestBody CancelABidRequest request) {
        return marketService.cancelABid(request.getShopName (), request.getBuyer (), request.getItemId ());
    }
    @Override
    @RequestMapping(value = "/approveAppointment")
    @CrossOrigin
    public Response approveAppointment(@RequestBody ApproveAppointmentRequest request) {
        return marketService.approveAppointment(request.getOwnerName(), request.getAppointedName(), request.getShopName());
    }
    @Override
    @RequestMapping(value = "/rejectAppointment")
    @CrossOrigin
    public Response rejectAppointment(@RequestBody ApproveAppointmentRequest request) {
        return marketService.rejectAppointment(request.getOwnerName(), request.getAppointedName(), request.getShopName());
    }
    @Override
    @RequestMapping(value = "/getMyPendingApps")
    @CrossOrigin
    public ResponseT<List<String>> getMyPendingApps(@RequestBody MyPendingAppsRequest request) {
        return marketService.getMyPendingApps(request.getOwnerName(), request.getShopName());
    }
    @Override
    @RequestMapping(value = "/isSystemManager")
    @CrossOrigin
    public ResponseT<Boolean> isSystemManager(@RequestBody IsSystemManagerRequest request) {
        return marketService.isSystemManager(request.getName());
    }
    @Override
    @RequestMapping(value = "/approveOrRejectBatch")
    @CrossOrigin
    public ResponseT<List<String>> approveOrRejectBatch(@RequestBody approveOrRejectBatchRequest request) {
        return marketService.approveOrRejectBatch(request.getShopName(),request.getOwnerName(),request.getAppointedNames(),request.isApprove());

    @RequestMapping(value = "/getAcqsForMember")
    @CrossOrigin
    public ResponseT<List<AcquisitionFacade>> getAcqsForMember(@RequestBody getAcqsForMemberRequest request) {
        return marketService.getAcqsForMember(request.getMemberName());
    }

    public ResponseT<MemberFacade> getMember(String memberName) {
        return userService.getMember(memberName);
    }

    public ResponseT<VisitorFacade> getVisitor(String name) {
        return userService.getVisitor(name);
    }

    public ResponseT<ItemFacade> getItemById(int id) {
        return marketService.getItemById(id);
    }

}

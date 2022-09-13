package com.example.server.serviceLayer;

import com.example.server.businessLayer.Market.Acquisition;
import com.example.server.businessLayer.Market.Item;
import com.example.server.serviceLayer.FacadeObjects.*;
import com.example.server.serviceLayer.Requests.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


import java.util.List;

public interface IService {
    //  ************************** System UseCases *******************************//
//
//    /**
//     * to be done in the version with the DB
//     * @return
//     */
//    public Response initMarket();



    Response firstInitMarket(InitMarketRequest request);

    // ************************** Visitor Use cases ******************************//



    /**
     * generates a unique name (temporary)
     * example -visitor123
     *
     * @return
     */
    public ResponseT<VisitorFacade> guestLogin();

    /**
     * if not a member - deletes from data
     */
    public Response exitSystem(ExitSystemRequest request);

    /**
     *
     * @param request
     * @return
     */
    public ResponseT<Boolean> register(NamePasswordRequest request);

    /**
     *
     * @param request
     * @return
     */

    public Response addPersonalQuery(AddPersonalQueryRequest request);

    /**
     * SearchProductByNameRequest request
     * @param request
     * @return
     */
    public ResponseT<List<ItemFacade>> searchProductByName(SearchProductByNameRequest request);

    /**
     * @param category
     * @return
     */
    public ResponseT<List<ItemFacade>> searchProductByCategory(Item.Category category);


    /**
     *
     * @param request
     * @return
     */
    public ResponseT<List<ItemFacade>> searchProductByKeyword(SearchProductByNameRequest request);

    /**
     *
     * @param request
     * @return
     */
    public ResponseT<List<ItemFacade>> filterItemByPrice(FilterItemByPriceRequest request);

    /**
     *
     * @param request
     * @return
     */
    public ResponseT<List<ItemFacade>> filterItemByCategory(FilterItemByCategoryRequest request);

    /**
     *
     * @param request
     * @return
     */
    public Response addItemToShoppingCart(AddItemToShoppingCartRequest request);

    /**
     *
     * @param request
     * @return
     */
    public ResponseT<ShoppingCartFacade> showShoppingCart(RequestVisitorName request);


    /**
     *
     * @param request
     * @return
     */
    public Response editItemFromShoppingCart(EditItemFromShoppingCartRequest request);

    /**
     *
     * @param request
     * @return
     */
    public ResponseT<ShoppingCartFacade> calculateShoppingCart(RequestVisitorName request);

    /**
     *
     * @param request
     * @return if succeed - new shopping cart
     * if couldn't complete - returns an error message with null shopping cart
     */
    public Response buyShoppingCart(BuyShoppingCartRequest request);


    /**
     *
     * @param request
     * @return
     */
    public ResponseT<List<String>> memberLogin(NamePasswordRequest request);

    /**
     *
     * @param request
     * @return
     */
    public ResponseT<MemberFacade> validateSecurityQuestions(ValidateSecurityRequest request);


    //************************* Member Use cases *************************************//

    /**
     *
     * @param request
     * @return
     */
    public ResponseT<VisitorFacade> logout(RequestVisitorName request);

    /**
     *
     * @param request
     * @return
     */
    public Response openNewShop(OpenNewShopRequest request);

    /**
     * an owner can reopen his own closed. employees and items return to what ever had before.
     * member that had been removed from market will be removed from shop too
     * @param request first string- owner, 2nd string - shopName
     * @return valid response if approved, error if not
     */
    public Response reOpenClosedShop(TwoStringRequest request);

    /**
     *
     * @param request
     * @return
     */
    public ResponseT<ShopFacade> getShopInfo(TwoStringRequest request);


    // *********************** Shop Owner use cases *******************************//

    /**
     *
     * @param request
     * @return
     */
    public Response updateShopItemAmount(UpdateShopItemAmountRequest request);

    /**
     *
     * @param request
     * @return
     */
    public Response removeItemFromShop(RemoveItemFromShopRequest request);

    /**
     *
     * @param addItemToShopRequest
     * @return
     */
    public ResponseT<ShopFacade> addItemToShop(AddItemToShopRequest addItemToShopRequest);


    /**
     *
     * @param request
     * @return
     */
    public Response setItemCurrentAmount(SetItemCurrentAmountRequest request);


    /**
     *
     * @param request
     * @return
     */
    public Response changeShopItemInfo(ChangeShopItemInfoRequest request);


    Response editItem(@RequestBody editItemRequest request);

    /**
     *
     * @param request
     * @return
     */
    public Response appointShopOwner(AppointmentShopOwnerRequest request);

    /**
     *
     * @param request
     * @return
     */
    public Response appointShopManager(AppointmentShopManagerRequest request);


    /**
     *
     * @param request
     * @return
     */
    public Response editShopManagerPermissions(EditShopManagerPermissionsRequest request);

    /**
     *
     * @param request
     * @return
     */
    public ResponseT getManagerPermission(GetManagerPermissionRequest request);

    /**
     *
     * @param request
     * @return
     */
    public Response closeShop(CloseShopRequest request);

    // ************************** Shop Manager and Shop Owner use cases ********************************//

    /**
     *
     * @param request
     * @return
     */
    public ResponseT<List<AppointmentFacade>> getShopEmployeesInfo(GetShopEmployeesRequest request);

    /**
     *
     * @param request
     * @return
     */
    public ResponseT<String> getShopPurchaseHistory(TwoStringRequest request);


    // ************************** System Manager use cases ********************************//

    /**
     *
     * @param request
     * @return
     */
    public ResponseT<String> getAllSystemPurchaseHistory(GetAllSystemPurchaseHistoryRequest request);


    /**
     *
     * @param request
     * @return
     */
    public ResponseT<String> getHistoryByShop(TwoStringRequest request);

    /**
     * @return Member purchase history
     */
    public ResponseT<String> getHistoryByMember(GetHistoryByMemberRequest request);

    /**
     *
     * @param request
     * @return
     */
    public Response removeShopOwnerAppointment(RemoveAppointmentRequest request);

    /**
     *
     * @param request
     * @return
     */
    public Response removeMember(RemoveMemberRequest request);

    /**
     *
     * @param request
     * @return
     */
    ResponseT<ItemFacade> getItemInfo(GetItemInfoRequest request);

    /**
     *
     * @param request
     * @return
     */
    ResponseT<String> getMarketInfo(GetMarketInfoRequest request);

    /**
     * @param request
     * @return
     */
    Response addDiscountToShop(AddDiscountToShopRequest request);

    /**
     * @param request
     * @return
     */
    Response removeDiscountFromShop(RemoveDiscountFromShopRequest request);

    /**
     * @param request
     * @return
     */
    Response addPurchasePolicyToShop(AddPurchasePolicyToShopRequest request);

    /**
     * @param request
     * @return
     */
    Response removePurchasePolicyFromShop(RemovePurchasePolicyFromShopRequest request);

    /**
     * @param request
     * @return
     */
    Response getPurchasePoliciesOfShop(GetPoliciesRequest request);

    /**
     * @param request
     * @return
     */
    Response getDiscountTypesOfShop(GetPoliciesRequest request);

    /**
     *
     * @return empty response if true, error message if not
     */
    public Response isServerInit();

    /**
     *  user sends new bid to shop
     * @param request
     * @return
     */
    public Response addABid(AddABidRequest request);

    /**
     *  owner/user approves the bid
     * @param request
     * @return
     */
    public Response approveABid(ApproveABidRequest request);


    /**
     *  owner suggest new offer - all other owner must approve it
     * @param request
     * @return
     */
    Response suggestNewOfferToBid(SuggestNewOfferToBidRequest request);

    /**
     *  owner/user may reject the request
     * @param request
     * @return
     */
    Response rejectABid(RejectABidRequest request);

    /**
     *  user revert his bid
     * @param request
     * @return
     */
    Response cancelABid(CancelABidRequest request);


    Response approveAppointment(@RequestBody ApproveAppointmentRequest request);


    Response rejectAppointment(@RequestBody ApproveAppointmentRequest request);



    ResponseT<List<String>> getMyPendingApps(@RequestBody MyPendingAppsRequest request);

    ResponseT<Boolean> isSystemManager(@RequestBody IsSystemManagerRequest request);


    ResponseT<List<String>> approveOrRejectBatch(@RequestBody approveOrRejectBatchRequest request);

    ResponseT<List<AcquisitionFacade>> getAcqsForMember(@RequestBody getAcqsForMemberRequest request);

}
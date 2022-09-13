package com.example.server.serviceLayer.FacadeObjects;

import com.example.server.businessLayer.Market.Acquisition;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.businessLayer.Market.ShoppingCart;

public class AcquisitionFacade  implements FacadeObject<Acquisition> {

    private boolean paymentDone;
    private boolean supplyConfirmed;
    private ShoppingCartFacade shoppingCartToBuy;
    private String buyerName;
    private int supplyID;
    private int paymentID;
    public AcquisitionFacade(Acquisition acquisition)
    {
        this.buyerName = acquisition.getBuyerName();
        this.paymentDone=acquisition.isPaymentDone();
        this.paymentID = acquisition.getPaymentID();
        this.supplyID = acquisition.getSupplyID();
        this.shoppingCartToBuy = new ShoppingCartFacade(acquisition.getShoppingCartToBuy());
    }

    @Override
    public Acquisition toBusinessObject() throws MarketException {
        return new Acquisition(this.shoppingCartToBuy.toBusinessObject(),this.buyerName);
    }
}

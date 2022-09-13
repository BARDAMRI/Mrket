package com.example.server.serviceLayer.FacadeObjects.PolicyFacade;

import com.example.server.businessLayer.Market.Policies.PurchasePolicy.AtLeastPurchasePolicyType;
import com.example.server.businessLayer.Market.Policies.PurchasePolicy.AtMostPurchasePolicyType;
import com.example.server.businessLayer.Market.Policies.PurchasePolicy.OrCompositePurchasePolicyType;
import com.example.server.businessLayer.Market.Policies.PurchasePolicy.PurchasePolicyType;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;

import java.util.ArrayList;
import java.util.List;

public class OrCompositePurchasePolicyTypeFacade extends CompositePurchasePolicyTypeFacade{


    public OrCompositePurchasePolicyTypeFacade(){};
    @Override
    public PurchasePolicyType toBusinessObject() throws MarketException {
        List<PurchasePolicyType> purchasePolicyTypes = new ArrayList<> (  );
        for(PurchasePolicyTypeFacade purchasePolicyTypeFacade: purchasePolicyTypeFacades)
            purchasePolicyTypes.add ( purchasePolicyTypeFacade.toBusinessObject () );
        return new OrCompositePurchasePolicyType ( purchasePolicyTypes );
    }

    @Override
    public PurchasePolicyTypeFacade toFacade(OrCompositePurchasePolicyType purchasePolicyType) {
        return null;
    }

    @Override
    public PurchasePolicyTypeFacade toFacade(AtLeastPurchasePolicyType purchasePolicyType) {
        return null;
    }

    @Override
    public PurchasePolicyTypeFacade toFacade(AtMostPurchasePolicyType purchasePolicyType) {
        return null;
    }

    @Override
    public PurchasePolicyTypeFacade toFacade(PurchasePolicyType purchasePolicyType) {
        return purchasePolicyType.visitToFacade ( this );
    }
}

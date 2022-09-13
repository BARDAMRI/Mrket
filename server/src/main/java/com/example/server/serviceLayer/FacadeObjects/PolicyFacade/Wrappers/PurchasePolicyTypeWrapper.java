package com.example.server.serviceLayer.FacadeObjects.PolicyFacade.Wrappers;

import com.example.server.businessLayer.Market.Policies.PurchasePolicy.AtLeastPurchasePolicyType;
import com.example.server.businessLayer.Market.Policies.PurchasePolicy.AtMostPurchasePolicyType;
import com.example.server.businessLayer.Market.Policies.PurchasePolicy.OrCompositePurchasePolicyType;
import com.example.server.businessLayer.Market.Policies.PurchasePolicy.PurchasePolicyState.*;
import com.example.server.businessLayer.Market.Policies.PurchasePolicy.PurchasePolicyType;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.serviceLayer.FacadeObjects.FacadeObject;

import java.util.ArrayList;
import java.util.List;

public class PurchasePolicyTypeWrapper implements FacadeObject<PurchasePolicyType> {


    public enum PurchasePolicyTypeWrapperType {
        OrCompositePurchasePolicyTypeFacade,
        AtLeastPurchasePolicyTypeFacade,
        AtMostPurchasePolicyTypeFacade;
    }

    private PurchasePolicyTypeWrapperType purchasePolicyTypeWrapperType;
    private double amount;


    private PurchasePolicyLevelStateWrapper purchasePolicyLevelStateWrapper;
    private List<PurchasePolicyTypeWrapper> purchasePolicyTypeWrappers;

    public PurchasePolicyTypeWrapper(PurchasePolicyTypeWrapperType purchasePolicyTypeWrapperType, double amount, PurchasePolicyLevelStateWrapper purchasePolicyLevelStateWrapper, List<PurchasePolicyTypeWrapper> purchasePolicyTypeWrappers) {
        this.purchasePolicyTypeWrapperType = purchasePolicyTypeWrapperType;
        this.amount = amount;
        this.purchasePolicyLevelStateWrapper = purchasePolicyLevelStateWrapper;
        this.purchasePolicyTypeWrappers = purchasePolicyTypeWrappers;
    }

    public PurchasePolicyTypeWrapper() {
    }


    @Override
    public PurchasePolicyType toBusinessObject() throws MarketException {
        switch (purchasePolicyTypeWrapperType) {
            case AtMostPurchasePolicyTypeFacade -> {
                return new AtMostPurchasePolicyType(purchasePolicyLevelStateWrapper.toBusinessObject(), amount);
            }
            case AtLeastPurchasePolicyTypeFacade -> {
                return new AtLeastPurchasePolicyType(purchasePolicyLevelStateWrapper.toBusinessObject(), amount);
            }
            case OrCompositePurchasePolicyTypeFacade -> {
                List<PurchasePolicyType> purchasePolicyTypes = new ArrayList<>();
                for (PurchasePolicyTypeWrapper purchasePolicyTypeWrapper : purchasePolicyTypeWrappers) {
                    purchasePolicyTypes.add(purchasePolicyTypeWrapper.toBusinessObject());
                }
                return new OrCompositePurchasePolicyType(purchasePolicyTypes);
            }
            default -> {
                return null;
            }
        }
    }


    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public List<PurchasePolicyTypeWrapper> getPurchasePolicyTypeWrappers() {
        return purchasePolicyTypeWrappers;
    }

    public void setPurchasePolicyTypeWrappers(List<PurchasePolicyTypeWrapper> purchasePolicyTypeWrappers) {
        this.purchasePolicyTypeWrappers = purchasePolicyTypeWrappers;
    }

    public PurchasePolicyTypeWrapperType getCompositePurchasePolicyTypeWrapperType() {
        return purchasePolicyTypeWrapperType;
    }

    public void setCompositePurchasePolicyTypeWrapperType(PurchasePolicyTypeWrapperType purchasePolicyTypeWrapperType) {
        this.purchasePolicyTypeWrapperType = purchasePolicyTypeWrapperType;
    }

    public PurchasePolicyTypeWrapperType getPurchasePolicyTypeWrapperType() {
        return purchasePolicyTypeWrapperType;
    }

    public void setPurchasePolicyTypeWrapperType(PurchasePolicyTypeWrapperType purchasePolicyTypeWrapperType) {
        this.purchasePolicyTypeWrapperType = purchasePolicyTypeWrapperType;
    }

    public PurchasePolicyLevelStateWrapper getPurchasePolicyLevelStateWrapper() {
        return purchasePolicyLevelStateWrapper;
    }

    public void setPurchasePolicyLevelStateWrapper(PurchasePolicyLevelStateWrapper purchasePolicyLevelStateWrapper) {
        this.purchasePolicyLevelStateWrapper = purchasePolicyLevelStateWrapper;
    }

    public static PurchasePolicyTypeWrapper createPurchasePolicyWrapper(PurchasePolicyType purchasePolicyType) {
        PurchasePolicyTypeWrapper purchasePolicyTypeWrapper = new PurchasePolicyTypeWrapper();
        ;
        if (purchasePolicyType.isAtLeast()) {
            AtLeastPurchasePolicyType atLeastPurchasePolicyType = (AtLeastPurchasePolicyType) purchasePolicyType;
            purchasePolicyTypeWrapper.setPurchasePolicyTypeWrapperType(PurchasePolicyTypeWrapper.PurchasePolicyTypeWrapperType.AtLeastPurchasePolicyTypeFacade);
            purchasePolicyTypeWrapper.setPurchasePolicyLevelStateWrapper(PurchasePolicyLevelStateWrapper.createPurchasePolicyLevelStateWrapper(purchasePolicyType.getPurchasePolicyLevelState()));
            purchasePolicyTypeWrapper.setAmount(atLeastPurchasePolicyType.getAmount());
        } else if (purchasePolicyType.isAtMost()) {
            AtMostPurchasePolicyType atMostPurchasePolicyType = (AtMostPurchasePolicyType) purchasePolicyType;
            purchasePolicyTypeWrapper.setPurchasePolicyTypeWrapperType(PurchasePolicyTypeWrapper.PurchasePolicyTypeWrapperType.AtMostPurchasePolicyTypeFacade);
            purchasePolicyTypeWrapper.setPurchasePolicyLevelStateWrapper(PurchasePolicyLevelStateWrapper.createPurchasePolicyLevelStateWrapper(purchasePolicyType.getPurchasePolicyLevelState()));
            purchasePolicyTypeWrapper.setAmount(atMostPurchasePolicyType.getAmount());
        } else {
            List<PurchasePolicyTypeWrapper> purchasePolicyTypeWrappers = new ArrayList<>();
            OrCompositePurchasePolicyType orCompositePurchasePolicyType = (OrCompositePurchasePolicyType) purchasePolicyType;
            for (PurchasePolicyType cur : orCompositePurchasePolicyType.getPolicies()) {
                purchasePolicyTypeWrappers.add(createPurchasePolicyWrapper(cur));
            }
            purchasePolicyTypeWrapper.setCompositePurchasePolicyTypeWrapperType(PurchasePolicyTypeWrapper.PurchasePolicyTypeWrapperType.OrCompositePurchasePolicyTypeFacade);
            purchasePolicyTypeWrapper.setPurchasePolicyTypeWrappers(purchasePolicyTypeWrappers);
        }
        return purchasePolicyTypeWrapper;
    }


}

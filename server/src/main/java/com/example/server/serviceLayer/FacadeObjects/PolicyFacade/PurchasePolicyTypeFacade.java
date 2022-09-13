package com.example.server.serviceLayer.FacadeObjects.PolicyFacade;

import com.example.server.businessLayer.Market.Policies.PurchasePolicy.AtLeastPurchasePolicyType;
import com.example.server.businessLayer.Market.Policies.PurchasePolicy.AtMostPurchasePolicyType;
import com.example.server.businessLayer.Market.Policies.PurchasePolicy.OrCompositePurchasePolicyType;
import com.example.server.businessLayer.Market.Policies.PurchasePolicy.PurchasePolicyState.PurchasePolicyLevelState;
import com.example.server.businessLayer.Market.Policies.PurchasePolicy.PurchasePolicyType;
import com.example.server.serviceLayer.FacadeObjects.FacadeObject;

public abstract class PurchasePolicyTypeFacade implements FacadeObject<PurchasePolicyType> {

    protected PurchasePolicyLevelStateFacade purchasePolicyLevelStateFacade;

    public PurchasePolicyTypeFacade(PurchasePolicyLevelStateFacade purchasePolicyLevelStateFacade) {
        this.purchasePolicyLevelStateFacade = purchasePolicyLevelStateFacade;
    }

    public PurchasePolicyTypeFacade(){}

    public PurchasePolicyLevelStateFacade getPurchasePolicyLevelStateFacade() {
        return purchasePolicyLevelStateFacade;
    }


    public void setPurchasePolicyLevelStateFacade(PurchasePolicyLevelStateFacade purchasePolicyLevelStateFacade) {
        this.purchasePolicyLevelStateFacade = purchasePolicyLevelStateFacade;
    }

    public abstract PurchasePolicyTypeFacade toFacade(OrCompositePurchasePolicyType purchasePolicyType);
    public abstract PurchasePolicyTypeFacade toFacade(AtLeastPurchasePolicyType purchasePolicyType);

    public abstract PurchasePolicyTypeFacade toFacade(AtMostPurchasePolicyType purchasePolicyType);

    public abstract PurchasePolicyTypeFacade toFacade(PurchasePolicyType purchasePolicyType);

    public PurchasePolicyLevelStateFacade getPurchasePolicyLevel(PurchasePolicyLevelState purchasePolicyLevelState){
        PurchasePolicyLevelStateFacade purchasePolicyLevelStateFacade;
        if(purchasePolicyLevelState.isItemLevel ()){
            purchasePolicyLevelStateFacade = new ItemPurchasePolicyLevelStateFacade (  );
        } else if(purchasePolicyLevelState.isCategoryLevel ()){
            purchasePolicyLevelStateFacade = new CategoryPurchasePolicyLevelStateFacade (  );
        }else if(purchasePolicyLevelState.isShopLevel ()){
            purchasePolicyLevelStateFacade = new ShopPurchasePolicyLevelStateFacade (  );
        }else if(purchasePolicyLevelState.isOrLevel ()){
            purchasePolicyLevelStateFacade = new OrCompositePurchasePolicyLevelStateFacade (  );
        }else if(purchasePolicyLevelState.isAndLevel ()){
            purchasePolicyLevelStateFacade = new AndCompositePurchasePolicyLevelStateFacade (  );
        } else{
            purchasePolicyLevelStateFacade = new XorCompositePurchasePolicyLevelStateFacade ();
        }
        purchasePolicyLevelStateFacade = purchasePolicyLevelStateFacade.toFacade ( purchasePolicyLevelState );
        return purchasePolicyLevelStateFacade;
    }
}

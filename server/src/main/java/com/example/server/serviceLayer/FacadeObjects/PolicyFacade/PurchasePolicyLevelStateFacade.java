package com.example.server.serviceLayer.FacadeObjects.PolicyFacade;

import com.example.server.businessLayer.Market.Policies.PurchasePolicy.PurchasePolicyState.*;
import com.example.server.serviceLayer.FacadeObjects.FacadeObject;

public abstract class PurchasePolicyLevelStateFacade implements FacadeObject<PurchasePolicyLevelState> {

    public abstract PurchasePolicyLevelStateFacade toFacade(ItemPurchasePolicyLevelState levelState);

    public abstract PurchasePolicyLevelStateFacade toFacade(CategoryPurchasePolicyLevelState levelState);

    public abstract PurchasePolicyLevelStateFacade toFacade(ShopPurchasePolicyLevelState levelState);

    public abstract PurchasePolicyLevelStateFacade toFacade(AndCompositePurchasePolicyLevelState levelState);

    public abstract PurchasePolicyLevelStateFacade toFacade(XorCompositePurchasePolicyLevelState levelState);

    public abstract PurchasePolicyLevelStateFacade toFacade(OrCompositePurchasePolicyLevelState levelState);

    public abstract PurchasePolicyLevelStateFacade toFacade(PurchasePolicyLevelState levelState);

    protected PurchasePolicyLevelStateFacade getPurchasePolicyLevel(PurchasePolicyLevelState purchasePolicyLevelState){
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
        }else{
            purchasePolicyLevelStateFacade = new XorCompositePurchasePolicyLevelStateFacade ();
        }
        purchasePolicyLevelStateFacade = purchasePolicyLevelStateFacade.toFacade ( purchasePolicyLevelState );
        return purchasePolicyLevelStateFacade;
    }

}

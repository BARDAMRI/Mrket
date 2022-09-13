package com.example.server.serviceLayer.FacadeObjects.PolicyFacade.Wrappers;

import com.example.server.businessLayer.Market.Item;
import com.example.server.businessLayer.Market.Policies.PurchasePolicy.PurchasePolicyState.*;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.serviceLayer.FacadeObjects.FacadeObject;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerTemplateAvailabilityProvider;

import java.util.ArrayList;
import java.util.List;

public class PurchasePolicyLevelStateWrapper implements FacadeObject<PurchasePolicyLevelState> {


    public enum PurchasePolicyLevelStateWrapperType {
        AndCompositePurchasePolicyLevelStateFacade,
        XorCompositePurchasePolicyLevelStateFacade,
        OrCompositePurchasePolicyLevelStateFacade,
        CategoryPurchasePolicyLevelStateFacade,
        ItemPurchasePolicyLevelStateFacade,
        ShopPurchasePolicyLevelStateFacade;
    }

    private PurchasePolicyLevelStateWrapperType purchasePolicyLevelStateWrapperType;
    private int itemID;


    private Item.Category category;
    private List<PurchasePolicyLevelStateWrapper> purchasePolicyLevelStateWrappers;

    public PurchasePolicyLevelStateWrapper(PurchasePolicyLevelStateWrapperType purchasePolicyLevelStateWrapperType, int itemID, Item.Category category, List<PurchasePolicyLevelStateWrapper> purchasePolicyLevelStateWrappers) {
        this.purchasePolicyLevelStateWrapperType = purchasePolicyLevelStateWrapperType;
        this.itemID = itemID;
        this.category = category;
        this.purchasePolicyLevelStateWrappers = purchasePolicyLevelStateWrappers;
    }

    public PurchasePolicyLevelStateWrapperType getPurchasePolicyLevelStateWrapperType() {
        return purchasePolicyLevelStateWrapperType;
    }


    public PurchasePolicyLevelStateWrapper() {
    }

    @Override
    public PurchasePolicyLevelState toBusinessObject() throws MarketException {
        switch (purchasePolicyLevelStateWrapperType) {
            case ItemPurchasePolicyLevelStateFacade -> {
                return new ItemPurchasePolicyLevelState(itemID);
            }
            case CategoryPurchasePolicyLevelStateFacade -> {
                return new CategoryPurchasePolicyLevelState(category);
            }
            case ShopPurchasePolicyLevelStateFacade -> {
                return new ShopPurchasePolicyLevelState();
            }
            case OrCompositePurchasePolicyLevelStateFacade -> {
                List<PurchasePolicyLevelState> purchasePolicyLevelStates = new ArrayList<>();
                for (PurchasePolicyLevelStateWrapper purchasePolicyLevelStateWrapper : purchasePolicyLevelStateWrappers) {
                    purchasePolicyLevelStates.add(purchasePolicyLevelStateWrapper.toBusinessObject());
                }
                return new OrCompositePurchasePolicyLevelState(purchasePolicyLevelStates);
            }
            case XorCompositePurchasePolicyLevelStateFacade -> {
                List<PurchasePolicyLevelState> purchasePolicyLevelStates = new ArrayList<>();
                for (PurchasePolicyLevelStateWrapper purchasePolicyLevelStateWrapper : purchasePolicyLevelStateWrappers) {
                    purchasePolicyLevelStates.add(purchasePolicyLevelStateWrapper.toBusinessObject());
                }
                return new XorCompositePurchasePolicyLevelState(purchasePolicyLevelStates);
            }
            case AndCompositePurchasePolicyLevelStateFacade -> {
                List<PurchasePolicyLevelState> purchasePolicyLevelStates = new ArrayList<>();
                for (PurchasePolicyLevelStateWrapper purchasePolicyLevelStateWrapper : purchasePolicyLevelStateWrappers) {
                    purchasePolicyLevelStates.add(purchasePolicyLevelStateWrapper.toBusinessObject());
                }
                return new AndCompositePurchasePolicyLevelState(purchasePolicyLevelStates);
            }
            default -> {
                return null;
            }
        }
    }

    public void setPurchasePolicyLevelStateWrapperType(PurchasePolicyLevelStateWrapperType purchasePolicyLevelStateWrapperType) {
        this.purchasePolicyLevelStateWrapperType = purchasePolicyLevelStateWrapperType;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public Item.Category getCategory() {
        return category;
    }

    public void setCategory(Item.Category category) {
        this.category = category;
    }


    public List<PurchasePolicyLevelStateWrapper> getPurchasePolicyLevelStateWrappers() {
        return purchasePolicyLevelStateWrappers;
    }

    public void setPurchasePolicyLevelStateWrappers(List<PurchasePolicyLevelStateWrapper> purchasePolicyLevelStateWrappers) {
        this.purchasePolicyLevelStateWrappers = purchasePolicyLevelStateWrappers;
    }

    public PurchasePolicyLevelStateWrapperType getCompositeDiscountLevelStateWrapperType() {
        return purchasePolicyLevelStateWrapperType;
    }

    public void setCompositeDiscountLevelStateWrapperType(PurchasePolicyLevelStateWrapperType purchasePolicyLevelStateWrapperType) {
        this.purchasePolicyLevelStateWrapperType = purchasePolicyLevelStateWrapperType;
    }

    public static PurchasePolicyLevelStateWrapper createPurchasePolicyLevelStateWrapper(PurchasePolicyLevelState purchasePolicyLevelState) {
        PurchasePolicyLevelStateWrapper purchasePolicyLevelStateWrapper = new PurchasePolicyLevelStateWrapper();
        if (purchasePolicyLevelState.isItemLevel()) {
            ItemPurchasePolicyLevelState itemPurchasePolicyLevelState = (ItemPurchasePolicyLevelState) purchasePolicyLevelState;
            purchasePolicyLevelStateWrapper.setCompositeDiscountLevelStateWrapperType(PurchasePolicyLevelStateWrapper.PurchasePolicyLevelStateWrapperType.ItemPurchasePolicyLevelStateFacade);
            purchasePolicyLevelStateWrapper.setItemID(itemPurchasePolicyLevelState.getItemId());
        } else if (purchasePolicyLevelState.isCategoryLevel()) {
            CategoryPurchasePolicyLevelState categoryPurchasePolicyLevelState = (CategoryPurchasePolicyLevelState) purchasePolicyLevelState;
            purchasePolicyLevelStateWrapper.setCompositeDiscountLevelStateWrapperType(PurchasePolicyLevelStateWrapper.PurchasePolicyLevelStateWrapperType.CategoryPurchasePolicyLevelStateFacade);
            purchasePolicyLevelStateWrapper.setCategory(categoryPurchasePolicyLevelState.getCategory());
        } else if (purchasePolicyLevelState.isShopLevel()) {
            purchasePolicyLevelStateWrapper.setCompositeDiscountLevelStateWrapperType(PurchasePolicyLevelStateWrapper.PurchasePolicyLevelStateWrapperType.ShopPurchasePolicyLevelStateFacade);
        } else if (purchasePolicyLevelState.isOrLevel()) {
            OrCompositePurchasePolicyLevelState orCompositePurchasePolicyLevelState = (OrCompositePurchasePolicyLevelState) purchasePolicyLevelState;
            List<PurchasePolicyLevelStateWrapper> purchasePolicyLevelStateWrappers = new ArrayList<>();
            for (PurchasePolicyLevelState cur : orCompositePurchasePolicyLevelState.getPurchasePolicyLevelStates()) {
                purchasePolicyLevelStateWrappers.add(createPurchasePolicyLevelStateWrapper((cur)));
            }
            purchasePolicyLevelStateWrapper.setCompositeDiscountLevelStateWrapperType(PurchasePolicyLevelStateWrapper.PurchasePolicyLevelStateWrapperType.OrCompositePurchasePolicyLevelStateFacade);
            purchasePolicyLevelStateWrapper.setPurchasePolicyLevelStateWrappers(purchasePolicyLevelStateWrappers);
        } else if (purchasePolicyLevelState.isXorLevel()) {
            XorCompositePurchasePolicyLevelState xorCompositePurchasePolicyLevelState = (XorCompositePurchasePolicyLevelState) purchasePolicyLevelState;
            List<PurchasePolicyLevelStateWrapper> purchasePolicyLevelStateWrappers = new ArrayList<>();
            for (PurchasePolicyLevelState cur : xorCompositePurchasePolicyLevelState.getPurchasePolicyLevelStates()) {
                purchasePolicyLevelStateWrappers.add(createPurchasePolicyLevelStateWrapper((cur)));
            }
            purchasePolicyLevelStateWrapper.setCompositeDiscountLevelStateWrapperType(PurchasePolicyLevelStateWrapper.PurchasePolicyLevelStateWrapperType.XorCompositePurchasePolicyLevelStateFacade);
            purchasePolicyLevelStateWrapper.setPurchasePolicyLevelStateWrappers(purchasePolicyLevelStateWrappers);
        } else {
            AndCompositePurchasePolicyLevelState andCompositePurchasePolicyLevelState = (AndCompositePurchasePolicyLevelState) purchasePolicyLevelState;
            List<PurchasePolicyLevelStateWrapper> purchasePolicyLevelStateWrappers = new ArrayList<>();
            for (PurchasePolicyLevelState cur : andCompositePurchasePolicyLevelState.getPurchasePolicyLevelStates()) {
                purchasePolicyLevelStateWrappers.add(createPurchasePolicyLevelStateWrapper((cur)));
            }
            purchasePolicyLevelStateWrapper.setCompositeDiscountLevelStateWrapperType(PurchasePolicyLevelStateWrapper.PurchasePolicyLevelStateWrapperType.AndCompositePurchasePolicyLevelStateFacade);
            purchasePolicyLevelStateWrapper.setPurchasePolicyLevelStateWrappers(purchasePolicyLevelStateWrappers);
        }
        return purchasePolicyLevelStateWrapper;
    }
}

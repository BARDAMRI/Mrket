package com.example.server.serviceLayer.FacadeObjects.PolicyFacade.Wrappers;

import com.example.server.businessLayer.Market.Policies.DiscountPolicy.Condition.AmountOfItemCondition;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.Condition.CompositionCondition.AndCompositeCondition;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.Condition.CompositionCondition.OrCompositeCondition;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.Condition.Condition;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.Condition.PriceCondition;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.serviceLayer.FacadeObjects.FacadeObject;
import com.example.server.serviceLayer.FacadeObjects.PolicyFacade.AmountOfItemConditionFacade;

import java.util.ArrayList;
import java.util.List;

public class ConditionWrapper implements FacadeObject<Condition> {

    public enum ConditionWrapperType {
        AndCompositeConditionFacade,
        OrCompositeConditionFacade,
        AmountOfItemConditionFacade,
        PriceConditionFacade;

    }
    private ConditionWrapperType conditionWrapperType;

    private List<ConditionWrapper> conditionWrappers;

    private int itemID;
    private double amount;
    private double price;
    public ConditionWrapper(ConditionWrapperType conditionWrapperType, List<ConditionWrapper> conditionWrappers, int itemID, double amount, double price) {
        this.conditionWrapperType = conditionWrapperType;
        this.conditionWrappers = conditionWrappers;
        this.itemID = itemID;
        this.amount = amount;
        this.price = price;
    }

    public ConditionWrapper() {
    }

    @Override
    public Condition toBusinessObject() throws MarketException {
        switch (conditionWrapperType){
            case PriceConditionFacade -> {
                return new PriceCondition ( price );
            }
            case AmountOfItemConditionFacade -> {
                return new AmountOfItemCondition ( amount, itemID );
            }
            case AndCompositeConditionFacade -> {
                List<Condition> conditions = new ArrayList<> (  );
                for(ConditionWrapper conditionWrapper : conditionWrappers){
                    conditions.add ( conditionWrapper.toBusinessObject () );
                }
                return new AndCompositeCondition ( conditions );
            }
            case OrCompositeConditionFacade -> {
                List<Condition> conditions = new ArrayList<> (  );
                for(ConditionWrapper conditionWrapper : conditionWrappers){
                    conditions.add ( conditionWrapper.toBusinessObject () );
                }
                return new OrCompositeCondition ( conditions );
            }
            default -> {
                return null;
            }
        }
    }

    public List<ConditionWrapper> getConditionWrappers() {
        return conditionWrappers;
    }

    public void setConditionWrappers(List<ConditionWrapper> conditionWrappers) {
        this.conditionWrappers = conditionWrappers;
    }

    public int getItemID() {
        return itemID;
    }

    public void setItemID(int itemID) {
        this.itemID = itemID;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ConditionWrapperType getConditionWrapperType() {
        return conditionWrapperType;
    }

    public void setConditionWrapperType(ConditionWrapperType conditionWrapperType) {
        this.conditionWrapperType = conditionWrapperType;
    }

    public ConditionWrapperType getCompositeConditionWrapperType() {
        return conditionWrapperType;
    }

    public void setCompositeConditionWrapperType(ConditionWrapperType conditionWrapperType) {
        this.conditionWrapperType = conditionWrapperType;
    }

    public static ConditionWrapper createConditionWrapper(Condition condition) {
        ConditionWrapper conditionWrapper = new ConditionWrapper (  );
        if(condition.isPrice ()){
            PriceCondition priceCondition = (PriceCondition) condition;
            conditionWrapper.setCompositeConditionWrapperType ( ConditionWrapper.ConditionWrapperType.PriceConditionFacade );
            conditionWrapper.setPrice ( priceCondition.getPriceNeeded () );
        } else if (condition.isAmountOfItem ()) {
            AmountOfItemCondition amountOfItemCondition = (AmountOfItemCondition) condition;
            conditionWrapper.setCompositeConditionWrapperType ( ConditionWrapper.ConditionWrapperType.AmountOfItemConditionFacade );
            conditionWrapper.setAmount ( amountOfItemCondition.getAmountNeeded () );
            conditionWrapper.setItemID ( amountOfItemCondition.getItemNeeded () );
        } else if (condition.isAnd ()) {
            AndCompositeCondition andCompositeCondition = (AndCompositeCondition) condition;
            List<ConditionWrapper> conditionWrappers = new ArrayList<> (  );
            for(Condition cur : andCompositeCondition.getConditions ()){
                conditionWrappers.add ( createConditionWrapper ( cur ) );
            }
            conditionWrapper.setCompositeConditionWrapperType ( ConditionWrapper.ConditionWrapperType.AndCompositeConditionFacade );
            conditionWrapper.setConditionWrappers ( conditionWrappers );
        } else{
            OrCompositeCondition orCompositeCondition = (OrCompositeCondition) condition;
            List<ConditionWrapper> conditionWrappers = new ArrayList<> (  );
            for(Condition cur : orCompositeCondition.getConditions ()){
                conditionWrappers.add ( createConditionWrapper ( cur ) );
            }
            conditionWrapper.setCompositeConditionWrapperType ( ConditionWrapper.ConditionWrapperType.OrCompositeConditionFacade );
            conditionWrapper.setConditionWrappers ( conditionWrappers );
        }
        return conditionWrapper;
    }


}

package com.example.server.serviceLayer.FacadeObjects.PolicyFacade.Wrappers;

import com.example.server.businessLayer.Market.Policies.DiscountPolicy.CompositeDiscount.MaxCompositeDiscount;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.ConditionalDiscount;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.DiscountType;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.SimpleDiscount;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.serviceLayer.FacadeObjects.FacadeObject;

import java.util.ArrayList;
import java.util.List;

public class DiscountTypeWrapper implements FacadeObject<DiscountType> {


    public enum DiscountTypeWrapperType {
        MaxCompositeDiscountTypeFacade,
        SimpleDiscountFacade,
        ConditionalDiscountFacade;
    }
    private DiscountTypeWrapperType discountTypeWrapperType;
    private double percentageOfDiscount;

    private DiscountLevelStateWrapper discountLevelStateWrapper;
    private ConditionWrapper conditionWrapper;
    List<DiscountTypeWrapper> discountTypeWrappers;
    public DiscountTypeWrapper(DiscountTypeWrapperType discountTypeWrapperType, double percentageOfDiscount, DiscountLevelStateWrapper discountLevelStateWrapper, ConditionWrapper conditionWrapper, List<DiscountTypeWrapper> discountTypeWrappers) {
        this.discountTypeWrapperType = discountTypeWrapperType;
        this.percentageOfDiscount = percentageOfDiscount;
        this.discountLevelStateWrapper = discountLevelStateWrapper;
        this.conditionWrapper = conditionWrapper;
        this.discountTypeWrappers = discountTypeWrappers;
    }

    public DiscountTypeWrapperType getDiscountTypeWrapperType() {
        return discountTypeWrapperType;
    }

    public void setDiscountTypeWrapperType(DiscountTypeWrapperType discountTypeWrapperType) {
        this.discountTypeWrapperType = discountTypeWrapperType;
    }

    public DiscountTypeWrapper() {
    }

    @Override
    public DiscountType toBusinessObject() throws MarketException {
        switch (discountTypeWrapperType){
            case SimpleDiscountFacade -> {
                return new SimpleDiscount ( percentageOfDiscount, discountLevelStateWrapper.toBusinessObject () );
            }
            case ConditionalDiscountFacade -> {
                return new ConditionalDiscount ( percentageOfDiscount, discountLevelStateWrapper.toBusinessObject (), conditionWrapper.toBusinessObject ( ) );
            }
            case MaxCompositeDiscountTypeFacade -> {
                List<DiscountType> discountTypes = new ArrayList<> (  );
                for(DiscountTypeWrapper discountTypeWrapper : discountTypeWrappers){
                    discountTypes.add ( discountTypeWrapper.toBusinessObject () );
                }
                return new MaxCompositeDiscount ( discountTypes );
            }
            default -> {
                return null;
            }
        }
    }

    public double getPercentageOfDiscount() {
        return percentageOfDiscount;
    }

    public void setPercentageOfDiscount(double percentageOfDiscount) {
        this.percentageOfDiscount = percentageOfDiscount;
    }

    public DiscountLevelStateWrapper getDiscountLevelStateWrapper() {
        return discountLevelStateWrapper;
    }

    public void setDiscountLevelStateWrapper(DiscountLevelStateWrapper discountLevelStateWrapper) {
        this.discountLevelStateWrapper = discountLevelStateWrapper;
    }

    public ConditionWrapper getConditionWrapper() {
        return conditionWrapper;
    }

    public void setConditionWrapper(ConditionWrapper conditionWrapper) {
        this.conditionWrapper = conditionWrapper;
    }

    public List<DiscountTypeWrapper> getDiscountTypeWrappers() {
        return discountTypeWrappers;
    }

    public void setDiscountTypeWrappers(List<DiscountTypeWrapper> discountTypeWrappers) {
        this.discountTypeWrappers = discountTypeWrappers;
    }

    public DiscountTypeWrapperType getCompositeDiscountTypeWrapperType() {
        return discountTypeWrapperType;
    }

    public void setCompositeDiscountTypeWrapperType(DiscountTypeWrapperType discountTypeWrapperType) {
        this.discountTypeWrapperType = discountTypeWrapperType;
    }


    public static DiscountTypeWrapper createDiscountTypeWrapper(DiscountType discountType) {
        DiscountTypeWrapper discountTypeWrapper = new DiscountTypeWrapper (  );
        discountTypeWrapper.setPercentageOfDiscount ( discountType.getPercentageOfDiscount () );
        discountTypeWrapper.setDiscountLevelStateWrapper (  DiscountLevelStateWrapper.createDiscountLevelStateWrapper(discountType.getDiscountLevelState ()) );
        if(discountType.isSimple ()){
            discountTypeWrapper.setCompositeDiscountTypeWrapperType ( DiscountTypeWrapper.DiscountTypeWrapperType.SimpleDiscountFacade );
        }else if(discountType.isConditional ()){
            ConditionalDiscount conditionalDiscount = (ConditionalDiscount) discountType;
            discountTypeWrapper.setCompositeDiscountTypeWrapperType ( DiscountTypeWrapper.DiscountTypeWrapperType.ConditionalDiscountFacade );
            discountTypeWrapper.setConditionWrapper ( ConditionWrapper.createConditionWrapper(conditionalDiscount.getCondition ()) );
        }else{
            MaxCompositeDiscount maxCompositeDiscount = (MaxCompositeDiscount) discountType;
            List<DiscountTypeWrapper> discountTypeWrappers = new ArrayList<> (  );
            for(DiscountType cur : maxCompositeDiscount.getDiscountTypes ()){
                discountTypeWrappers.add ( createDiscountTypeWrapper ( cur ) );
            }
            discountTypeWrapper.setCompositeDiscountTypeWrapperType ( DiscountTypeWrapper.DiscountTypeWrapperType.MaxCompositeDiscountTypeFacade );
            discountTypeWrapper.setDiscountTypeWrappers ( discountTypeWrappers );
        }
        return discountTypeWrapper;
    }


}

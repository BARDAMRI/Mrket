package com.example.server.serviceLayer.FacadeObjects.PolicyFacade;

import com.example.server.businessLayer.Market.Policies.DiscountPolicy.CompositeDiscount.MaxCompositeDiscount;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.Condition.Condition;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.ConditionalDiscount;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.DiscountState.DiscountLevelState;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.DiscountType;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.SimpleDiscount;
import com.example.server.serviceLayer.FacadeObjects.FacadeObject;

public abstract class DiscountTypeFacade implements FacadeObject<DiscountType> {
    protected double percentageOfDiscount;
    protected DiscountLevelStateFacade discountLevelState;

    public DiscountTypeFacade(double percentageOfDiscount, DiscountLevelStateFacade discountLevelState) {
        this.percentageOfDiscount = percentageOfDiscount;
        this.discountLevelState = discountLevelState;
    }

    public DiscountTypeFacade(){}

    public double getPercentageOfDiscount() {
        return percentageOfDiscount;
    }

    public void setPercentageOfDiscount(double percentageOfDiscount) {
        this.percentageOfDiscount = percentageOfDiscount;
    }

    public DiscountLevelStateFacade getDiscountLevelState() {
        return discountLevelState;
    }

    public void setDiscountLevelState(DiscountLevelStateFacade discountLevelState) {
        this.discountLevelState = discountLevelState;
    }

    public abstract DiscountTypeFacade toFacade(SimpleDiscount discount);

    public abstract DiscountTypeFacade toFacade(ConditionalDiscount discount);

    public abstract DiscountTypeFacade toFacade(MaxCompositeDiscount discount);

    public abstract DiscountTypeFacade toFacade(DiscountType discount);

    protected DiscountLevelStateFacade getDiscountLevelStateFacade(DiscountLevelState discountLevelState){
        DiscountLevelStateFacade discountLevelStateFacade;
        if(discountLevelState.isAnd ()){
            discountLevelStateFacade = new AndCompositeDiscountLevelStateFacade (  );
        } else if(discountLevelState.isMaxXor ()){
            discountLevelStateFacade = new MaxXorCompositeDiscountLevelStateFacade (  );
        }else if(discountLevelState.isItem ()){
            discountLevelStateFacade = new ItemLevelStateFacade (  );
        }else if(discountLevelState.isCategory ()){
            discountLevelStateFacade = new CategoryLevelStateFacade (  );
        }else{
            discountLevelStateFacade = new ShopLevelStateFacade ();
        }
        discountLevelStateFacade = discountLevelStateFacade.toFacade ( discountLevelState );
        return discountLevelStateFacade;
    }

    protected ConditionFacade getConditionFacade(Condition condition){
        ConditionFacade conditionFacade;
        if(condition.isAnd ()){
            conditionFacade = new AndCompositeConditionFacade (  );
        } else if(condition.isOr ()){
            conditionFacade = new OrCompositeConditionFacade (  );
        }else if(condition.isAmountOfItem ()){
            conditionFacade = new AmountOfItemConditionFacade (  );
        }else{
            conditionFacade = new PriceConditionFacade (  );
        }
        conditionFacade = conditionFacade.toFacade ( condition );
        return conditionFacade;
    }


    protected DiscountTypeFacade getDiscountType(DiscountType discountType){
        DiscountTypeFacade discountTypeFacade;
        if(discountType.isSimple ()){
            discountTypeFacade = new SimpleDiscountFacade (  );
        } else if(discountType.isConditional ()){
            discountTypeFacade = new ConditionalDiscountFacade (  );
        }else {
            discountTypeFacade = new MaxCompositeDiscountTypeFacade (  );
        }
        discountTypeFacade = discountTypeFacade.toFacade ( discountType );
        return discountTypeFacade;
    }

}

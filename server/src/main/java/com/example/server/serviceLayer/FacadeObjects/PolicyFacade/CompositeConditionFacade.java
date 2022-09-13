package com.example.server.serviceLayer.FacadeObjects.PolicyFacade;

import com.example.server.businessLayer.Market.Appointment.Appointment;
import com.example.server.businessLayer.Market.Appointment.ShopManagerAppointment;
import com.example.server.businessLayer.Market.Appointment.ShopOwnerAppointment;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.Condition.AmountOfItemCondition;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.Condition.CompositionCondition.AndCompositeCondition;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.Condition.CompositionCondition.OrCompositeCondition;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.Condition.Condition;
import com.example.server.businessLayer.Market.Policies.DiscountPolicy.Condition.PriceCondition;
import com.example.server.businessLayer.Market.ResourcesObjects.MarketException;
import com.example.server.serviceLayer.FacadeObjects.AppointmentFacade;

import java.util.ArrayList;
import java.util.List;

public abstract class CompositeConditionFacade extends ConditionFacade{
    protected List<ConditionFacade> conditionFacadeList;

    public CompositeConditionFacade(List<ConditionFacade> conditionFacadeList) {
        this.conditionFacadeList = conditionFacadeList;
    }

    public CompositeConditionFacade(){}

    public List<ConditionFacade> getConditionFacadeList() {
        return conditionFacadeList;
    }

    public void setConditionFacadeList(List<ConditionFacade> conditionFacadeList) {
        this.conditionFacadeList = conditionFacadeList;
    }


}

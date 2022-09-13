package com.example.server.businessLayer.Market.Appointment.Permissions;

import com.example.server.businessLayer.Market.Appointment.Appointment;
import com.example.server.businessLayer.Market.Shop;

import java.util.Map;

public class EmployeesPermission extends IPermission<Map<String, Appointment>> {
    public EmployeesPermission() {
        this.name = "EmployeesPermission";
    }

    @Override
    public Map<String, Appointment> apply(Shop relatedShop) {
        return relatedShop.getEmployees ();
    }

    @Override
    public boolean isPermission(String permission) {
        return permission == name;
    }
}
package com.example.ciklo.service.bill;

import com.example.ciklo.model.Bill;

import java.util.List;

public interface BillService {

    public boolean saveBill(String id,String cusUsername, String driverUsername, String start_loc, String end_loc, String distance, String time);

}

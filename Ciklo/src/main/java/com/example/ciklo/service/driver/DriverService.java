package com.example.ciklo.service.driver;

import com.example.ciklo.model.Bill;
import com.example.ciklo.model.Cyclo;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public interface DriverService {

    List<Bill> viewBillsDriverToday(String username);
    List<Bill> viewBillsDriverMonth(String username);
    Double totalBillsToday(String username);
    Double totalBillsMonth(String username);

    List<Object>totalBillsInAllMonths(String username);
    List<Object> totalTripsInAllMonths(String username);

    List<Bill> getAllBill(String username);

    void changeStatusDriver(String username, boolean status);

}

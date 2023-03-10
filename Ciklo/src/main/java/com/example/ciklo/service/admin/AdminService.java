package com.example.ciklo.service.admin;

import com.example.ciklo.model.Bill;
import com.example.ciklo.model.Cyclo;
import com.example.ciklo.model.Driver;

import java.util.List;
import java.util.Optional;

public interface AdminService {

    List<Bill> viewBillsToday();
    List<Bill> viewBillsMonth();
    Double totalBillsToday();
    Double totalBillsMonth();
    List<Object>totalBillsInAllMonths();
    List<Object> totalTripsInAllMonths();
    List<Bill> getAllBill();
    List<Driver> getAllDriver();

    boolean addDriver(String email, String firstname, String lastname, String pass, String phone);
    boolean removeDriver(int id);
    boolean recoverDriver(int id);
    List<Driver> checkSameMail(String email);


}

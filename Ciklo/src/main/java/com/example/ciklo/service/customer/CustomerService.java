package com.example.ciklo.service.customer;

import com.example.ciklo.model.Bill;

import java.util.List;

public interface CustomerService {
    List<Bill> viewBillsCustomer (String cusID);


}

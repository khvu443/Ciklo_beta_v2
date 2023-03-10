package com.example.ciklo.service.customer;

import com.example.ciklo.model.Bill;
import com.example.ciklo.repository.BillRepo;
import com.example.ciklo.repository.CustomerRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerServiceImpl implements CustomerService {
    @Autowired
    private final CustomerRepo cusRepo;
    @Autowired
    private final BillRepo billRepo;
    @Override
    public List<Bill> viewBillsCustomer(String  email) {
        var cus = cusRepo.findCustomerByCEmail(email).orElseThrow(() -> new UsernameNotFoundException("NOT FOUND CUSTOMER IN VIEW BILL"));
        log.info("cus find in bill {}", cus);
        var all = billRepo.findBillsByCus(cus);
        log.info("all bill: {}", all);
        if (all.isEmpty()) {
            return null;
        }
        return all;

    }
}

package com.example.ciklo.service.homepage;

import com.example.ciklo.model.Admin;
import com.example.ciklo.model.Customer;
import com.example.ciklo.model.Driver;
import com.example.ciklo.repository.AdminRepo;
import com.example.ciklo.repository.CustomerRepo;
import com.example.ciklo.repository.DriverRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class HomepageService {

    private final CustomerRepo cusRep;
    private final DriverRepo driverRep;
    private final AdminRepo adRep;

    public <T> Object findTheInfoUser(String username) {
        Customer cus = cusRep.findCustomerByCEmail(username).orElse(null);
        Driver driver = driverRep.findByDEmail(username).orElse(null);
        Admin ad = adRep.findAdminByAdEmail(username).orElse(null);

        if (cus != null) {
            return cus;
        } else if (driver != null) {
            return driver;
        } else return ad;
    }

}

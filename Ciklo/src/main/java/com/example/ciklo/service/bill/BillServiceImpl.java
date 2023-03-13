package com.example.ciklo.service.bill;

import com.example.ciklo.model.Bill;
import com.example.ciklo.model.Customer;
import com.example.ciklo.model.Cyclo;
import com.example.ciklo.model.Driver;
import com.example.ciklo.repository.BillRepo;
import com.example.ciklo.repository.CustomerRepo;
import com.example.ciklo.repository.CycloRepo;
import com.example.ciklo.repository.DriverRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
@AllArgsConstructor
@Slf4j
public class BillServiceImpl implements BillService {
    private final CustomerRepo cusRepo;
    private final BillRepo billRepo;
    private final DriverRepo driverRepo;
    private final CycloRepo cycloRepo;

    @Override
    public boolean saveBill(String id,String cusUsername, String driverUsername, String start_loc, String end_loc, String distance, String time) {

        Customer cus = cusRepo.findCustomerByCEmail(cusUsername).get();
        Driver driver = driverRepo.findByDEmail(driverUsername).get();
        Cyclo cyclo = cycloRepo.findByDriver(driver).get();
        double total = Double.parseDouble(distance.substring(0, distance.indexOf("km"))) * 10000;

        log.info("Bill save info:{} - {} - {} - {} - {} - {} - {} - {}",id, cus.getCusId(), driver.getDriverId(), cyclo.getId(), start_loc, end_loc, time, distance);
        try {
            if(billRepo.findById(UUID.fromString(id)).isPresent())
            {
                return false;
            }
            else {
                if(driver.isDStatus())
                {
                    Bill bill = new Bill(UUID.fromString(id), cus, driver, cyclo, start_loc, end_loc, distance, time.replace(',', ' '), total);
                    billRepo.save(bill);
                    return true;
                }
                else
                {
                    return false;
                }
            }

        } catch (Exception e) {
            log.error("Something wrong happen when saving bill: {}", e.getMessage());
        }
        return false;
    }

}

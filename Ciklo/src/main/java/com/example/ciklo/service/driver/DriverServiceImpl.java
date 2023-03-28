package com.example.ciklo.service.driver;

import com.example.ciklo.model.Bill;
import com.example.ciklo.model.Cyclo;
import com.example.ciklo.model.Driver;
import com.example.ciklo.repository.BillRepo;
import com.example.ciklo.repository.CycloRepo;
import com.example.ciklo.repository.DriverRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class DriverServiceImpl<T> implements DriverService {

    private final DriverRepo driverRepo;
    private final BillRepo billRepo;

    private final CycloRepo cycloRepo;

    @Override
    public List<Bill> viewBillsDriverToday(String username) {
        Driver driver = driverRepo.findByDEmail(username).get();
        List<Bill> bills = billRepo.getBillsTodayByDriverId(driver.getDriverId());
        log.info(bills.toString());
        if (!bills.isEmpty()) return bills;
        return null;
    }

    @Override
    public List<Bill> viewBillsDriverMonth(String username) {
        Driver driver = driverRepo.findByDEmail(username).get();
        List<Bill> bills = billRepo.getBillsMonthByDriverId(driver.getDriverId());
        if (!bills.isEmpty()) return bills;
        return null;
    }

    @Override
    public Double totalBillsToday(String username) {
        return billRepo.getTotalBillsToday(driverRepo.findByDEmail(username).get().getDriverId()) ==null? 0 : billRepo.getTotalBillsToday(driverRepo.findByDEmail(username).get().getDriverId());
    }

    @Override
    public Double totalBillsMonth(String username) {
        return billRepo.getTotalBillsMonth(driverRepo.findByDEmail(username).get().getDriverId()) == null? 0 : billRepo.getTotalBillsMonth(driverRepo.findByDEmail(username).get().getDriverId());
    }

    @Override
    public List<Object> totalBillsInAllMonths(String username) {
        List<Object> list = new ArrayList<>();
        list.add(billRepo.getTotalBillAllMonthsByDriverId(driverRepo.findByDEmail(username).get().getDriverId()));
        log.info(list.toString());
        return list;
    }

    @Override
    public List<Object> totalTripsInAllMonths(String username) {
        List<Object> list = new ArrayList<>();
        list.add(billRepo.getNumberTripsAllMonthsByDriverId(driverRepo.findByDEmail(username).get().getDriverId()));
        return list;
    }

    @Override
    public List<Bill> getAllBill(String username) {
        return billRepo.findBillsByDriver(driverRepo.findByDEmail(username).get());
    }



    @Override
    public void changeStatusDriver(String username, boolean status) {
        driverRepo.changeStatusDriver(driverRepo.findByDEmail(username).get().getDriverId(), status);
    }

//    @Override
//    public List getFiveLatestBills(String username) {
//        log.info("get latest bill" + billRepo.findFiveNewBills(driverRepo.findByDEmail(username).get().getDriverId()).toString());
//        return billRepo.findFiveNewBills(driverRepo.findByDEmail(username).get().getDriverId());
//    }
}

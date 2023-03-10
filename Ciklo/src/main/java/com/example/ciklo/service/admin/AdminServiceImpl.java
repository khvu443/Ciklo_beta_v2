package com.example.ciklo.service.admin;

import com.example.ciklo.model.Bill;
import com.example.ciklo.model.Cyclo;
import com.example.ciklo.model.Driver;
import com.example.ciklo.repository.BillRepo;
import com.example.ciklo.repository.CycloRepo;
import com.example.ciklo.repository.DriverRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
//@Transactional
public class AdminServiceImpl implements AdminService {

    private final DriverRepo driverRepo;
    private final BillRepo billRepo;
    private final CycloRepo cycloRepo;
    private final PasswordEncoder encoder;

    @Override
    public List<Bill> viewBillsToday() {
        List<Bill> bills = billRepo.getAllBillsToday();
        if (bills.isEmpty()) return null;
        else return bills;
    }

    @Override
    public List<Bill> viewBillsMonth() {

        return billRepo.getBillsMonth().isEmpty() ? null : billRepo.getBillsMonth();
    }

    @Override
    public Double totalBillsToday() {
        return billRepo.getTotalAllBillsToday() == null ? 0 : billRepo.getTotalAllBillsToday();
    }

    @Override
    public Double totalBillsMonth() {
        return billRepo.getTotalAllBillsMonth() == null ? 0 : billRepo.getTotalAllBillsMonth();
    }

    @Override
    public List<Object> totalBillsInAllMonths() {
        List<Object> list = new ArrayList<>();
        list.add(billRepo.getTotalBillAllMonths());
        return list;
    }

    @Override
    public List<Object> totalTripsInAllMonths() {
        List<Object> list = new ArrayList<>();
        list.add(billRepo.getNumberTripsAllMonths());
        return list;
    }

    @Override
    public List<Bill> getAllBill() {
        return billRepo.findAll();
    }

    @Override
    public List<Driver> getAllDriver() {
        log.info(driverRepo.findAll().toString());
        return driverRepo.findAll();
    }


    @Override
    public boolean addDriver(String email, String firstname, String lastname, String pass, String phone) {
        if(email == null || pass == null)
        {
            return false;
        }
        else {
            var driver = Driver.builder()
                    .DEmail(email)
                    .dPassword(encoder.encode(pass))
                    .dFirstname(firstname)
                    .dLastname(lastname)
                    .dPhone(phone)
                    .dStatus(true)
                    .isActive(true)
                    .build();
            try {
                if (driverRepo.findByDEmail(driver.getDEmail()).isPresent()) {
                    log.error("Exist driver {} in DB", driver.getDEmail());
                    return false;
                } else {
                    driverRepo.save(driver);
                    return true;
                }
            } catch (Exception e) {
                log.error("Something wrong happen when add new driver: {}", e.getMessage());
            }
        }
        return false;
    }

    @Override
    public boolean removeDriver(int id) {
        Driver d = driverRepo.findDriverByDriverId(id);

        if (d != null) {
            changeStatusDriver(d, false);
            return true;
        }
        return false;
    }

    @Override
    public boolean recoverDriver(int id) {
        Driver d = driverRepo.findDriverByDriverId(id);
        if (d != null) {
            changeStatusDriver(d, true);
            return true;
        }
        return false;
    }

    @Override
    public List<Driver> checkSameMail(String email) {
        List<Driver> drivers = new ArrayList<>();
        drivers.add(driverRepo.findByDEmail(email).orElse(null));
        return drivers;
    }


    private void changeStatusDriver(Driver d, boolean status) {
        Driver driver = Driver.builder()
                .driverId(d.getDriverId())
                .DEmail(d.getUsername())
                .dFirstname(d.getDFirstname())
                .dLastname(d.getDLastname())
                .dPassword(d.getPassword())
                .dPhone(d.getDPhone())
                .isActive(status)
                .dStatus(d.isDStatus())
                .build();
        driverRepo.save(driver);
    }
}

package com.example.ciklo.service.cyclo;

import com.example.ciklo.model.Cyclo;
import com.example.ciklo.model.Driver;
import com.example.ciklo.repository.CycloRepo;
import com.example.ciklo.repository.DriverRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class CycloServiceImpl implements CycloService {
    @Autowired
    private final DriverRepo driverRepo;
    @Autowired
    private final CycloRepo cycloRepo;

    @Override
    public Boolean changeStatusCyclo(String reg) {
        Cyclo cyclo = getCycloByReg(reg).orElse(null);
        if(cyclo != null)
        {
            try
            {
                cycloRepo.updateStatus(reg,!cyclo.isMaintain());
                return true;
            }
            catch (Exception e)
            {
                log.error("Something happen when update status cyclo: " + e.getMessage());
                return false;
            }
        }
        return false;
    }

    @Override
    public List<Cyclo> getAllCyclo() {
        return cycloRepo.findAll();
    }


    @Override
    public Boolean addCyclo(String reg_no, String username) {
        var d = driverRepo.findByDEmail(username);
        var cyclo = Cyclo.builder()
                .regNo(reg_no)
                .driver(d.get())
                .isMaintain(false)
                .build();
        try {
            cycloRepo.save(cyclo);
            return true;
        } catch (Exception e) {
            log.info("Something happened while saving cyclo: " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<Driver> listAddDriverToCyclo() {
        List<Driver> drivers = driverRepo.findAll();
        for(int i = 0; i< getAllCyclo().size(); i++)
        {
            drivers.remove(getAllCyclo().get(i).getDriver());
        }
        return drivers;
    }

    @Override
    public Optional<Cyclo> getCyclo(String username) {
        return cycloRepo.findByDriver(driverRepo.findByDEmail(username).get());
    }

    @Override
    public Optional<Cyclo> getCycloByReg(String reg)
    {
        return cycloRepo.findByRegNo(reg);
    }

}

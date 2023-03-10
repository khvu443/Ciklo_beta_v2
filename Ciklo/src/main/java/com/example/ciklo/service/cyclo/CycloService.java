package com.example.ciklo.service.cyclo;

import com.example.ciklo.model.Cyclo;
import com.example.ciklo.model.Driver;

import java.util.List;
import java.util.Optional;

public interface CycloService {

    Boolean changeStatusCyclo(String id);
    List<Cyclo> getAllCyclo();
    Boolean addCyclo(String reg_no, String username);
    List<Driver> listAddDriverToCyclo();

    Optional<Cyclo> getCyclo(String username);
    Optional<Cyclo> getCycloByReg(String reg);
}

package com.example.ciklo.controller.cyclo;

import com.example.ciklo.model.Cyclo;
import com.example.ciklo.model.Driver;
import com.example.ciklo.repository.DriverRepo;
import com.example.ciklo.service.cyclo.CycloServiceImpl;
import com.example.ciklo.service.driver.DriverServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@AllArgsConstructor
@RequestMapping("ciklo/cyclo")
public class CycloController {

    private final CycloServiceImpl cycloService;

    //-------------------Cyclo Data--------------------------------------------------------------------------------------
    //Get data cyclo
    @GetMapping("/cyclo_data")
    public ResponseEntity<List<Cyclo>> displayAllCyclos() {
        return new ResponseEntity<>(cycloService.getAllCyclo(), HttpStatus.OK);
    }

    //-------------------Add new Cyclo Data-----------------------------------------------------------------------------
    @PostMapping("/add_cyclo")
    public String addCyclo(HttpServletRequest request) {

        return String.valueOf(cycloService.addCyclo(
                request.getParameter("reg_no"),
                request.getParameter("demail")
        ));
    }

    @GetMapping("/driver_for_cyclo")
    public ResponseEntity<List<Driver>> listAddDriver() {
        return new ResponseEntity<>(cycloService.listAddDriverToCyclo(), HttpStatus.OK);
    }

    //-----------------------Change Status of Cyclo---------------------------------------------------------------------
    @PostMapping("/status_cyclo")
    public ResponseEntity<HashMap<String, String>> changeStatusCyclo(HttpServletRequest request)
    {
        String reg = request.getParameter("reg");
        HashMap<String, String > maps = new HashMap<>();
        maps.put("isSuccess", String.valueOf(cycloService.changeStatusCyclo(reg)));
        maps.put("maintain", String.valueOf(cycloService.getCycloByReg(reg).get().isMaintain()));
        return new ResponseEntity<>(maps, HttpStatus.OK);
    }
    //-----------------------Get Cyclo Base on Driver-------------------------------------------------------------------
    @GetMapping("/cyclo_driver")
    public ResponseEntity<Optional<Cyclo>> displayCyclo() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            return new ResponseEntity<Optional<Cyclo>>(cycloService.getCyclo(auth.getName()), HttpStatus.OK);
        }
        return null;
    }

}

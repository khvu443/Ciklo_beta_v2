package com.example.ciklo.controller.homepage;

import com.example.ciklo.model.Admin;
import com.example.ciklo.model.Customer;
import com.example.ciklo.model.Driver;
import com.example.ciklo.service.admin.AdminDetailServiceImpl;
import com.example.ciklo.service.customer.CustomerDetailServiceImpl;
import com.example.ciklo.service.driver.DriverDetailServiceImpl;
import com.example.ciklo.service.homepage.HomepageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

@RestController
@RequestMapping("/ciklo/homepage")
@RequiredArgsConstructor
@Slf4j
public class HomepageController {

    private final HomepageService homeService;

    private final CustomerDetailServiceImpl cusDetailService;
    private final AdminDetailServiceImpl adminDetailService;
    private final DriverDetailServiceImpl driverDetailService;

    ModelAndView mav = new ModelAndView();

    @GetMapping("/")
    public ModelAndView Homepage() {
        mav.setViewName("homepage/index");
//
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        log.info("context holder - " + auth.getName());
        return mav;
    }

    @GetMapping("/userInfo")
    public ResponseEntity<HashMap<String, String>> userInfo()
    {
        HashMap<String, String> map = new HashMap<>();
        Customer cus = null;
        Driver driver = null;
        Admin ad = null;

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("context holder - " + auth.getAuthorities().iterator().next().toString().equals("CUSTOMER"));
        log.info("context holder - " + auth.getAuthorities().iterator().next().toString().equals("DRIVER"));
        log.info("context holder - " + auth.getAuthorities().iterator().next().toString().equals("ADMIN"));

        if (auth.getAuthorities().iterator().next().toString().equals("CUSTOMER")) {
            cus = (Customer) homeService.findTheInfoUser(auth.getName());
            map.put("name", cus.getCEmail());
            map.put("role", cusDetailService.loadUserByUsername(auth.getName()).getAuthorities().iterator().next().toString());
        }

        if (auth.getAuthorities().iterator().next().toString().equals("DRIVER")) {
            driver = (Driver) homeService.findTheInfoUser(auth.getName());
            map.put("name", driver.getDEmail());
            map.put("role",driverDetailService.loadUserByUsername(auth.getName()).getAuthorities().iterator().next().toString());
        }

        if (auth.getAuthorities().iterator().next().toString().equals("ADMIN")) {
            ad = (Admin) homeService.findTheInfoUser(auth.getName());
            map.put("name", ad.getAdEmail());
            map.put("role", adminDetailService.loadUserByUsername(auth.getName()).getAuthorities().iterator().next().toString());
        }

        return new ResponseEntity<>(map, HttpStatus.OK);

    }
}

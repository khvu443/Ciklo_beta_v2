package com.example.ciklo.controller.driver;

import com.example.ciklo.model.Bill;
import com.example.ciklo.model.Cyclo;
import com.example.ciklo.service.driver.DriverServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
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
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ciklo/driver")
@RequiredArgsConstructor
@Slf4j
public class DriverPageController<T> {

    private final DriverServiceImpl driverService;

    @GetMapping("/")
    public ModelAndView driverpage() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("driverpage/driverpage");
        return mav;
    }

    @GetMapping("/statistic")
    public ResponseEntity<HashMap<String, String>> statisticDriver(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("statistic "+auth.getName());
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            String username = auth.getName();
            // number trip that driver has taken today
            int number_of_trip_today = driverService.viewBillsDriverToday(username) == null ? 0 : driverService.viewBillsDriverToday(username).size();
            // total invoice trip that driver has taken today
            double total_today = driverService.totalBillsToday(username);
            // number trip that driver has taken in one month
            int number_of_trip_month = driverService.viewBillsDriverMonth(username) == null ? 0 : driverService.viewBillsDriverMonth(username).size();
            // total invoice trip that driver has taken in one month
            double total_month = driverService.totalBillsMonth(username);


            HashMap<String, String> statisticMap = new HashMap<>();
            statisticMap.put("today_trips", String.valueOf(number_of_trip_today));
            statisticMap.put("total_today_trips", String.valueOf(total_today));
            statisticMap.put("month_trips", String.valueOf(number_of_trip_month));
            statisticMap.put("total_month_trips", String.valueOf(total_month));
            statisticMap.put("name", username);

            return new ResponseEntity<>(statisticMap, HttpStatus.OK);
        }
        return null;
    }

    @GetMapping("/chartBills")
    public ResponseEntity<List<Object>> chartBillsDriver() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            return new ResponseEntity<List<Object>>(driverService.totalBillsInAllMonths(auth.getName()), HttpStatus.OK);
        }
        return null;
    }

    @GetMapping("/chartTrips")
    public ResponseEntity<List<Object>> chartTripsDriver() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            return new ResponseEntity<List<Object>>(driverService.totalTripsInAllMonths(auth.getName()), HttpStatus.OK);
        }
        return null;
    }

    @GetMapping("/list_bills")
    public ResponseEntity<List<Bill>> displayAllBill() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            return new ResponseEntity<List<Bill>>(driverService.getAllBill(auth.getName()), HttpStatus.OK);
        }
        return null;
    }

    @PostMapping("/change-status")
    public void changeStatusDriver(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            log.info("change status");
            driverService.changeStatusDriver(
                    auth.getName(),
                    Boolean.parseBoolean(request.getParameter("status"))
            );
        }
    }

//    @GetMapping("/latest")
//    public ResponseEntity latestBills() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        if (!(auth instanceof AnonymousAuthenticationToken)) {
//            return new ResponseEntity<>(driverService.getFiveLatestBills(auth.getName()), HttpStatus.OK);
//        }
//        return null;
//    }
}

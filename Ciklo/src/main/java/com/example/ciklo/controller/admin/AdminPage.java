package com.example.ciklo.controller.admin;

import com.example.ciklo.model.Bill;
import com.example.ciklo.model.Cyclo;
import com.example.ciklo.model.Driver;
import com.example.ciklo.service.admin.AdminServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
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

@RestController
@RequestMapping("/ciklo/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminPage {
    private final AdminServiceImpl adService;

    @GetMapping("/")
    public ModelAndView adminPage() {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("adminpage/adminpage");
        return mav;
    }

    //-----------Set name user to page--------------------------------------------
    @GetMapping("/user")
    public ResponseEntity<HashMap<String, String>> userDetail() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        HashMap<String, String> user = new HashMap<>();
        user.put("user", auth.getName());
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    //-------------------satistic Data---------------------------
    @GetMapping("/statistic")
    public ResponseEntity<HashMap<String, String>> statisticAdmin(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            // number trip that driver has taken today
            int number_of_trip_today = adService.viewBillsToday() == null ? 0 : adService.viewBillsToday().size();
            // total invoice trip that driver has taken today
            double total_today = adService.totalBillsToday();
            // number trip that driver has taken in one month
            int number_of_trip_month = adService.viewBillsMonth() == null ? 0 : adService.viewBillsMonth().size();
            // total invoice trip that driver has taken in one month
            double total_month = adService.totalBillsMonth();


            HashMap<String, String> statisticMap = new HashMap<>();
            statisticMap.put("today_trips", String.valueOf(number_of_trip_today));
            statisticMap.put("total_today_trips", String.valueOf(total_today));
            statisticMap.put("month_trips", String.valueOf(number_of_trip_month));
            statisticMap.put("total_month_trips", String.valueOf(total_month));

            return new ResponseEntity<>(statisticMap, HttpStatus.OK);
        }
        return null;
    }

    //===================chart for admin==================================
    //Chart Bill
    @GetMapping("/chart_bill")
    public ResponseEntity<List<Object>> chartBillAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            return new ResponseEntity<>(adService.totalBillsInAllMonths(), HttpStatus.OK);
        }
        return null;
    }

    //Chart Trip
    @GetMapping("/chart_trip")
    public ResponseEntity<List<Object>> chartTripAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            return new ResponseEntity<>(adService.totalTripsInAllMonths(), HttpStatus.OK);
        }
        return null;
    }


    //===================Data for admin==================================

    //-------------------Driver Data-------------------------------------
    //Get Data Driver
    @GetMapping("/driver_data")
    public ResponseEntity<List<Driver>> displayAllDrivers() {
        return new ResponseEntity<>(adService.getAllDriver(), HttpStatus.OK);
    }

    @GetMapping("/check_driver")
    public ResponseEntity<List<Driver>> checkDuplicateEmail(HttpServletRequest request)
    {

        return new ResponseEntity<>(
                adService.checkSameMail(request.getParameter("email")),
                HttpStatus.OK);
    }

    @PostMapping("/add_driver")
    public String addDriver(@NonNull HttpServletRequest request) {
        return String.valueOf(adService.addDriver(
                request.getParameter("email").isEmpty() ? null : request.getParameter("email"),
                request.getParameter("fname"),
                request.getParameter("lname"),
                request.getParameter("pass").isEmpty() ? null : request.getParameter("pass"),
                request.getParameter("phone")
        ));
    }

    //Delete driver
    @PostMapping("/delete_driver")
    public String removeDriver(@NonNull HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));
        log.info("change status account driver {} of driver is false", id);
        return String.valueOf(adService.removeDriver(id));

    }

    //recover driver
    @PostMapping("/recover_driver")
    public String recoverDriver(HttpServletRequest request) {
        int id = Integer.parseInt(request.getParameter("id"));

        return String.valueOf(adService.recoverDriver(id));
    }

    //-------------------Bill Data--------------------------------------------------------------------------------------
    //Get data bill
    @GetMapping("/bill_data")
    public ResponseEntity<List<Bill>> displayAllBills() {
        return new ResponseEntity<>(adService.getAllBill(), HttpStatus.OK);
    }
}



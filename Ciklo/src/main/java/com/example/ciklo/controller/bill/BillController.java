package com.example.ciklo.controller.bill;

import com.example.ciklo.model.Bill;
import com.example.ciklo.service.bill.BillServiceImpl;
import com.example.ciklo.service.customer.CustomerServiceImpl;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ciklo/bill")
@RequiredArgsConstructor
@Slf4j

public class BillController {
    private final BillServiceImpl billService;
    private final CustomerServiceImpl customerService;

    @PostMapping("/saveBill")
    public String saveBill(HttpServletRequest request) {
        String id = request.getParameter("id");
        String driver = request.getParameter("driver");
        String rider = request.getParameter("rider");
        String begin = request.getParameter("begin");
        String des = request.getParameter("des");
        String time = request.getParameter("time");
        String distance = request.getParameter("distance");

//        log.info("bill save" + driver, rider, begin, des, time, distance);
        return String.valueOf(billService.saveBill(id,rider, driver, begin, des, distance, time));

    }

    @GetMapping("/viewBill")
    public ResponseEntity<List<Bill>> viewBill(HttpServletRequest request)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        List<Bill> bills = customerService.viewBillsCustomer(auth.getName());
        return new ResponseEntity<>(bills, HttpStatus.OK);
    }
}

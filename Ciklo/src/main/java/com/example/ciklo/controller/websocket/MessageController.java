package com.example.ciklo.controller.websocket;

import com.example.ciklo.model.Driver;
import com.example.ciklo.model.Message;
import com.example.ciklo.repository.CycloRepo;
import com.example.ciklo.repository.DriverRepo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@AllArgsConstructor
@Slf4j
public class MessageController {
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    private final DriverRepo driverRepo;
    private final CycloRepo cycloRepo;

    @MessageMapping("/application")
    @SendTo("/all/messages")
    public void sendDriver(@Payload Message msg) throws Exception {
        List<Driver> driverList = driverRepo.findAll();
        for (Driver driver : driverList) {
//            log.info(String.valueOf(driver.isDStatus() && !cycloRepo.findByDriver(driver).get().isMaintain()));
            if (driver.isDStatus() && !cycloRepo.findByDriver(driver).get().isMaintain()) {
                simpMessagingTemplate.convertAndSendToUser(driver.getDEmail(), "/driver", msg);
            }
        }

    }

    @MessageMapping("/rider")
//    @SendTo("/all/noticeRider")
    public void sendRider(@Payload Message msg) throws Exception {

        simpMessagingTemplate.convertAndSendToUser(msg.getRider(), "/rider", msg);

    }
}

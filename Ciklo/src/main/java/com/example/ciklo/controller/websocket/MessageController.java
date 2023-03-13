package com.example.ciklo.controller.websocket;

import com.example.ciklo.model.Cyclo;
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
import java.util.Objects;

@Controller
@AllArgsConstructor
@Slf4j
public class MessageController {
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    private final DriverRepo driverRepo;
    private final CycloRepo cycloRepo;

    @MessageMapping("/private_driver")
    public void sendDriver(@Payload Message msg) throws Exception {
        List<Cyclo> list = cycloRepo.findAll();
        log.info("message send to driver " + msg);
        for (Cyclo c : list) {
//            log.info(String.valueOf(driver.isDStatus() && !cycloRepo.findByDriver(driver).get().isMaintain()));
            if (!c.isMaintain() ) {
                log.info("get driver that cyclo doesn't maintain: " + c.getDriver().getDEmail());
                if(c.getDriver().isDStatus())
                {
                    log.info("get driver that's free: " + c.getDriver().getDEmail());
                    simpMessagingTemplate.convertAndSendToUser(c.getDriver().getDEmail(), "/driver", msg);
                }
            }
        }
    }

    @MessageMapping("/private_cancel")
    public void sendCancelDriver(@Payload Message msg) throws Exception {
        List<Cyclo> list = cycloRepo.findAll();
        log.info("message send to driver " + msg);
        for (Cyclo c : list) {
//            log.info(String.valueOf(driver.isDStatus() && !cycloRepo.findByDriver(driver).get().isMaintain()));
            if (!c.isMaintain() ) {
                log.info("get driver that cyclo doesn't maintain: " + c.getDriver().getDEmail());
                if(c.getDriver().isDStatus())
                {
                    log.info("get driver that's free: " + c.getDriver().getDEmail());
                    simpMessagingTemplate.convertAndSendToUser(c.getDriver().getDEmail(), "/cancel", msg);
                }
            }
        }
    }


    @MessageMapping("/private_rider")
//    @SendTo("/all/noticeRider")
    public void sendRider(@Payload Message msg) throws Exception {

        simpMessagingTemplate.convertAndSendToUser(msg.getRider(), "/rider", msg);

    }
}

package com.example.ciklo.auth.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@AllArgsConstructor
public class EmailService implements EmailSender {

    @Autowired
    private final JavaMailSender mailSender;

    @Override
    @Async
    public void send(String to, String email) {
        try
        {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(email, true);
            helper.setTo(to);
            helper.setSubject("Confirm Your Email");
            helper.setFrom("ciklo_admin@gmail.com");
            mailSender.send(mimeMessage);
        }
        catch(MessagingException e)
        {
            log.error("failed to send email: ", e);
            throw new IllegalStateException("Failed to send email");
        }
    }

}

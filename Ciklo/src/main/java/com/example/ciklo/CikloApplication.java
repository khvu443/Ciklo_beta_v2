package com.example.ciklo;

import com.example.ciklo.model.Admin;
import com.example.ciklo.repository.AdminRepo;
import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@AllArgsConstructor
@SpringBootApplication
public class CikloApplication {
    private final PasswordEncoder encoder;
    private final AdminRepo adRep;

    public static void main(String[] args) {
        SpringApplication.run(CikloApplication.class, args);
    }
    @Bean
    public void addAdmin()
    {
        var ad = Admin.builder()
                .adEmail("admin@gmail.com")
                .adLastname("vu")
                .adFirstname("admin")
                .adPhone("00928391723")
                .adPassword(encoder.encode("1234"))
                .build();
        adRep.save(ad);
    }

}

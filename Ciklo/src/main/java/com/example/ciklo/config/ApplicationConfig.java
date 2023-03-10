package com.example.ciklo.config;

import com.example.ciklo.repository.AdminRepo;
import com.example.ciklo.repository.CustomerRepo;
import com.example.ciklo.repository.DriverRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class ApplicationConfig {
    private final CustomerRepo cusRep;
    private final AdminRepo adRep;
    private final DriverRepo driverRep;

    @Bean
    public UserDetailsService userDetailsService() {
        return username ->
        {
            if(cusRep.findCustomerByCEmail(username).isPresent())
            {
                cusRep.findCustomerByCEmail(username);
            }
            else
            if(driverRep.findByDEmail(username).isPresent())
            {
                driverRep.findByDEmail(username);
            }
            else
            if(adRep.findAdminByAdEmail(username).isPresent())
            {
                adRep.findAdminByAdEmail(username);
            }
            else
            {
                throw new UsernameNotFoundException("User not found");
            }
            return null;
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider()
    {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}

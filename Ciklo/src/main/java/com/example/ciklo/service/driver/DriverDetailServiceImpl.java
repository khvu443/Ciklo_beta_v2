package com.example.ciklo.service.driver;

import com.example.ciklo.model.Driver;
import com.example.ciklo.repository.DriverRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class DriverDetailServiceImpl implements UserDetailsService {

    @Autowired
    private final DriverRepo driverRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Driver> driver = driverRepo.findByDEmail(username);
        if(driver.isEmpty())
        {
            return null;
        }
        else {
            List<GrantedAuthority> listAuthorities = new ArrayList<GrantedAuthority>();
            listAuthorities.add(new SimpleGrantedAuthority("DRIVER"));
            return new org.springframework.security.core.userdetails.User(username, driver.get().getDPassword(), true, true, true, true, listAuthorities);
        }
    }
}

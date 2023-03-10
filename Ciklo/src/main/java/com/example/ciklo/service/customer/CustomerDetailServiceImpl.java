package com.example.ciklo.service.customer;

import com.example.ciklo.model.Customer;
import com.example.ciklo.repository.CustomerRepo;
import jakarta.transaction.Transactional;
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
public class CustomerDetailServiceImpl implements UserDetailsService {
    @Autowired
    private CustomerRepo customerRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Customer> cus = customerRepo.findCustomerByCEmail(username);
        if(cus.isPresent())
        {
            List<GrantedAuthority> listAuthorities = new ArrayList<GrantedAuthority>();
            listAuthorities.add(new SimpleGrantedAuthority("CUSTOMER"));
            return new org.springframework.security.core.userdetails.User(username, cus.get().getCPassword(), cus.get().isEnabled(), true, true, cus.get().isAccountNonLocked(), listAuthorities);
        }
        else {
           return null;
        }
    }
}

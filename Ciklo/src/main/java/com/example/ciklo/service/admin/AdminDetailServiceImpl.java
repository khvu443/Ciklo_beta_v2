package com.example.ciklo.service.admin;

import com.example.ciklo.model.Admin;
import com.example.ciklo.repository.AdminRepo;
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
public class AdminDetailServiceImpl implements UserDetailsService {

    @Autowired
    private AdminRepo adRepo;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Admin> ad = adRepo.findAdminByAdEmail(username);
        if(ad.isEmpty())
        {
            return null;
        }
        else {
            List<GrantedAuthority> listAuthorities = new ArrayList<GrantedAuthority>();
            listAuthorities.add(new SimpleGrantedAuthority("ADMIN"));
            return new org.springframework.security.core.userdetails.User(username, ad.get().getAdPassword(), true, true, true, true, listAuthorities);
        }
    }

}

package com.example.ciklo.repository;

import com.example.ciklo.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Optional;

@EnableJpaRepositories
public interface AdminRepo extends JpaRepository<Admin, Integer> {
    Optional<Admin> findAdminByAdEmail(String email);
    Admin findAdminByAdId(int id);
}

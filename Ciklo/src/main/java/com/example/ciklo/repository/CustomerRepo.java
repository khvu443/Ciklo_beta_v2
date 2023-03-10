package com.example.ciklo.repository;

import com.example.ciklo.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
@Repository
@Transactional
@EnableJpaRepositories
public interface CustomerRepo extends JpaRepository<Customer, Integer> {
    Optional<Customer> findCustomerByCEmail(String email);
    Optional<Customer> findCustomerByCusId(int cusId);

    @Transactional
    @Modifying
    @Query("UPDATE Customer cus " +
            "SET cus.enabled = TRUE WHERE cus.CEmail = ?1")
    int enableAppUser(String email);
}

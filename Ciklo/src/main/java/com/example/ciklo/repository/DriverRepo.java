package com.example.ciklo.repository;

import com.example.ciklo.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
@Transactional
//@EnableJpaRepositories
public interface DriverRepo extends JpaRepository<Driver, Integer> {
    Optional<Driver> findByDEmail(String email);
    Driver findDriverByDriverId(int id);
    @Modifying
    @Query("UPDATE Driver d set d.dStatus = ?2 where d.driverId= ?1")
    void changeStatusDriver(int id, boolean status);
}

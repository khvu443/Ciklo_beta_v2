package com.example.ciklo.repository;

import com.example.ciklo.model.Cyclo;
import com.example.ciklo.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

//@EnableJpaRepositories
@Repository
@Transactional
@EnableJpaRepositories
public interface CycloRepo extends JpaRepository<Cyclo, Integer> {
    Optional<Cyclo> findByDriver(Driver driver);
    Optional<Cyclo> findByRegNo(String reg_no);

    @Modifying
    @Query("update Cyclo c set c.isMaintain= ?2 where c.regNo = ?1")
    void updateStatus(String reg, boolean maintain);
}

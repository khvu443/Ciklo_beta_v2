package com.example.ciklo.repository;

import com.example.ciklo.model.ConfirmToken;
import com.example.ciklo.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@Transactional
public interface ConfirmTokenRepository extends JpaRepository<ConfirmToken,Long> {
    Optional<ConfirmToken> findByToken(String token);

    @Transactional
    @Modifying
    @Query("update ConfirmToken c set c.confirmAt = ?2 where c.token = ?1")
    int UpdateConfirmAt(String token, LocalDateTime confirmAt);

    Optional<ConfirmToken> findConfirmTokenByCus(Customer cus);
}

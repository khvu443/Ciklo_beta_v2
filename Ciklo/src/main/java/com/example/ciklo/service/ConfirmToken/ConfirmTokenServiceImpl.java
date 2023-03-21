package com.example.ciklo.service.ConfirmToken;

import com.example.ciklo.model.ConfirmToken;
import com.example.ciklo.model.Customer;
import com.example.ciklo.repository.ConfirmTokenRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class ConfirmTokenServiceImpl implements ConfirmTokenService{
    private final ConfirmTokenRepository confirmTokenRepository;

    @Override
    public void saveConfirmToken(ConfirmToken token) {
        confirmTokenRepository.save(token);
    }

    @Override
    public Optional<ConfirmToken> getConfirmToken(String token) {
        return confirmTokenRepository.findByToken(token);
    }

    @Override
    public int setConfirmToken(String token) {

        return confirmTokenRepository.UpdateConfirmAt(token, LocalDateTime.now());
    }

    @Override
    public Optional<ConfirmToken> getConfirmTokenByCustomer(Customer cus) {
        return confirmTokenRepository.findConfirmTokenByCus(cus);
    }


}

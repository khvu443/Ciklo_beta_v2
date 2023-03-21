package com.example.ciklo.service.ConfirmToken;

import com.example.ciklo.model.ConfirmToken;
import com.example.ciklo.model.Customer;

import java.util.Optional;

public interface ConfirmTokenService {

    public void saveConfirmToken(ConfirmToken token);

    public Optional<ConfirmToken> getConfirmToken(String token);

    public int setConfirmToken(String token);

    public Optional<ConfirmToken> getConfirmTokenByCustomer(Customer id);
}

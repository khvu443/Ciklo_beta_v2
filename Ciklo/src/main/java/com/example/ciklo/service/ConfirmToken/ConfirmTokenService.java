package com.example.ciklo.service.ConfirmToken;

import com.example.ciklo.model.ConfirmToken;

import java.util.Optional;

public interface ConfirmTokenService {

    public void saveConfirmToken(ConfirmToken token);

    public Optional<ConfirmToken> getConfirmToken(String token);

    public int setConfirmToken(String token);
}

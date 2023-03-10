package com.example.ciklo.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterCusRequest {
    private String cFirstname;
    private String cLastname;
    private String cEmail;
    private String cPassword;
    private String cPhone;

    private Collection<String> roles = new ArrayList<>(Collections.singleton("CUSTOMER"));
}

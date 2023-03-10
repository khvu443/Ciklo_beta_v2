package com.example.ciklo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private UUID id;
    private String rider;
    private String driver;
    private String begin;
    private String des;
    private String time;
    private String distance;
}

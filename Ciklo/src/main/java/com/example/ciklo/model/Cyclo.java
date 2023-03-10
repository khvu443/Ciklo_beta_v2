package com.example.ciklo.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Builder
@Table(name = "_cyclo")
public class Cyclo
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String regNo;

    private boolean isMaintain;

//    @OneToMany(fetch = FetchType.EAGER)
//    @Transient
//    private List<Bill> bill = new ArrayList<>();

    @OneToOne(fetch = FetchType.EAGER)
    private Driver driver;

}

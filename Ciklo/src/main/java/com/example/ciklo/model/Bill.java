package com.example.ciklo.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Nationalized;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "_bill")
public class Bill {
    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private UUID bid;
    @ManyToOne(fetch = FetchType.EAGER)
    @ToString.Exclude
    private Customer cus;
    @ManyToOne(fetch = FetchType.EAGER)
    @ToString.Exclude
    private Driver driver;
    @ManyToOne(fetch = FetchType.EAGER)
    @ToString.Exclude
    private Cyclo cyclo;
    @Nationalized
    private String start_loc;
    @Nationalized
    private String end_loc;
    private String distance;
    private String dateTrip;
    private double total;

    public Bill(Customer cus, Driver driver, Cyclo cyclo, String startLoc, String endLoc, String distance, String time, double total) {
        this.cus = cus;
        this.driver = driver;
        this.cyclo= cyclo;
        this.start_loc = startLoc;
        this.end_loc=endLoc;
        this.distance=distance;
        this.dateTrip = time;
        this.total = total;
    }
}

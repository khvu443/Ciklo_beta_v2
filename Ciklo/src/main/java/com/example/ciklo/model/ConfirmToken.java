package com.example.ciklo.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Data
public class ConfirmToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable=false)
    private Long id;
    @Column(nullable = false)
    private String token;
    @Column(nullable = false)
    private LocalDateTime createAt;
    private LocalDateTime expireAt;
    private LocalDateTime confirmAt;
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "customer_id"
    )
    private Customer cus;

    public ConfirmToken(String token,
                        LocalDateTime createAt,
                        LocalDateTime expiredAt,
                        Customer cus) {
        this.token = token;
        this.createAt = createAt;
        this.expireAt = expiredAt;
        this.cus = cus;
    }
}

package com.cse308.sbuify.label;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class RoyaltyPayment implements Serializable {
    @Id
    private Integer id;
    private Double amount;
    private LocalDateTime startPeriod;
    private LocalDateTime endPeriod;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private RecordLabel payee;


}

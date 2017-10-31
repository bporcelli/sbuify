package com.sbuify.SBUify.domain;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
public class RoyaltyPayment {
    private Integer id;
    private Double amount;
    private LocalDateTime startPeriod;
    private LocalDateTime endPeriod;
    @Enumerated(EnumType.STRING)
    private PaymentStatus status;
    private RecordLabel payee;


}

package com.cse308.sbuify.label.payment;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PaymentPeriodRepository  extends CrudRepository<PaymentPeriod, Integer> {
    List<PaymentPeriod> findAllByOrderByEndDesc();
}

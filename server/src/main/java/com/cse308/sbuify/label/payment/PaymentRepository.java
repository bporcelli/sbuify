package com.cse308.sbuify.label.payment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface PaymentRepository extends CrudRepository<Payment, Integer> {
    Page<Payment> findAllByStatusAndPeriod_Id(PaymentStatus status, Integer periodId, Pageable pageable);

    Page<Payment> findAllByStatus(PaymentStatus status, Pageable pageable);

    Page<Payment> findAllByPeriod_Id(Integer period, Pageable pageable);

    Page<Payment> findAll(Pageable pageable);
}

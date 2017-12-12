package com.cse308.sbuify.customer;

import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;

public interface SubscriptionRepository extends CrudRepository<Subscription, Integer> {
    Integer countAllByEndBetween(LocalDateTime start, LocalDateTime end);
    Integer countAllByStartBetween(LocalDateTime start, LocalDateTime end);
    Integer countAllByEndIsNull();
}

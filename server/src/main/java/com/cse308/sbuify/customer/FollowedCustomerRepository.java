package com.cse308.sbuify.customer;

import com.cse308.sbuify.user.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface FollowedCustomerRepository extends CrudRepository<FollowedCustomer, Integer> {
    Optional<FollowedCustomer> findByCustomerAndFriend_Id(User customer, Integer friendId);

    @Modifying
    void deleteByCustomerAndFriend_Id(User customer, Integer friendId);

    boolean existsByCustomerAndFriend_Id(User customer, Integer friendId);

    List<FollowedCustomer> findAllByCustomer(User customer);
}

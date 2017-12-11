package com.cse308.sbuify.customer;

import com.cse308.sbuify.common.api.ResponseDecorator;
import com.cse308.sbuify.security.AuthFacade;
import com.cse308.sbuify.user.User;

public class CustomerDecorator implements ResponseDecorator<Customer> {

    private AuthFacade authFacade;

    private FollowedCustomerRepository followedCustomerRepo;

    public CustomerDecorator(AuthFacade authFacade, FollowedCustomerRepository followedCustomerRepo) {
        this.authFacade = authFacade;
        this.followedCustomerRepo = followedCustomerRepo;
    }

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(Customer.class);
    }

    @Override
    public void decorate(Customer customer) {
        User current = authFacade.getCurrentUser();

        // add "followed" flag
        Boolean followed = followedCustomerRepo.existsByCustomerAndFriend_Id(current, customer.getId());
        customer.set("followed", followed);
    }
}

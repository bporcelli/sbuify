package com.cse308.sbuify.user;

import com.cse308.sbuify.common.api.ResponseDecorator;
import com.cse308.sbuify.customer.Customer;
import com.cse308.sbuify.customer.FollowedCustomerRepository;
import com.cse308.sbuify.security.AuthFacade;

public class UserDecorator implements ResponseDecorator<User> {

    private AuthFacade authFacade;

    private FollowedCustomerRepository followedCustomerRepo;

    public UserDecorator(AuthFacade authFacade, FollowedCustomerRepository followedCustomerRepo) {
        this.authFacade = authFacade;
        this.followedCustomerRepo = followedCustomerRepo;
    }

    @Override
    public boolean supports(Class<?> type) {
        return type.equals(User.class);
    }

    @Override
    public void decorate(User user) {
        if (user instanceof Customer) {
            decorateCustomer((Customer) user);
        }
    }

    private void decorateCustomer(Customer user) {
        User current = authFacade.getCurrentUser();

        // add "followed" flag
        Boolean followed = followedCustomerRepo.existsByCustomerAndFriend_Id(current, user.getId());
        user.set("followed", followed);
    }
}

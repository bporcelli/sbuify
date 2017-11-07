package com.cse308.sbuify.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.cse308.sbuify.user.User;

@Component
public class AuthFacade {

    public User getCurrentUser() {
        Authentication token = SecurityContextHolder.getContext().getAuthentication();
        if (token != null) {
            return (User) token.getPrincipal();
        }
        return null;
    }

}

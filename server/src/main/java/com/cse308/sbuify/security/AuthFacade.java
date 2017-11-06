package com.cse308.sbuify.security;

import com.cse308.sbuify.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

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

package com.cse308.sbuify.security;

import com.cse308.sbuify.user.AppUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AuthFacade {

    public AppUser getCurrentUser() {
        Authentication token = SecurityContextHolder.getContext().getAuthentication();
        if (token != null) {
            return (AppUser) token.getPrincipal();
        }
        return null;
    }

}

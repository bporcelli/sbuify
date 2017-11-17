package com.cse308.sbuify.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.cse308.sbuify.user.User;
import com.cse308.sbuify.user.UserRepository;

@Component
public class AuthFacade {

    @Autowired
    private UserRepository userRepo;
    
    @Autowired
    private AuthFacade authFacade;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public User getCurrentUser() {
        Authentication token = SecurityContextHolder.getContext().getAuthentication();

        if (token != null) {
            if (token.getPrincipal() instanceof User) {
                return (User) token.getPrincipal();
            } else {
                logger.warn("AuthFacade:getCurrentUser() token.getPricipal().getClass: " + token.getPrincipal().getClass());
                throw new ClassCastException();
            }
        }
        return null;
    }

}

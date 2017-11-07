package com.cse308.sbuify.security;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cse308.sbuify.user.User;
import com.cse308.sbuify.user.UserRepository;

/**
 * User Details Service.
 *
 * Loads user-specific data for the Spring Security Framework.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> result = userRepository.findByEmail(s);

        if (!result.isPresent()) {
            throw new UsernameNotFoundException("Invalid email address.");
        }

        return result.get();
    }
}

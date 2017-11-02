package com.cse308.sbuify.security;

import com.cse308.sbuify.user.User;
import com.cse308.sbuify.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.util.Collections.emptyList;

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

        User user = result.get();

        // TODO: set GrantedAuthorities based on user role
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), emptyList());
    }

}

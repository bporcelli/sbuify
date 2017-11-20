package com.cse308.sbuify.user;

import java.util.Optional;

import com.cse308.sbuify.customer.Customer;
import com.cse308.sbuify.email.Email;
import com.cse308.sbuify.email.NewAccountEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Register a user.
     *
     * @return ResponseEntity<>
     */
    @PostMapping
    public ResponseEntity<?> create(@RequestBody User user) {
        Optional<User> existing = userRepository.findByEmail(user.getEmail());

        // Return a 409 response if this email is already in use
        if (existing.isPresent()) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        // Hash password and save user
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = userRepository.save(user);

        Email userRegistration = new NewAccountEmail(user);

        if (!userRegistration.dispatch()) { // email delivery failed -- return 500
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
    
}

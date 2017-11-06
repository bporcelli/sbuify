package com.cse308.sbuify.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

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
     * NOTE: The response body MUST include an empty JSON object or Angular will treat successful responses as errors.
     * See https://github.com/angular/angular/issues/18680.
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
        userRepository.save(user);

        return new ResponseEntity<>("{}", HttpStatus.CREATED);
    }
}

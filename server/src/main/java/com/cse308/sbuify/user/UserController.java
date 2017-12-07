package com.cse308.sbuify.user;

import com.cse308.sbuify.email.Email;
import com.cse308.sbuify.email.NewAccountEmail;
import com.cse308.sbuify.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

    /**
     * Change a user's password.
     * @param userId ID of user.
     * @param password New password for user.
     * @return Http.OK successful, Http.FORBIDDEN, no permission, Http.NOT_FOUND, cannot find user
     */
    @PostMapping(path = "/{id}/change-password")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'LABEL', 'ADMIN')")
    public ResponseEntity<?> changePassword(@PathVariable Integer userId, @RequestBody String password) {
        // todo: surely we should require the user to enter a password verification. check verification here.
        User user = getUserById(userId);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!SecurityUtils.userCanEdit(user)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Delete a user
     * @param userId
     * @return Http.OK successful, Http.FORBIDDEN, no permission, Http.NOT_FOUND, cannot find user
     */
    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'LABEL', 'ADMIN')")
    public ResponseEntity<?> removeUser(@PathVariable Integer userId){
        User user = getUserById(userId);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!SecurityUtils.userCanEdit(user)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        userRepository.delete(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Helper to get user by Id
     * @param id User ID.
     * @return User, or null if the provided user ID is invalid.
     */
    private User getUserById(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            return null;
        }
        return userOptional.get();
    }
}

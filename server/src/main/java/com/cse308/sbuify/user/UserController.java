package com.cse308.sbuify.user;

import java.util.Optional;

import com.cse308.sbuify.admin.Admin;
import com.cse308.sbuify.email.Email;
import com.cse308.sbuify.email.NewAccountEmail;
import com.cse308.sbuify.email.PasswordResetEmail;
import com.cse308.sbuify.security.AuthFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/api/users")
public class UserController {

    @Autowired
    private AuthFacade authFacade;

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
     * Change user password given id
     * @param userId
     * @param password
     * @return Http.OK successful, Http.FORBIDDEN, no permission, Http.NOT_FOUND, cannot find user
     */
    @PostMapping(path = "/{id}/change-password")
    @PreAuthorize("hasAnyRole('CUSTOMER','ROLE_LABEL','ADMIN' )")
    public ResponseEntity<?> changePassword(@PathVariable Integer userId, @RequestBody String password){
        User user = getUserById(userId);
        if(user == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(!ownerOrAdmin(user)){
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
    @PreAuthorize("hasAnyRole('CUSTOMER','ROLE_LABEL','ADMIN' )")
    public ResponseEntity<?> removeUser(@PathVariable Integer userId){
        User user = getUserById(userId);
        if(user == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if(!ownerOrAdmin(user)){
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        userRepository.delete(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Helper to get user by Id
     * @param id
     * @return
     */
    private User getUserById(Integer id){
        Optional<User> userOptional = userRepository.findById(id);
        if(!userOptional.isPresent()){
            return null;
        }
        return userOptional.get();
    }

    /**
     * check if user is admin or current user
     * @param checkUser
     * @return
     */
    private boolean ownerOrAdmin(User checkUser){
        User user = authFacade.getCurrentUser();
        return user.equals(checkUser) || user instanceof Admin;
    }
    
}

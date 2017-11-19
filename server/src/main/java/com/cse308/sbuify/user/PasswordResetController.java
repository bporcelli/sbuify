package com.cse308.sbuify.user;


import com.cse308.sbuify.email.Email;
import com.cse308.sbuify.email.NewAccountEmail;
import com.cse308.sbuify.email.PasswordResetEmail;
import com.cse308.sbuify.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping(path = "/api/reset-password")
public class PasswordResetController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Give token to user for password reset and send password reset request
     *
     * @return ResponseEntity<>
     */
    @PostMapping
    public ResponseEntity<?> resetToken(@RequestBody String email) {

        Optional<User> existing = userRepository.findByEmail(email);

        // Return a 404 response no account found
        if (!existing.isPresent()) {

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User user = existing.get();

        user.setToken(SecurityUtils.generateToken());

        userRepository.save(user);


        Email resetEmail = new PasswordResetEmail(user);

        // send email
        // return 500 response, error sending email
        if ( !resetEmail.dispatch()) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * given a token, match the correct user, change the password
     *
     * @param token
     * @param password
     * @return ResponseEntity<>
     */
    @PostMapping("/")
    public ResponseEntity<?> changePassword(@RequestParam("token") String token, @RequestParam("password") String password){

        Optional<User> existing = userRepository.findByToken(token);

        // Return 400, invalid token, Not Found
        if (!existing.isPresent()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User existingUser = existing.get();

        // Hash password and save user
        existingUser.setPassword(passwordEncoder.encode(password));
        existingUser.setToken(null);

        userRepository.save(existingUser);


        return new ResponseEntity<>(HttpStatus.OK);

    }

}

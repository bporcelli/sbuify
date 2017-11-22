package com.cse308.sbuify.user;


import com.cse308.sbuify.email.Email;
import com.cse308.sbuify.email.PasswordResetEmail;
import com.cse308.sbuify.security.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;
import java.util.UUID;

@Controller
public class PasswordResetController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Send a password reset email to the user with the given email address.
     *
     * @param email Email of user who wants to reset their password.
     * @return ResponseEntity<>
     */
    @PostMapping(path = SecurityConstants.RESET_URL)
    public ResponseEntity<?> sendResetEmail(@RequestParam String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);

        if (!optionalUser.isPresent()) {  // invalid email
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User user = optionalUser.get();
        user.setToken(generateToken());
        userRepository.save(user);

        Email resetEmail = new PasswordResetEmail(user);
        if (!resetEmail.dispatch()) {  // error sending email
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Process a request to change a user's password.
     *
     * @param token Password reset token.
     * @param password New password.
     * @return ResponseEntity<>
     */
    @PostMapping(path = SecurityConstants.CHANGE_PASS_URL)
    public ResponseEntity<?> changePassword(@RequestParam String token, @RequestParam String password) {
        Optional<User> optionalUser = userRepository.findByToken(token);

        if (!optionalUser.isPresent()) {  // invalid user token
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        User user = optionalUser.get();

        // Hash password and save user
        user.setPassword(passwordEncoder.encode(password));
        user.setToken(null);
        userRepository.save(user);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Generate a cryptographically secure token that can be used to identify a user during the
     * password reset process.
     *
     * @return Password reset token.
     */
    private static String generateToken() {
        return UUID.randomUUID().toString();
    }
}

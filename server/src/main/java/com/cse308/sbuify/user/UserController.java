package com.cse308.sbuify.user;

import com.cse308.sbuify.common.api.DecorateResponse;
import com.cse308.sbuify.customer.LibraryProperties;
import com.cse308.sbuify.email.Email;
import com.cse308.sbuify.email.NewAccountEmail;
import com.cse308.sbuify.security.AuthFacade;
import com.cse308.sbuify.security.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/api/users")
public class UserController {

    private final Integer USERS_PER_PAGE;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthFacade authFacade;

    @Autowired
    public UserController(LibraryProperties properties) {
        USERS_PER_PAGE = properties.getItemPerPage();
    }

    /**
     * Get information about a user.
     * @param id Optional user ID. If not provided, the authenticated user is returned.
     * @return a 200 response with information about the user in the body.
     */
    @GetMapping(path = {"/current", "/{id}"})
    @DecorateResponse(type = User.class)
    public ResponseEntity<?> get(@PathVariable Optional<Integer> id) {
        if (!id.isPresent()) {
            return new ResponseEntity<>(authFacade.getCurrentUser(), HttpStatus.OK);
        }
        Optional<User> optionalUser = userRepository.findById(id.get());
        if (!optionalUser.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(optionalUser.get(), HttpStatus.OK);
    }

    /**
     * Get information about all users.
     * @param page Page index.
     * @return a 200 response containing a list of User objects on success.
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public @ResponseBody List<User> getAll(@RequestParam(defaultValue = "0") Integer page) {
        Page<User> userPage = userRepository.findAll(PageRequest.of(page, USERS_PER_PAGE));
        List<User> userList = new ArrayList<>();
        for (User user: userPage) {
            userList.add(user);
        }
        return userList;
    }

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
     * @param id ID of user.
     * @param req Request object containing the old and new user password.
     * @return Http.OK successful, Http.FORBIDDEN, no permission, Http.NOT_FOUND, cannot find user
     */
    @PostMapping(path = "/{id}/change-password")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'LABEL', 'ADMIN')")
    public ResponseEntity<?> changePassword(@PathVariable Integer id, @RequestBody PasswordChangeRequest req) {
        User user = getUserById(id);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!SecurityUtils.userCanEdit(user)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        // check: does old password match?
        if (!passwordEncoder.matches(req.getOldPassword(), user.getPassword())) {
            return new ResponseEntity<>("The password you entered is incorrect.", HttpStatus.FORBIDDEN);
        }

        user.setPassword(passwordEncoder.encode(req.getNewPassword()));
        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Delete a user
     * @param id
     * @return Http.OK successful, Http.FORBIDDEN, no permission, Http.NOT_FOUND, cannot find user
     */
    @DeleteMapping(path = "/{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'LABEL', 'ADMIN')")
    public ResponseEntity<?> removeUser(@PathVariable Integer id){
        User user = getUserById(id);
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

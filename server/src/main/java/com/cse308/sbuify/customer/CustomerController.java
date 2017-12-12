package com.cse308.sbuify.customer;

import com.cse308.sbuify.admin.Admin;
import com.cse308.sbuify.image.Base64Image;
import com.cse308.sbuify.image.Image;
import com.cse308.sbuify.image.StorageException;
import com.cse308.sbuify.image.StorageService;
import com.cse308.sbuify.security.AuthFacade;
import com.cse308.sbuify.security.SecurityUtils;
import com.cse308.sbuify.user.User;
import com.cse308.sbuify.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping(path = "/api/customer/")
public class CustomerController {

    @Autowired
    private AuthFacade authFacade;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SubscriptionRepository subscriptionRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private StorageService storageService;

    /**
     * Update a customer.
     * @param id ID of customer.
     * @param updated Updated customer, with fields that should be left unchanged set to null.
     * @return Http.OK successful, Http.FORBIDDEN, no permission, Http.NOT_FOUND, invalid Id
     */
    @PatchMapping(path = {"", "{id}"})
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<?> updateCustomer(@RequestBody Customer updated, @PathVariable Optional<Integer> id) {
        User currentUser = authFacade.getCurrentUser();

        // get the user to edit -- either the current user or the one with the given id
        User user;

        if (id.isPresent()) {
            user = getUserById(id.get());
        } else {
            user = currentUser;
        }

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!SecurityUtils.userCanEdit(user)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        if (!(currentUser instanceof Admin)) {
            String password = updated.getPassword();

            if (password == null || !bCryptPasswordEncoder.matches(password, user.getPassword())) {
                return new ResponseEntity<>("Invalid password", HttpStatus.FORBIDDEN);
            }
        }

        Customer customer = (Customer) user;

        if (updated.getFirstName() != null) {
            customer.setFirstName(updated.getFirstName());
        }
        if (updated.getLastName() != null) {
            customer.setLastName(updated.getLastName());
        }
        if (updated.getEmail() != null) {
            Optional<User> optionalUser = userRepository.findByEmail(updated.getEmail());
            if (optionalUser.isPresent() && !user.equals(optionalUser.get())) {  // email in use
                return new ResponseEntity<>("The email address you entered is in use", HttpStatus.BAD_REQUEST);
            }
            customer.setEmail(updated.getEmail());
        }
        if (updated.getBirthday() != null) {
            customer.setBirthday(updated.getBirthday());
        }

        customer = userRepository.save(customer);

        return new ResponseEntity<>(customer, HttpStatus.OK);
    }

    /**
     * Change the user's profile picture.
     * @param newImage Base64-encoded image.
     * @return a 200 response containing the updated image on success, otherwise a 400 response.
     */
    @PutMapping(path = "profile-picture")
    public ResponseEntity<?> updateProfilePicture(@RequestBody Base64Image newImage){
        Customer customer = getCurrentCustomer();
        String dataURL = newImage.getDataURL();
        Image image = null;
        if (!dataURL.isEmpty()) {  // new image selected
            try {
                image = storageService.save(newImage.getDataURL());
            } catch (StorageException e) {
                return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
            }
        }
        if (customer.getProfileImage() != null) {
            this.storageService.delete(customer.getProfileImage());
        }
        customer.setProfileImage(image);
        customer = userRepository.save(customer);
        return new ResponseEntity<>(customer.getProfileImage(), HttpStatus.OK);
    }

    /**
     * Upgrade the authenticated customer's account.
     * @param card The credit card entered by the user.
     * @return an empty 200 response on success.
     */
    @PostMapping(path = "subscription")
    public ResponseEntity<?> upgradeAccount(@RequestBody CreditCard card) {
        Customer customer = (Customer) authFacade.getCurrentUser();

        if (customer.isPremium()) {
            return new ResponseEntity<>("Customer is already a premium user.", HttpStatus.BAD_REQUEST);
        }
        if (!card.isValid()) {
            return new ResponseEntity<>("Invalid credit card number.", HttpStatus.BAD_REQUEST);
        }

        /* In reality, we would use a third party API to save the customer's payment information and create
         * their subscription; for our purposes, we just generate a dummy subscription. */
        String subscriptionId = UUID.randomUUID().toString();
        Subscription subscription = new Subscription(subscriptionId);
        customer.setSubscription(subscription);

        userRepository.save(customer);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Downgrade the authenticated customer's account.
     * @return an empty 200 response on success.
     */
    @DeleteMapping(path = "subscription")
    public ResponseEntity<?> downgradeAccount() {
        Customer customer = (Customer) authFacade.getCurrentUser();

        if (!customer.isPremium()) {
            return new ResponseEntity<>("Customer doesn't have a subscription.", HttpStatus.BAD_REQUEST);
        }

        // set subscription end date
        Subscription subscription = customer.getSubscription();
        subscription.setEnd(LocalDateTime.now());
        subscriptionRepo.save(subscription);

        // unset customer subscription
        customer.setSubscription(null);
        userRepository.save(customer);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Customer getCurrentCustomer(){
        return (Customer) authFacade.getCurrentUser();
    }

    /**
     * Helper to get a user by ID.
     * @param id User ID.
     * @return User, or null if the given user ID is invalid.
     */
    private User getUserById(Integer id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (!userOptional.isPresent()) {
            return null;
        }
        return userOptional.get();
    }
}

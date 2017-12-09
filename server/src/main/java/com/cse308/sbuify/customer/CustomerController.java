package com.cse308.sbuify.customer;

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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping(path = "/api/customer/")
public class CustomerController {

    @Autowired
    private AuthFacade authFacade;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StorageService storageService;

    /**
     * Update a customer.
     * @param id ID of customer.
     * @param updated Updated customer, with fields that should be left unchanged set to null.
     * @return Http.OK successful, Http.FORBIDDEN, no permission, Http.NOT_FOUND, invalid Id
     */
    @PatchMapping(path = "{id}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'ADMIN')")
    public ResponseEntity<?> updateCustomer(@PathVariable Integer id, @RequestBody Customer updated) {
        User user = getUserById(id);

        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (!SecurityUtils.userCanEdit(user)) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Customer customer = (Customer) user;

        if (updated.getFirstName() != null) {
            customer.setFirstName(updated.getFirstName());
        }
        if (updated.getLastName() != null) {
            customer.setLastName(updated.getLastName());
        }
        if (updated.getBirthday() != null) {
            customer.setBirthday(updated.getBirthday());
        }

        userRepository.save(customer);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Change the user's profile picture.
     * @param newImage Base64-encoded image.
     * @return Http.OK when successful, otherwise, Http.BAD_REQUEST
     */
    @PutMapping(path = "profile-picture")
    public ResponseEntity<?> updateProfilePicture(@RequestBody Base64Image newImage){
        Customer customer = getCurrentCustomer();
        Image image;
        try{
            image = storageService.save(newImage.getDataURL());
        } catch (StorageException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        if (customer.getProfileImage() != null) {
            this.storageService.delete(customer.getProfileImage());
        }
        customer.setProfileImage(image);
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

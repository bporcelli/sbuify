package com.cse308.sbuify.user;

import com.cse308.sbuify.common.AppConstants;
import com.cse308.sbuify.customer.*;
import com.cse308.sbuify.playlist.Library;
import com.cse308.sbuify.playlist.LibraryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping(path = "/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LibraryRepository libraryRepository;

    @Autowired
    private PlayQueueRepository playQueueRepository;

    @Autowired
    private PreferenceRepository preferenceRepository;

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

        if (user instanceof Customer){
            Customer customer = (Customer) user;
            if (!initCustomer(customer)){
                return new ResponseEntity<>(HttpStatus.CONFLICT);
            }
        }

        return new ResponseEntity<>("{}", HttpStatus.CREATED);
    }

    /*
        Create init Customer

     */
    private boolean initCustomer(Customer customer){
        try{
            // init customer library
            Library library = new Library(customer);
            customer.setLibrary(library);

            libraryRepository.save(library);

            //init playqueue

            PlayQueue playQueue = new PlayQueue(customer);
            customer.setPlayQueue(playQueue);
            playQueueRepository.save(playQueue);

            //init Preference

            List<Preference> preferences = new ArrayList<>();

            Preference language =  new Preference( CustomerConstants.LANGUAGE, Language.ENGLISH.name());
            Preference hdStream =  new Preference(CustomerConstants.HQ_STREAM, AppConstants.NO);
            preferences.add(language);
            preferences.add(hdStream);
            customer.setPreferences(preferences);

            preferenceRepository.saveAll(preferences);

            //save customer
            userRepository.save(customer);
            return true;
        } catch (Exception E){
            return false;
        }





    }
}

package com.cse308.sbuify.customer.preferences;

import com.cse308.sbuify.customer.Customer;
import com.cse308.sbuify.security.AuthFacade;
import com.cse308.sbuify.user.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping(path = "/api/customer/preferences")
public class PreferenceController {

    @Autowired
    private AuthFacade authFacade;

    @Autowired
    private UserRepository userRepository;
	// todo: handlers for all customer preference endpoints (get preferences from customer object)

    /**
     * Get user preferences
     * @return HTTP.OK with preferences in JSON body, HTTP.BAD_REQUEST, cannot process map<String,String>
     */
    @GetMapping
    public @ResponseBody Map<String,String> getPreferences(){
        Customer customer = (Customer) authFacade.getCurrentUser();
        Map<String, String> preferences = new HashMap<>();
        return preferences;

    }

    /**
     * Replace user preference with new preferences
     * @param newPreferences
     * @return HTTP.OK when successful
     */
    @PutMapping
    public ResponseEntity<?> replacePreference(@RequestBody Map<String,String> newPreferences){
        Customer customer = (Customer) authFacade.getCurrentUser();
        customer.setPreferences(newPreferences);
        userRepository.save(customer);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Replace a specific key with a specified value
     * @param key
     * @param value
     * @return HTTP.OK when successful
     */
    @PutMapping(path = "/{key}")
    public ResponseEntity<?> replacePreference(@PathVariable String key, @RequestBody String value){
        Customer customer = (Customer) authFacade.getCurrentUser();
        Map<String, String> preferences = customer.getPreferences();
        preferences.replace(key, value);
        userRepository.save(customer);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

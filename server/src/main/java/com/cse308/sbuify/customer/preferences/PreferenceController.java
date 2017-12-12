package com.cse308.sbuify.customer.preferences;

import com.cse308.sbuify.customer.Customer;
import com.cse308.sbuify.security.AuthFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping(path = "/api/customer/preferences")
public class PreferenceController {

    @Autowired
    private AuthFacade authFacade;

    @Autowired
    private PreferenceService prefService;

    /**
     * Get user preferences.
     * @return an OK response with the user's preferences in the body on success.
     */
    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public @ResponseBody Map<String, String> getPreferences() {
        Customer customer = (Customer) authFacade.getCurrentUser();
        return prefService.getAll(customer);
    }

    /**
     * Bulk update user preferences.
     * @param newPreferences Map from preference keys to new preference values.
     * @return an empty 200 response on success.
     */
    @PutMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> updatePreferences(@RequestBody Map<String, String> newPreferences) {
        Customer customer = (Customer) authFacade.getCurrentUser();
        for (String key: newPreferences.keySet()) {
            try {
                prefService.set(customer, key, newPreferences.get(key));
            } catch (PreferenceException ex) {  // invalid preference key/val
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * Update an individual user preference.
     * @param key Preference key.
     * @param value Preference value.
     * @return an empty 200 response on success.
     */
    @PutMapping(path = "/{key}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> updatePreference(@PathVariable String key, @RequestBody String value) {
        Customer customer = (Customer) authFacade.getCurrentUser();
        try {
            prefService.set(customer, key, value);
        } catch (PreferenceException ex) {  // bad preference key
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

package com.cse308.sbuify.customer.preferences;

import com.cse308.sbuify.customer.Customer;
import com.cse308.sbuify.customer.CustomerProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

/**
 * Provides utilities for saving and retrieving customer preferences.
 */
@Service
public class PreferenceService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PreferenceService.class);

    @Autowired
    private PreferenceRepository preferenceRepository;

    private final Map<String, Map<String, String>> DEFAULT_PREFS;

    @Autowired
    public PreferenceService(CustomerProperties properties) {
        DEFAULT_PREFS = properties.getPreferences();
    }

    /**
     * Get all preferences for a customer as a map from keys to values.
     * @param customer
     * @reutrn Map from customer preference keys to values.
     */
    public Map<String, String> getAll(Customer customer) {
        Map<String, String> prefMap = new HashMap<>();

        for (String key: DEFAULT_PREFS.keySet()) {
            Preference pref = preferenceRepository.get(customer, key);
            String value;
            if (pref != null) {
                value = pref.getValue();
            } else {
                value = DEFAULT_PREFS.get(key).get("default");
            }
            prefMap.put(key, value);
        }
        return prefMap;
    }

    /**
     * Get an individual customer preference.
     * @param customer Customer.
     * @param key Preference key.
     * @param type Preference type.
     * @return User-defined preference value, or the default value if no value is set.
     */
    public <T> T get(Customer customer, String key, Class<T> type) {
        Preference pref = preferenceRepository.get(customer, key);

        if (pref == null) {  // use default value
            if (DEFAULT_PREFS.containsKey(key)) {
                Map<String, String> defaultPref = DEFAULT_PREFS.get(key);
                pref = new Preference(key, defaultPref.get("default"));
            } else {
                throw new PreferenceException("Invalid preference key.");
            }
        }

        try {
            ObjectReader objectMapper = new ObjectMapper().readerFor(type);
            return objectMapper.readValue(pref.getValue());
        } catch (IOException ex) {
            return null;
        }
    }

    /**
     * Set an individual customer preference.
     * @param customer Customer.
     * @param key Preference key.
     * @param value Preference value.
     */
    public void set(Customer customer, String key, Object value) {
        if (!DEFAULT_PREFS.containsKey(key)) {
            throw new PreferenceException("Invalid preference key.");
        }

        String prefVal;
        if (value instanceof String) {
            prefVal = (String) value;  // assume value is already JSON encoded
        } else {
            Map<String, String> defaultPref = DEFAULT_PREFS.get(key);
            Class<?> clazz;
            try {
                clazz = Class.forName(defaultPref.get("type"));
            } catch (ClassNotFoundException e) {
                throw new PreferenceException("Failed to write preference.", e);
            }

            ObjectWriter objectMapper = new ObjectMapper().writerFor(clazz);
            try {
                prefVal = objectMapper.writeValueAsString(value);
            } catch (JsonProcessingException ex) {
                LOGGER.debug("Failed to write preference. Error was:", ex.getMessage());
                throw new PreferenceException("Failed to write preference.", ex);
            }
        }

        Preference pref = new Preference(key, prefVal);
        pref.setCustomer(customer);
        preferenceRepository.save(pref);
    }
}

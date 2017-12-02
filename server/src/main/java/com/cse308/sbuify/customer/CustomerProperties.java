package com.cse308.sbuify.customer;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties("customer")
public class CustomerProperties {

    /**
     * A map specifying valid customer preferences.
     *
     * Each map entry corresponds to one preference. The map key is the preference key and the value
     * is a map with keys 'type' (preference type) and 'default' (default value).
     */
    private Map<String, Map<String, String>> preferences;

    public Map<String, Map<String, String>> getPreferences() {
        return preferences;
    }

    public void setPreferences(Map<String, Map<String, String>> preferences) {
        this.preferences = preferences;
    }
}

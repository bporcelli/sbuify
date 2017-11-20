package com.cse308.sbuify.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public class SecurityUtils {

    /**
     * Generate a secure token to be used during the password reset process.
     * @return
     */
	public static String generateToken() {
	    return UUID.randomUUID().toString();
	}

    /**
     * Convert the given user's granted authorities to a list of strings.
     * @param user
     * @return a list of strings describing the user's granted authorities.
     */
	public static List<String> getAuthorityStrings(UserDetails user) {
        ArrayList<String> scopes = new ArrayList<>();

        for (GrantedAuthority auth: user.getAuthorities()) {
            scopes.add(auth.toString());
        }

        return scopes;
    }
}

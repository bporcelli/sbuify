package com.cse308.sbuify.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.cse308.sbuify.security.AuthFacade;
import com.cse308.sbuify.test.helper.AuthenticatedTest;
import com.cse308.sbuify.user.User;

public class ArtistRequestControllerTest extends AuthenticatedTest {
    
    @Autowired
    private AuthFacade authFacade;

    /**
     * 
     */
    @Test
    public void createRequestTest() {
        User user = authFacade.getCurrentUser();
        ResponseEntity<Void> response = restTemplate.postForEntity("/api/artist-requests", null, Void.class);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Override
    public String getEmail() {
        return "sbuify+admin@gmail.com";
    }

    @Override
    public String getPassword() {
        return "a";
    }
}

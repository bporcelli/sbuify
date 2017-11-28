package com.cse308.sbuify.test;

import com.cse308.sbuify.album.Album;
import com.cse308.sbuify.test.helper.AuthenticatedTest;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class AlbumControllerTest extends AuthenticatedTest {

    private final static int NUM_NEW_RELEASES = 30; // todo: make configurable with @ConfigurationProperties

    /**
     * Test: getting new releases.
     */
    @Test
    public void getNewReleases() {
        Map<String, String> params = new HashMap<>();
        params.put("page", "0");
        ResponseEntity<ArrayList<Album>> response = restTemplate.exchange("/api/albums/new-releases?page={page}",
                HttpMethod.GET, null, new ParameterizedTypeReference<ArrayList<Album>>() {}, params);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(NUM_NEW_RELEASES, response.getBody().size());
    }

    @Override
    public String getEmail() {
        return "sbuify+b@gmail.com";
    }

    @Override
    public String getPassword() {
        return "b";
    }
}
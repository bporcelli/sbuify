package com.cse308.sbuify.test;

import com.cse308.sbuify.test.helper.AuthenticatedTest;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;

public class SearchTest extends AuthenticatedTest {

    /**
     * Test: is search song work properly?
     */
    @Test
    public void searchSongs() {
        // todo: proper search w/demo data
        String keyword = "hello";

        // Send get request to the server and get the response
        ResponseEntity<String> response;

        response = restTemplate.getForEntity("http://localhost:" + port + "/api/search/songs?keyword=" + keyword,
                String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
    /**
     * Test: is search artists work properly? Unmanaged = false by default
     */
    @Test
    public void searchArtistsWithDefault() {
        // todo: proper search w/demo data
        String keyword = "hello";

        // Send get request to the server and get the response
        ResponseEntity<String> response;

        response = restTemplate.getForEntity("http://localhost:" + port + "/api/search/artists?keyword=" + keyword,
                String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
    /**
     * Test: is search artists work properly? Unmanaged = false by set value
     */
    @Test
    public void searchArtistsUnmanaged() {
        // todo: proper search w/demo data
        String keyword = "hello";

        // Send get request to the server and get the response
        ResponseEntity<String> response;

        response = restTemplate.getForEntity("http://localhost:" + port + "/api/search/artists?keyword=" + keyword + "&unmanaged=" + false,
                String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    

    /**
     * Test: is search artists work properly? Unmanaged = false by set value
     */
    @Test
    public void searchArtistsManaged() {
        // todo: proper search w/demo data
        String keyword = "hello";

        // Send get request to the server and get the response
        ResponseEntity<String> response;

        response = restTemplate.getForEntity("http://localhost:" + port + "/api/search/artists?keyword=" + keyword + "&unmanaged=" + true,
                String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
    /**
     * Test: is search albums work properly?
     */
    @Test
    public void searchAlbums() {
        // todo: proper search w/demo data
        String keyword = "hello";

        // Send get request to the server and get the response
        ResponseEntity<String> response;

        response = restTemplate.getForEntity("http://localhost:" + port + "/api/search/albums?keyword=" + keyword,
                String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
    /**
     * Test: is search playlist work properly?
     */
    @Test
    public void searchPlaylist() {
        // todo: proper search w/demo data
        String keyword = "hello";

        // Send get request to the server and get the response
        ResponseEntity<String> response;

        response = restTemplate.getForEntity("http://localhost:" + port + "/api/search/playlists?keyword=" + keyword,
                String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
    /**
     * Test: is search record label work properly?
     */
    @Test
    public void searchLabel() {
        // todo: proper search w/demo data
        String keyword = "hello";

        // Send get request to the server and get the response
        ResponseEntity<String> response;

        response = restTemplate.getForEntity("http://localhost:" + port + "/api/search/labels?keyword=" + keyword,
                String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Override
    public String getEmail() {
        return "a@sbuify.com";  // tests will run with user a@sbuify.com authenticated
    }

    @Override
    public String getPassword() {
        return "a";
    }
}

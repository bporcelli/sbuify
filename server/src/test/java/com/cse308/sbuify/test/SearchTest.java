package com.cse308.sbuify.test;

import static com.cse308.sbuify.test.TestUtils.randomEmail;
import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.cse308.sbuify.customer.Customer;
import com.cse308.sbuify.test.helper.LoginHelper;
import com.cse308.sbuify.user.UserRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SearchTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * Test: is search song work properly?
     */
    @Test
    public void searchSongs() {
        LoginHelper.simulateCustomerRegisterLogin(passwordEncoder, userRepository, port, restTemplate);
        
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
        LoginHelper.simulateCustomerRegisterLogin(passwordEncoder, userRepository, port, restTemplate);
        
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
        LoginHelper.simulateCustomerRegisterLogin(passwordEncoder, userRepository, port, restTemplate);
        
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
        LoginHelper.simulateCustomerRegisterLogin(passwordEncoder, userRepository, port, restTemplate);
        
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
        LoginHelper.simulateCustomerRegisterLogin(passwordEncoder, userRepository, port, restTemplate);
        
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
        LoginHelper.simulateCustomerRegisterLogin(passwordEncoder, userRepository, port, restTemplate);
        
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
        LoginHelper.simulateCustomerRegisterLogin(passwordEncoder, userRepository, port, restTemplate);
        
        String keyword = "hello";

        // Send get request to the server and get the response
        ResponseEntity<String> response;

        response = restTemplate.getForEntity("http://localhost:" + port + "/api/search/labels?keyword=" + keyword,
                String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
    
    
}

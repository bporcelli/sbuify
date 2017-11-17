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
        // Create an account
        Customer customer = new Customer(randomEmail(), "12345", "Jane", "Doe", new Date());
        LoginHelper.registerUser(passwordEncoder, userRepository, customer);

        // Test customer authentication
        ResponseEntity<Void> loginRes = LoginHelper.sendLoginRequest(port, restTemplate, customer);
        LoginHelper.checkToken(loginRes, customer);

        String keyword = "hello";

        // Send get request to the server and get the response
        ResponseEntity<String> response;

        response = restTemplate.getForEntity("http://localhost:" + port + "/api/search/songs?keyword=" + keyword,
                String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
